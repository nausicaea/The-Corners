package net.ludocrypt.corners.client.render;

import net.ludocrypt.corners.render.ChristmasRendererDto;
import net.ludocrypt.corners.util.ColorConversion;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.mojang.blaze3d.systems.RenderSystem;

import net.ludocrypt.corners.TheCorners;
import net.ludocrypt.corners.config.CornerConfig;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec2f;

public class ChristmasRenderer implements SpecialModelRenderer {
    private final ChristmasRendererDto dto;
    private double gazeTimer = 0;
    private double gazeWaiting = 0;

    public ChristmasRenderer(ChristmasRendererDto dto) {
        this.dto = dto;
    }

	@Override
	public void setup(MatrixStack matrices, Matrix4f viewMatrix, Matrix4f positionMatrix, float tickDelta,
			ShaderProgram shader, BlockPos origin) {

		if (CornerConfig.get().christmas.isChristmas()) {

			if (shader.getUniform("christmas") != null) {
				shader.getUniform("christmas").set(1);
			}

			for (int i = 0; i < 6; i++) {
				RenderSystem.setShaderTexture(i + 4, TheCorners.id("textures/sky/%s_lights_%d.png".formatted(dto.id(), i)));
				shader.addSampler("Light" + i, RenderSystem.getShaderTexture(i + 4));

				if (shader.getUniform("leftTint" + i) != null) {
					shader
						.getUniform("leftTint" + i)
						.set(new Vector4f(ColorConversion.hexToRGBA(CornerConfig.get().christmas.leftColors
							.get((((int) Math.floor(RenderSystem.getShaderGameTime() * 1000)) + i) % CornerConfig
								.get().christmas.leftColors.size()))));
				}

				if (shader.getUniform("rightTint" + i) != null) {
					shader
						.getUniform("rightTint" + i)
						.set(new Vector4f(ColorConversion.hexToRGBA(CornerConfig.get().christmas.rightColors
							.get((((int) Math.floor(RenderSystem.getShaderGameTime() * 1000)) + i) % CornerConfig
								.get().christmas.rightColors.size()))));
				}

			}

		} else {

			if (shader.getUniform("christmas") != null) {
				shader.getUniform("christmas").set(0);
			}

		}

		RenderSystem.setShaderTexture(0, TheCorners.id("textures/sky/%s.png".formatted(dto.id())));

		for (int i = 0; i < 3; i++) {
			RenderSystem.setShaderTexture(i + 1, TheCorners.id("textures/sky/%s_twinkles_%d.png".formatted(dto.id(), i)));
			shader.addSampler("Twinkle" + i, RenderSystem.getShaderTexture(i + 1));
		}

		if (shader.getUniform("GameTime") != null) {
			shader.getUniform("GameTime").set(RenderSystem.getShaderGameTime());
		}

		MinecraftClient client = MinecraftClient.getInstance();
		Camera camera = client.gameRenderer.getCamera();
		Matrix4f matrix = new MatrixStack().peek().getPositionMatrix();
		matrix.rotate(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
		matrix.rotate(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
		double gazeAngle = Math.max(Math.toRadians(camera.getPitch() % 360) / Math.PI * -2, 0);

		if (gazeAngle > 0.4) {
			gazeWaiting += ((gazeAngle - 0.5) / (0.5)) * (0.004) + 0.001;

			if (gazeWaiting > 0.5) {
				gazeTimer += ((gazeAngle - 0.5) / (0.5)) * (0.002) + 0.001;
			}

		} else {
			gazeTimer -= 0.01D;
			gazeWaiting = 0.0D;
		}

		gazeTimer = MathHelper.clamp(gazeTimer, 0, 1);

		if (shader.getUniform("gaze") != null) {
			shader.getUniform("gaze").set((float) gazeTimer);
		}

		if (shader.getUniform("RotMat") != null) {
			shader.getUniform("RotMat").set(matrix);
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
