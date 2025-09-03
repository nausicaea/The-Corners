package net.ludocrypt.specialmodels.client.impl.chunk;

import com.mojang.blaze3d.systems.VertexSorter;
import net.ludocrypt.specialmodels.client.api.SpecialModelRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class SortTask implements Task {

	private final BuiltChunk builtChunk;
	private final ChunkData data;
	private final SpecialModelRenderer renderer;
	private final double distance;
	private final AtomicBoolean cancelled = new AtomicBoolean(false);
	private final boolean highPriority;

	public SortTask(BuiltChunk builtChunk, double distance, ChunkData data, SpecialModelRenderer renderer) {
		this.distance = distance;
		this.highPriority = true;
		this.builtChunk = builtChunk;
		this.data = data;
		this.renderer = renderer;
	}

	@Override
	public String name() {
		return "rend_chk_sort";
	}

	@Override
	public double getDistance() {
		return this.distance;
	}

	@Override
	public AtomicBoolean getCancelled() {
		return this.cancelled;
	}

	@Override
	public boolean isHighPriority() {
		return this.highPriority;
	}

	@Override
	public CompletableFuture<Result> run(SpecialBufferBuilderStorage buffers) {

		if (this.cancelled.get()) {
			return CompletableFuture.completedFuture(Result.CANCELLED);
		} else if (!builtChunk.shouldBuild()) {
			this.cancelled.set(true);
			return CompletableFuture.completedFuture(Result.CANCELLED);
		} else if (this.cancelled.get()) {
			return CompletableFuture.completedFuture(Result.CANCELLED);
		} else {
			Vec3d cameraPos = builtChunk.specialChunkBuilder.getCameraPosition();
			float x = (float) cameraPos.x;
			float y = (float) cameraPos.y;
			float z = (float) cameraPos.z;

			if (this.data.bufferStates.containsKey(renderer) && !this.data.isEmpty(renderer)) {
				SortState sortState = this.data.bufferStates.get(renderer);
				SpecialBufferBuilder bufferBuilder = buffers.get(renderer);

				builtChunk.beginBufferBuilding(bufferBuilder);
				bufferBuilder.restoreState(sortState);

				bufferBuilder
					.setQuadSorting(VertexSorter
						.byDistance(x - (float) builtChunk.getOrigin().getX(),
							y - (float) builtChunk.getOrigin().getY(), z - (float) builtChunk.getOrigin().getZ()));

				this.data.bufferStates.put(renderer, bufferBuilder.popState());

				RenderedBuffer renderedBuffer = bufferBuilder.end();

				if (this.cancelled.get()) {
					renderedBuffer.release();
					return CompletableFuture.completedFuture(Result.CANCELLED);
				} else {
					CompletableFuture<Result> completableFuture = builtChunk.specialChunkBuilder
						.scheduleUpload(renderer, renderedBuffer, builtChunk.getBuffer(renderer))
						.thenApply(v -> Result.CANCELLED);
					return completableFuture.handle((result, throwable) -> {

						if (throwable != null && !(throwable instanceof CancellationException) && !(throwable instanceof InterruptedException)) {
							MinecraftClient
								.getInstance()
								.setCrashReportSupplierAndAddDetails(CrashReport.create(throwable, "Rendering chunk"));
						}

						return this.cancelled.get() ? Result.CANCELLED : Result.SUCCESSFUL;
					});
				}

			} else {
				return CompletableFuture.completedFuture(Result.CANCELLED);
			}

		}

	}

	@Override
	public void cancel() {
		this.cancelled.set(true);
	}

}
