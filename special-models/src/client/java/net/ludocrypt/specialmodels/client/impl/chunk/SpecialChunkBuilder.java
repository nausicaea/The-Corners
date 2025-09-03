package net.ludocrypt.specialmodels.client.impl.chunk;

import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;

import net.ludocrypt.specialmodels.impl.SpecialModels;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import net.ludocrypt.specialmodels.client.api.SpecialModelRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.ChunkRendererRegionBuilder;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.thread.TaskExecutor;

public class SpecialChunkBuilder {
	private final PriorityBlockingQueue<Task> highPriorityChunksToBuild = Queues.newPriorityBlockingQueue();
	private final Queue<Task> chunksToBuild = Queues.<Task>newLinkedBlockingDeque();

	private int highPriorityQuota = 2;

	private final Queue<SpecialBufferBuilderStorage> threadBuffers;
	private final Queue<Runnable> uploadQueue = Queues.newConcurrentLinkedQueue();

	private volatile int queuedTaskCount;
	private volatile int bufferCount;

	final SpecialBufferBuilderStorage buffers;
	private final TaskExecutor<Runnable> mailbox;
	private final Executor executor;

	final MinecraftClient client;
	final WorldRenderer worldRenderer;
	ClientWorld world;

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
					.getExpectedBufferSize() * SpecialModelRenderer.SPECIAL_MODEL_RENDERER.size() * 4) - 1);

		int avaliable = Runtime.getRuntime().availableProcessors();
		int minThreads = useMaxThreads ? avaliable : Math.min(avaliable, 4);
		int maxThreads = Math.max(1, Math.min(minThreads, layer));

		List<SpecialBufferBuilderStorage> storage = Lists.newArrayListWithExpectedSize(maxThreads);

		try {

			for (int i = 0; i < maxThreads; ++i) {
				storage.add(new SpecialBufferBuilderStorage());
			}

		} catch (OutOfMemoryError e) {
			SpecialModels.LOGGER.warn("Allocated only {}/{} buffers", storage.size(), maxThreads);

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

								if (result == Result.SUCCESSFUL) {
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

}
