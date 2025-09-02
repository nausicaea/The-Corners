package net.ludocrypt.corners.client.render;

import net.ludocrypt.corners.render.DeepBookshelfRendererDto;
import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;

import net.ludocrypt.corners.init.CornerBlocks;
import net.ludocrypt.corners.client.mixin.GameRendererAccessor;
import net.ludocrypt.specialmodels.client.api.SpecialModelRenderer;
import net.ludocrypt.specialmodels.impl.render.Vec4b;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;

public record DeepBookshelfRenderer(DeepBookshelfRendererDto dto) implements SpecialModelRenderer {

    @Override
	public void setup(MatrixStack matrices, Matrix4f viewMatrix, Matrix4f positionMatrix, float tickDelta,
			ShaderProgram shader, BlockPos origin) {
		RenderSystem.enablePolygonOffset();
		RenderSystem.polygonOffset(-3.0F, -3.0F);
		RenderSystem.setShaderTexture(0, PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
		RenderSystem.setShaderTexture(1, DeepBookshelfRendererDto.DEEP_BOOKSHELF_ATLAS_TEXTURE);
		MinecraftClient client = MinecraftClient.getInstance();
		Camera camera = client.gameRenderer.getCamera();
		MatrixStack matrixStack = new MatrixStack();
		((GameRendererAccessor) client.gameRenderer).callTiltViewWhenHurt(matrixStack, tickDelta);

		if (client.options.getBobView().getValue()) {
			((GameRendererAccessor) client.gameRenderer).callBobView(matrixStack, tickDelta);
		}

		MatrixStack basicStack = new MatrixStack();
		basicStack
			.multiplyPositionMatrix(client.gameRenderer
				.getBasicProjectionMatrix(((GameRendererAccessor) client.gameRenderer).callGetFov(camera, tickDelta, true)));

		if (shader.getUniform("BasicMat") != null) {
			shader.getUniform("BasicMat").set(basicStack.peek().getPositionMatrix());
		}

		if (shader.getUniform("BobMat") != null) {
			shader.getUniform("BobMat").set(matrixStack.peek().getPositionMatrix());
		}

		if (shader.getUniform("cameraPos") != null) {
			shader
				.getUniform("cameraPos")
				.set((float) camera.getPos().getX(), (float) camera.getPos().getY(), (float) camera.getPos().getZ());
		}

	}

    @Override
    public boolean performOutside() {
        return dto.performOutside();
    }

    @Override
	public Vec4b appendState(ChunkRendererRegion chunkRenderRegion, BlockPos pos, BlockState state, BakedModel model,
			long modelSeed) {

		if (state.isOf(CornerBlocks.DEEP_BOOKSHELF)) {
			byte b1 = 0;
			byte b2 = 0;
			byte b3 = 0;
			byte b4 = 0;

			if (state.get(Properties.SLOT_0_OCCUPIED)) {
				b1 += 1;
			}

			if (state.get(Properties.SLOT_1_OCCUPIED)) {
				b1 += 2;
			}

			if (state.get(Properties.SLOT_2_OCCUPIED)) {
				b1 += 4;
			}

			if (state.get(Properties.SLOT_3_OCCUPIED)) {
				b2 += 1;
			}

			if (state.get(Properties.SLOT_4_OCCUPIED)) {
				b2 += 2;
			}

			if (state.get(Properties.SLOT_5_OCCUPIED)) {
				b2 += 4;
			}

			return new Vec4b(b1, b2, b3, b4);
		}

		return SpecialModelRenderer.super.appendState(chunkRenderRegion, pos, state, model, modelSeed);
	}

}
