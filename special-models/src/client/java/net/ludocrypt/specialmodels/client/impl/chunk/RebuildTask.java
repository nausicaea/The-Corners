package net.ludocrypt.specialmodels.client.impl.chunk;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.VertexSorter;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.ludocrypt.specialmodels.client.api.SpecialModelRenderer;
import net.ludocrypt.specialmodels.client.impl.access.BakedModelAccess;
import net.ludocrypt.specialmodels.client.impl.access.WorldChunkBuilderAccess;
import net.ludocrypt.specialmodels.client.impl.render.SpecialVertexFormats;
import net.ludocrypt.specialmodels.impl.SpecialModels;
import net.ludocrypt.specialmodels.impl.render.MutableQuad;
import net.ludocrypt.specialmodels.impl.render.MutableVertice;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public record RebuildTask(BuiltChunk builtChunk, AtomicReference<ChunkRendererRegion> region, double distance, AtomicBoolean cancelled, boolean highPriority) implements Task {

	public RebuildTask(BuiltChunk builtChunk, double distance, @Nullable ChunkRendererRegion region, boolean highPriority) {
		this(builtChunk, new AtomicReference<>(region), distance, new AtomicBoolean(false), highPriority);
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
	public String name() {
		return "rend_chk_rebuild";
	}

	@Override
	public CompletableFuture<Result> run(SpecialBufferBuilderStorage buffers) {

		if (this.cancelled.get()) {
			return CompletableFuture.completedFuture(Result.CANCELLED);
		} else if (!builtChunk.shouldBuild()) {
			this.region.set(null);
			builtChunk.scheduleRebuild(false);
			this.cancelled.set(true);
			return CompletableFuture.completedFuture(Result.CANCELLED);
		} else if (this.cancelled.get()) {
			return CompletableFuture.completedFuture(Result.CANCELLED);
		} else {
			Vec3d cameraPos = builtChunk.specialChunkBuilder.getCameraPosition();
			float x = (float) cameraPos.x;
			float y = (float) cameraPos.y;
			float z = (float) cameraPos.z;
			RenderedChunkData renderedChunkData = this.render(x, y, z, buffers);

			if (this.cancelled.get()) {
				renderedChunkData.renderedBuffers.values().forEach(RenderedBuffer::release);
				return CompletableFuture.completedFuture(Result.CANCELLED);
			} else {
				ChunkData chunkData = new ChunkData();

				chunkData.occlusionGraph = renderedChunkData.occlusionGraph;

				chunkData.bufferStates.clear();
				chunkData.bufferStates.putAll(renderedChunkData.bufferStates);

				List<CompletableFuture<Void>> results = Lists.newArrayList();
				renderedChunkData.renderedBuffers.forEach((modelRenderer, renderedBuffer) -> {

					results
						.add(builtChunk.specialChunkBuilder
							.scheduleUpload(modelRenderer, renderedBuffer,
								builtChunk.getBuffer(modelRenderer)));

					if (!renderedBuffer.isEmpty()) {
						chunkData.renderedBuffers.put(modelRenderer, renderedBuffer);
					} else {
						chunkData.renderedBuffers.remove(modelRenderer);
					}

				});
				return Util.combine(results).handle((listx, throwable) -> {

					if (throwable != null && !(throwable instanceof CancellationException) && !(throwable instanceof InterruptedException)) {
						MinecraftClient
							.getInstance()
							.setCrashReportSupplierAndAddDetails(CrashReport.create(throwable, "Rendering chunk"));
					}

					if (this.cancelled.get()) {
						return Result.CANCELLED;
					} else {
						builtChunk.data.set(chunkData);
						builtChunk.cancelledInitialBuilds.set(0);

						((WorldChunkBuilderAccess) (builtChunk.specialChunkBuilder.worldRenderer))
							.specialmodels$addSpecialBuiltChunk(builtChunk);

						return Result.SUCCESSFUL;
					}

				});
			}

		}

	}

	private RenderedChunkData render(float cameraX, float cameraY, float cameraZ,
									 SpecialBufferBuilderStorage buffers) {
		RenderedChunkData renderedChunkData = new RenderedChunkData();

		BlockPos originPos = builtChunk.getOrigin().toImmutable();
		BlockPos boundingPos = originPos.add(15, 15, 15);

		ChunkOcclusionDataBuilder chunkOcclusionDataBuilder = new ChunkOcclusionDataBuilder();
		ChunkRendererRegion chunkRenderRegion = this.region.get();
		this.region.set(null);

		MatrixStack matrixStack = new MatrixStack();

		if (chunkRenderRegion != null) {
			BlockModelRenderer.enableBrightnessCache();
			Random randomGenrator = Random.create();
			BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();

			for (BlockPos pos : BlockPos.iterate(originPos, boundingPos)) {
				BlockState state = chunkRenderRegion.getBlockState(pos);

				if (state.isOpaqueFullCube(chunkRenderRegion, pos)) {
					chunkOcclusionDataBuilder.markClosed(pos);
				}

				if (state.getRenderType() != BlockRenderType.INVISIBLE) {
					matrixStack.push();
					matrixStack
						.translate((float) (pos.getX() & 15), (float) (pos.getY() & 15), (float) (pos.getZ() & 15));
					BakedModel bakedModel = blockRenderManager.getModel(state);
					List<Pair<SpecialModelRenderer, BakedModel>> models = ((BakedModelAccess) bakedModel).specialmodels$getModels(state);

					BakedModel forward = blockRenderManager.getModel(state);

					while (models.isEmpty() && forward instanceof ForwardingBakedModel) {

						if (((ForwardingBakedModel) forward).getWrappedModel() != null) {
							models = ((BakedModelAccess) ((ForwardingBakedModel) forward).getWrappedModel())
								.specialmodels$getModels(state);
							forward = ((ForwardingBakedModel) forward).getWrappedModel();
						}

					}

					if (!models.isEmpty()) {

						for (Pair<SpecialModelRenderer, BakedModel> pair : models) {
							SpecialModelRenderer modelRenderer = pair.getFirst();
							BakedModel model = pair.getSecond();
							long modelSeed = state.getRenderingSeed(pos);
							SpecialBufferBuilder buffer = buffers.get(modelRenderer);
							buffer
								.setState(() -> modelRenderer
									.appendState(chunkRenderRegion, pos, state, model, modelSeed));

							if (!buffer.isBuilding()) {
								buffer
									.begin(VertexFormat.DrawMode.QUADS,
										SpecialVertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL_STATE);
							}

							ReconstructableModel constructedModel = new ReconstructableModel(model);
							constructedModel
								.setFunction((quads, blockState, direction, random) -> quads
									.stream()
									.map((quad) -> reconstructBakedQuad(chunkRenderRegion, pos, state, model,
										modelSeed, quad, modelRenderer))
									.toList());
							blockRenderManager
								.getModelRenderer()
								.render(chunkRenderRegion, constructedModel, state, pos, matrixStack, buffer, true,
									randomGenrator, modelSeed, OverlayTexture.DEFAULT_UV);
						}

					}

					matrixStack.pop();
				}

			}

			for (SpecialModelRenderer modelRenderer : buffers.getSpecialModelBuffers().keySet()) {
				SpecialBufferBuilder bufferBuilder = buffers.get(modelRenderer);

				if (!bufferBuilder.isCurrentBatchEmpty()) {
					bufferBuilder
						.setQuadSorting(VertexSorter
							.byDistance(cameraX - originPos.getX(), cameraY - originPos.getY(),
								cameraZ - originPos.getZ()));
					renderedChunkData.bufferStates.put(modelRenderer, bufferBuilder.popState());
				}

				if (!bufferBuilder.isBuilding()) {
					bufferBuilder
						.begin(VertexFormat.DrawMode.QUADS,
							SpecialVertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL_STATE);
				}

				RenderedBuffer renderedBuffer = bufferBuilder.end();

				if (renderedBuffer != null) {
					renderedChunkData.renderedBuffers.put(modelRenderer, renderedBuffer);
				}

			}

			BlockModelRenderer.disableBrightnessCache();
		}

		renderedChunkData.occlusionGraph = chunkOcclusionDataBuilder.build();
		return renderedChunkData;
	}

	private BakedQuad reconstructBakedQuad(ChunkRendererRegion region, BlockPos pos, BlockState state,
										   BakedModel model, long modelSeed, BakedQuad quad, SpecialModelRenderer modelRenderer) {
		int[] vertexData = quad.getVertexData();
		int vertexDataLength = 8;

		try (MemoryStack memoryStack = MemoryStack.stackPush()) {
			ByteBuffer byteBuffer = memoryStack
				.malloc(VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL.getVertexSizeByte());
			IntBuffer intBuffer = byteBuffer.asIntBuffer();
			int[] reconstructed = new int[vertexData.length];
			int uvIndex = 0;
			intBuffer.clear();
			intBuffer.put(vertexData, uvIndex, vertexDataLength);
			float x1 = byteBuffer.getFloat(0);
			float y1 = byteBuffer.getFloat(4);
			float z1 = byteBuffer.getFloat(8);
			float u1 = byteBuffer.getFloat(16);
			float v1 = byteBuffer.getFloat(20);
			uvIndex += vertexDataLength;
			intBuffer.clear();
			intBuffer.put(vertexData, uvIndex, vertexDataLength);
			float x2 = byteBuffer.getFloat(0);
			float y2 = byteBuffer.getFloat(4);
			float z2 = byteBuffer.getFloat(8);
			float u2 = byteBuffer.getFloat(16);
			float v2 = byteBuffer.getFloat(20);
			uvIndex += vertexDataLength;
			intBuffer.clear();
			intBuffer.put(vertexData, uvIndex, vertexDataLength);
			float x3 = byteBuffer.getFloat(0);
			float y3 = byteBuffer.getFloat(4);
			float z3 = byteBuffer.getFloat(8);
			float u3 = byteBuffer.getFloat(16);
			float v3 = byteBuffer.getFloat(20);
			uvIndex += vertexDataLength;
			intBuffer.clear();
			intBuffer.put(vertexData, uvIndex, vertexDataLength);
			float x4 = byteBuffer.getFloat(0);
			float y4 = byteBuffer.getFloat(4);
			float z4 = byteBuffer.getFloat(8);
			float u4 = byteBuffer.getFloat(16);
			float v4 = byteBuffer.getFloat(20);
			MutableQuad mutableQuad = modelRenderer
				.modifyQuad(region, pos, state, model, quad, modelSeed,
					new MutableQuad(new MutableVertice(x1, y1, z1, u1, v1), new MutableVertice(x2, y2, z2, u2, v2),
						new MutableVertice(x3, y3, z3, u3, v3), new MutableVertice(x4, y4, z4, u4, v4)));
			uvIndex = 0;
			intBuffer.clear();
			intBuffer.put(vertexData, uvIndex, vertexDataLength);
			byteBuffer.putFloat(0, (float) mutableQuad.v1().getPos().x);
			byteBuffer.putFloat(4, (float) mutableQuad.v1().getPos().y);
			byteBuffer.putFloat(8, (float) mutableQuad.v1().getPos().z);
			byteBuffer.putFloat(16, mutableQuad.v1().getUv().x);
			byteBuffer.putFloat(20, mutableQuad.v1().getUv().y);
			intBuffer.position(0);
			intBuffer.get(reconstructed, uvIndex, vertexDataLength);
			uvIndex += vertexDataLength;
			intBuffer.clear();
			intBuffer.put(vertexData, uvIndex, vertexDataLength);
			byteBuffer.putFloat(0, (float) mutableQuad.v2().getPos().x);
			byteBuffer.putFloat(4, (float) mutableQuad.v2().getPos().y);
			byteBuffer.putFloat(8, (float) mutableQuad.v2().getPos().z);
			byteBuffer.putFloat(16, mutableQuad.v2().getUv().x);
			byteBuffer.putFloat(20, mutableQuad.v2().getUv().y);
			intBuffer.position(0);
			intBuffer.get(reconstructed, uvIndex, vertexDataLength);
			uvIndex += vertexDataLength;
			intBuffer.clear();
			intBuffer.put(vertexData, uvIndex, vertexDataLength);
			byteBuffer.putFloat(0, (float) mutableQuad.v3().getPos().x);
			byteBuffer.putFloat(4, (float) mutableQuad.v3().getPos().y);
			byteBuffer.putFloat(8, (float) mutableQuad.v3().getPos().z);
			byteBuffer.putFloat(16, mutableQuad.v3().getUv().x);
			byteBuffer.putFloat(20, mutableQuad.v3().getUv().y);
			intBuffer.position(0);
			intBuffer.get(reconstructed, uvIndex, vertexDataLength);
			uvIndex += vertexDataLength;
			intBuffer.clear();
			intBuffer.put(vertexData, uvIndex, vertexDataLength);
			byteBuffer.putFloat(0, (float) mutableQuad.v4().getPos().x);
			byteBuffer.putFloat(4, (float) mutableQuad.v4().getPos().y);
			byteBuffer.putFloat(8, (float) mutableQuad.v4().getPos().z);
			byteBuffer.putFloat(16, mutableQuad.v4().getUv().x);
			byteBuffer.putFloat(20, mutableQuad.v4().getUv().y);
			intBuffer.position(0);
			intBuffer.get(reconstructed, uvIndex, vertexDataLength);
			return new BakedQuad(reconstructed, quad.getColorIndex(), quad.getFace(), quad.getSprite(),
				quad.hasShade());
		}

	}

	@Override
	public void cancel() {
		this.region.set(null);

		if (this.cancelled.compareAndSet(false, true)) {
			builtChunk.scheduleRebuild(false);
		}

	}

}
