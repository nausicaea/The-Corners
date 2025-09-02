package net.ludocrypt.specialmodels.client.impl.chunk;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;

import net.ludocrypt.specialmodels.impl.SpecialModelsRegistries;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.mojang.logging.LogUtils;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.ludocrypt.specialmodels.client.api.SpecialModelRenderer;
import net.ludocrypt.specialmodels.client.impl.chunk.BuiltChunk.Task;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.ChunkOcclusionData;
import net.minecraft.client.render.chunk.ChunkRendererRegionBuilder;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.thread.TaskExecutor;

public class SpecialChunkBuilder {

	private static final Logger LOGGER = LogUtils.getLogger();

	private final PriorityBlockingQueue<Task> highPriorityChunksToBuild = Queues.newPriorityBlockingQueue();
	private final Queue<Task> chunksToBuild = Queues.<Task>newLinkedBlockingDeque();

	private int highPriorityQuota = 2;

	private final Queue<SpecialBufferBuilderStorage> threadBuffers;
	private final Queue<Runnable> uploadQueue = Queues.newConcurrentLinkedQueue();

	private volatile int queuedTaskCount;
	private volatile int bufferCount;

	private final SpecialBufferBuilderStorage buffers;
	private final TaskExecutor<Runnable> mailbox;
	private final Executor executor;

	private MinecraftClient client;
	private WorldRenderer worldRenderer;
	private ClientWorld world;

	private Vec3d cameraPosition = Vec3d.ZERO;

	public SpecialChunkBuilder(ClientWorld world, WorldRenderer renderer, Executor executor, boolean useMaxThreads,
			SpecialBufferBuilderStorage buffers) {
		this.client = MinecraftClient.getInstance();
		this.worldRenderer = renderer;
		this.world = world;
		this.buffers = buffers;

		int layer = Math
			.max(1,
				(int) (Runtime.getRuntime().maxMemory() * 0.3) / (RenderLayer
					.getSolid()
					.getExpectedBufferSize() * SpecialModelsRegistries.Renderer.REGISTRY.size() * 4) - 1);

		int avaliable = Runtime.getRuntime().availableProcessors();
		int minThreads = useMaxThreads ? avaliable : Math.min(avaliable, 4);
		int maxThreads = Math.max(1, Math.min(minThreads, layer));

		List<SpecialBufferBuilderStorage> storage = Lists.newArrayListWithExpectedSize(maxThreads);

		try {

			for (int i = 0; i < maxThreads; ++i) {
				storage.add(new SpecialBufferBuilderStorage());
			}

		} catch (OutOfMemoryError e) {
			LOGGER.warn("Allocated only {}/{} buffers", storage.size(), maxThreads);

			int size = Math.min(storage.size() * 2 / 3, storage.size() - 1);

			for (int i = 0; i < size; ++i) {
				storage.remove(storage.size() - 1);
			}

			System.gc();
		}

		this.threadBuffers = Queues.newArrayDeque(storage);
		this.bufferCount = this.threadBuffers.size();

		this.executor = executor;

		this.mailbox = TaskExecutor.create(executor, "Special Chunk Renderer");
		this.mailbox.send(this::scheduleRunTasks);
	}

	public void setWorld(ClientWorld world) {
		this.world = world;
	}

	private void scheduleRunTasks() {

		if (!this.threadBuffers.isEmpty()) {
			Task task = this.getNextBuildTask();

			if (task != null) {
				SpecialBufferBuilderStorage storage = this.threadBuffers.poll();
				this.queuedTaskCount = this.highPriorityChunksToBuild.size() + this.chunksToBuild.size();
				this.bufferCount = this.threadBuffers.size();
				CompletableFuture
					.supplyAsync(Util.debugSupplier(task.name(), () -> task.run(storage)), this.executor)
					.thenCompose(future -> future)
					.whenComplete((result, throwable) -> {

						if (throwable != null) {
							MinecraftClient.getInstance().setCrashReportSupplierAndAddDetails(CrashReport.create(throwable, "Batching chunks"));
						} else {
							this.mailbox.send(() -> {

								if (result == SpecialChunkBuilder.Result.SUCCESSFUL) {
									storage.clear();
								} else {
									storage.reset();
								}

								this.threadBuffers.add(storage);
								this.bufferCount = this.threadBuffers.size();
								this.scheduleRunTasks();
							});
						}

					});
			}

		}

	}

	@Nullable
	private Task getNextBuildTask() {

		if (this.highPriorityQuota <= 0) {
			Task task = this.chunksToBuild.poll();

			if (task != null) {
				this.highPriorityQuota = 2;
				return task;
			}

		}

		Task task = this.highPriorityChunksToBuild.poll();

		if (task != null) {
			--this.highPriorityQuota;
			return task;
		} else {
			this.highPriorityQuota = 2;
			return this.chunksToBuild.poll();
		}

	}

	public String getDebugString() {
		return String
			.format(Locale.ROOT, "pC: %03d, pU: %02d, aB: %02d", this.queuedTaskCount, this.uploadQueue.size(),
				this.bufferCount);
	}

	public int getToBatchCount() {
		return this.queuedTaskCount;
	}

	public int getChunksToUpload() {
		return this.uploadQueue.size();
	}

	public int getFreeBufferCount() {
		return this.bufferCount;
	}

	public void setCameraPosition(Vec3d cameraPosition) {
		this.cameraPosition = cameraPosition;
	}

	public Vec3d getCameraPosition() {
		return this.cameraPosition;
	}

	public void upload() {
		Runnable poll;

		while ((poll = this.uploadQueue.poll()) != null) {
			poll.run();
		}

	}

	public void rebuild(BuiltChunk chunk, ChunkRendererRegionBuilder cache) {
		chunk.rebuild(cache);
	}

	public void reset() {
		this.clear();
	}

	public void send(Task task) {
		this.mailbox.send(() -> {

			if (task.highPriority) {
				this.highPriorityChunksToBuild.offer(task);
			} else {
				this.chunksToBuild.offer(task);
			}

			this.queuedTaskCount = this.highPriorityChunksToBuild.size() + this.chunksToBuild.size();
			this.scheduleRunTasks();
		});
	}

	public CompletableFuture<Void> scheduleUpload(SpecialModelRenderer modelRenderer, RenderedBuffer renderedBuffer,
			VertexBuffer buffer) {
		return CompletableFuture.runAsync(() -> {

			if (!buffer.isClosed()) {
				buffer.bind();
				renderedBuffer.upload(buffer);
				VertexBuffer.unbind();
			}

		}, this.uploadQueue::add);
	}

	private void clear() {

		while (!this.highPriorityChunksToBuild.isEmpty()) {
			Task task = this.highPriorityChunksToBuild.poll();

			if (task != null) {
				task.cancel();
			}

		}

		while (!this.chunksToBuild.isEmpty()) {
			Task task = this.chunksToBuild.poll();

			if (task != null) {
				task.cancel();
			}

		}

		this.queuedTaskCount = 0;
	}

	public boolean isEmpty() {
		return this.queuedTaskCount == 0 && this.uploadQueue.isEmpty();
	}

	public void stop() {
		this.clear();
		this.mailbox.close();
		this.threadBuffers.clear();
	}

	public WorldRenderer worldRenderer() {
		return worldRenderer;
	}

	public SpecialBufferBuilderStorage buffers() {
		return buffers;
	}

	public ClientWorld world() {
		return world;
	}

	public MinecraftClient client() {
		return client;
	}

	public static class ChunkData {

		public static final SpecialChunkBuilder.ChunkData EMPTY = new SpecialChunkBuilder.ChunkData() {

			@Override
			public boolean isVisibleThrough(Direction from, Direction to) {
				return false;
			}

		};

		public final Map<SpecialModelRenderer, RenderedBuffer> renderedBuffers = new Reference2ObjectArrayMap<>();
		public final Map<SpecialModelRenderer, SortState> bufferStates = new Reference2ObjectArrayMap<>();

		public ChunkOcclusionData occlusionGraph = new ChunkOcclusionData();

		public boolean isEmpty() {
			return this.renderedBuffers.isEmpty();
		}

		public boolean isEmpty(SpecialModelRenderer layer) {
			return !this.renderedBuffers
				.containsKey(
					layer) || (this.renderedBuffers.containsKey(layer) && this.renderedBuffers.get(layer).isEmpty());
		}

		public boolean isVisibleThrough(Direction from, Direction to) {
			return this.occlusionGraph.isVisibleThrough(from, to);
		}

	}

	public static enum Result {
		SUCCESSFUL,
		CANCELLED;
	}

	public static class ChunkInfo {

		public final BuiltChunk chunk;
		private byte direction;
		public byte cullingState;
		public final int propagationLevel;

		public ChunkInfo(BuiltChunk chunk, @Nullable Direction direction, int propagationLevel) {
			this.chunk = chunk;

			if (direction != null) {
				this.addDirection(direction);
			}

			this.propagationLevel = propagationLevel;
		}

		public void updateCullingState(byte parentCullingState, Direction from) {
			this.cullingState = (byte) (this.cullingState | parentCullingState | 1 << from.ordinal());
		}

		public boolean canCull(Direction from) {
			return (this.cullingState & 1 << from.ordinal()) > 0;
		}

		public void addDirection(Direction direction) {
			this.direction = (byte) (this.direction | this.direction | 1 << direction.ordinal());
		}

		public boolean hasDirection(int ordinal) {
			return (this.direction & 1 << ordinal) > 0;
		}

		public boolean hasAnyDirection() {
			return this.direction != 0;
		}

		public boolean isAxisAlignedWith(int i, int j, int k) {
			BlockPos blockPos = this.chunk.getOrigin();
			return i == blockPos.getX() / 16 || k == blockPos.getZ() / 16 || j == blockPos.getY() / 16;
		}

		public int hashCode() {
			return this.chunk.getOrigin().hashCode();
		}

		public boolean equals(Object object) {

			if (!(object instanceof ChunkInfo)) {
				return false;
			} else {
				ChunkInfo chunkInfo = (ChunkInfo) object;
				return this.chunk.getOrigin().equals(chunkInfo.chunk.getOrigin());
			}

		}

	}

	public static class ChunkInfoListMap {

		private final ChunkInfo[] current;

		ChunkInfoListMap(int size) {
			this.current = new ChunkInfo[size];
		}

		public void setInfo(BuiltChunk chunk, ChunkInfo info) {
			this.current[chunk.index] = info;
		}

		@Nullable
		public ChunkInfo getInfo(BuiltChunk chunk) {
			int i = chunk.index;
			return i >= 0 && i < this.current.length ? this.current[i] : null;
		}

	}

	public static class RenderableChunks {

		public final ChunkInfoListMap builtChunkMap;
		public final LinkedHashSet<ChunkInfo> builtChunks;

		public RenderableChunks(int size) {
			this.builtChunkMap = new ChunkInfoListMap(size);
			this.builtChunks = new LinkedHashSet<ChunkInfo>(size);
		}

	}

}
