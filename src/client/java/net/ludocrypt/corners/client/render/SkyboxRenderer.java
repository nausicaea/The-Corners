package net.ludocrypt.corners.client.render;

import net.ludocrypt.corners.render.SkyboxRendererDto;
import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;

import net.ludocrypt.corners.TheCorners;
import net.ludocrypt.corners.client.mixin.GameRendererAccessor;
import net.ludocrypt.specialmodels.client.api.SpecialModelRenderer;
import net.ludocrypt.specialmodels.impl.render.MutableQuad;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec2f;

public record SkyboxRenderer(SkyboxRendererDto dto) implements SpecialModelRenderer {
	@Override
	public void setup(MatrixStack matrices, Matrix4f viewMatrix, Matrix4f positionMatrix, float tickDelta,
			ShaderProgram shader, BlockPos origin) {

		for (int i = 0; i < 6; i++) {
			RenderSystem.setShaderTexture(i, TheCorners.id("textures/sky/%s_%d.png".formatted(dto.id(), i)));
		}

		MinecraftClient client = MinecraftClient.getInstance();
		Camera camera = client.gameRenderer.getCamera();
		Matrix4f matrix = new MatrixStack().peek().getPositionMatrix();
		matrix.rotate(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
		matrix.rotate(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));

		if (shader.getUniform("RotMat") != null) {
			shader.getUniform("RotMat").set(matrix);
		}

		MatrixStack matrixStack = new MatrixStack();
		((GameRendererAccessor) client.gameRenderer).callTiltViewWhenHurt(matrixStack, tickDelta);

		if (client.options.getBobView().getValue()) {
			((GameRendererAccessor) client.gameRenderer).callBobView(matrixStack, tickDelta);
		}

		if (shader.getUniform("bobMat") != null) {
			shader.getUniform("bobMat").set(matrixStack.peek().getPositionMatrix());
		}

	}

    @Override
    public boolean performOutside() {
        return dto.performOutside();
    }

    @Override
	public MutableQuad modifyQuad(ChunkRendererRegion chunkRenderRegion, BlockPos pos, BlockState state, BakedModel model,
			BakedQuad quadIn, long modelSeed, MutableQuad quad) {
		quad.v1().setUv(new Vec2f(0.0F, 0.0F));
		quad.v2().setUv(new Vec2f(0.0F, 1.0F));
		quad.v3().setUv(new Vec2f(1.0F, 1.0F));
		quad.v4().setUv(new Vec2f(1.0F, 0.0F));
		return quad;
	}

}
