package net.ludocrypt.specialmodels.client.impl.chunk;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.ludocrypt.specialmodels.client.api.SpecialModelRenderer;
import net.ludocrypt.specialmodels.client.impl.render.SpecialVertexFormats;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.render.chunk.ChunkRendererRegionBuilder;
import net.minecraft.util.Util;
import net.minecraft.util.math.*;
import net.minecraft.world.chunk.ChunkStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class BuiltChunk {

	final SpecialChunkBuilder specialChunkBuilder;
	public final int index;

	public final AtomicReference<ChunkData> data = new AtomicReference<ChunkData>(ChunkData.EMPTY);
	final AtomicInteger cancelledInitialBuilds = new AtomicInteger(0);

	@Nullable
	private RebuildTask rebuildTask;
	public final Map<SpecialModelRenderer, SortTask> sortTasks = new Reference2ObjectArrayMap<>();

	private Box boundingBox;

	private boolean needsRebuild = true;
	private boolean needsImportantRebuild;

	private final BlockPos.Mutable origin = new BlockPos.Mutable(-1, -1, -1);

	private final BlockPos.Mutable[] neighbours = Util.make(new BlockPos.Mutable[6], pos -> {

		for (int i = 0; i < pos.length; ++i) {
			pos[i] = new BlockPos.Mutable();
		}

	});

	private final Map<SpecialModelRenderer, VertexBuffer> specialModelBuffers = SpecialModelRenderer.SPECIAL_MODEL_RENDERER
		.getEntrySet()
		.stream()
		.collect(Collectors.toMap(entry -> entry.getValue(), entry -> new VertexBuffer(VertexBuffer.Usage.STATIC)));

	public VertexBuffer getBuffer(SpecialModelRenderer modelRenderer) {
		return specialModelBuffers.get(modelRenderer);
	}

	public Map<SpecialModelRenderer, VertexBuffer> getSpecialModelBuffers() {
		return specialModelBuffers;
	}

	public BuiltChunk(SpecialChunkBuilder specialChunkBuilder, int index, int x, int y, int z) {
		this.specialChunkBuilder = specialChunkBuilder;
		this.index = index;
		this.setOrigin(x, y, z);
	}

	private boolean isChunkNonEmpty(BlockPos pos) {
		return specialChunkBuilder.world
			.getChunk(ChunkSectionPos.getSectionCoord(pos.getX()), ChunkSectionPos.getSectionCoord(pos.getZ()),
				ChunkStatus.FULL, false) != null;
	}

	public boolean shouldBuild() {

		if (!(this.getSquaredCameraDistance() > 576.0)) {
			return true;
		} else {
			return this.isChunkNonEmpty(this.neighbours[Direction.WEST.ordinal()]) && this
				.isChunkNonEmpty(this.neighbours[Direction.NORTH.ordinal()]) && this
				.isChunkNonEmpty(this.neighbours[Direction.EAST.ordinal()]) && this
				.isChunkNonEmpty(this.neighbours[Direction.SOUTH.ordinal()]);
		}

	}

	public Box getBoundingBox() {
		return this.boundingBox;
	}

	public void setOrigin(int x, int y, int z) {
		this.clear();
		this.origin.set(x, y, z);
		this.boundingBox = new Box(x, y, z, x + 16, y + 16, z + 16);

		for (Direction direction : Direction.values()) {
			this.neighbours[direction.ordinal()].set(this.origin).move(direction, 16);
		}

	}

	protected double getSquaredCameraDistance() {
		Camera camera = specialChunkBuilder.client.gameRenderer.getCamera();
		double x = this.boundingBox.minX + 8.0 - camera.getPos().x;
		double y = this.boundingBox.minY + 8.0 - camera.getPos().y;
		double z = this.boundingBox.minZ + 8.0 - camera.getPos().z;
		return x * x + y * y + z * z;
	}

	void beginBufferBuilding(SpecialBufferBuilder buffer) {
		buffer.begin(VertexFormat.DrawMode.QUADS, SpecialVertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL_STATE);
	}

	public ChunkData getData() {
		return this.data.get();
	}

	private void clear() {
		this.cancel();
		this.data.set(ChunkData.EMPTY);
		this.needsRebuild = true;
	}

	public void delete() {
		this.clear();
		this.specialModelBuffers.values().forEach(VertexBuffer::close);
	}

	public BlockPos getOrigin() {
		return this.origin;
	}

	public void scheduleRebuild(boolean important) {
		boolean neededRebuild = this.needsRebuild;
		this.needsRebuild = true;
		this.needsImportantRebuild = important | (neededRebuild && this.needsImportantRebuild);
	}

	public void cancelRebuild() {
		this.needsRebuild = false;
		this.needsImportantRebuild = false;
	}

	public boolean needsRebuild() {
		return this.needsRebuild;
	}

	public boolean needsImportantRebuild() {
		return this.needsRebuild && this.needsImportantRebuild;
	}

	public BlockPos getNeighborPosition(Direction direction) {
		return this.neighbours[direction.ordinal()];
	}

	public boolean scheduleSort(SpecialModelRenderer renderer, SpecialChunkBuilder chunkRenderer) {
		ChunkData data = this.getData();

		if (this.sortTasks.containsKey(renderer)) {
			this.sortTasks.get(renderer).cancel();
		}

		if (data.isEmpty(renderer)) {
			return false;
		} else {
			SortTask task = new SortTask(this, this.getSquaredCameraDistance(), data, renderer);
			this.sortTasks.put(renderer, task);
			chunkRenderer.send(task);
			return true;
		}

	}

	protected boolean cancel() {
		boolean cancelled = false;

		if (this.rebuildTask != null) {
			this.rebuildTask.cancel();
			this.rebuildTask = null;
			cancelled = true;
		}

		this.sortTasks.forEach((renderer, task) -> task.cancel());
		this.sortTasks.clear();

		return cancelled;
	}

	public Task createRebuildTask(ChunkRendererRegionBuilder cache) {
		boolean cancelled = this.cancel();

		BlockPos pos = this.origin.toImmutable();
		ChunkRendererRegion region = cache
			.build(specialChunkBuilder.world, pos.add(-1, -1, -1), pos.add(16, 16, 16), 1);

		boolean empty = this.data.get() == ChunkData.EMPTY;

		if (empty && cancelled) {
			this.cancelledInitialBuilds.incrementAndGet();
		}

		this.rebuildTask = new RebuildTask(this, this.getSquaredCameraDistance(), region,
			!empty || this.cancelledInitialBuilds.get() > 2);
		return this.rebuildTask;
	}

	public void scheduleRebuild(SpecialChunkBuilder builder, ChunkRendererRegionBuilder cache) {
		builder.send(this.createRebuildTask(cache));
	}

	public void rebuild(ChunkRendererRegionBuilder cache) {
		this.createRebuildTask(cache).run(specialChunkBuilder.buffers);
	}

}
