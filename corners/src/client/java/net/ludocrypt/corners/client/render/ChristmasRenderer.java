package net.ludocrypt.corners.client.render;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.quiltmc.loader.api.minecraft.ClientOnly;

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

public class ChristmasRenderer extends SpecialModelRenderer {

	private final String id;

	public ChristmasRenderer(String id) {
		this.id = id;
	}

	@ClientOnly
	private double gazeTimer = 0;
	@ClientOnly
	private double gazeWaiting = 0;

	@Override
	@ClientOnly
	public void setup(MatrixStack matrices, Matrix4f viewMatrix, Matrix4f positionMatrix, float tickDelta,
			ShaderProgram shader, BlockPos origin) {

		if (CornerConfig.get().christmas.isChristmas()) {

			if (shader.getUniform("christmas") != null) {
				shader.getUniform("christmas").set(1);
			}

			for (int i = 0; i < 6; i++) {
				RenderSystem.setShaderTexture(i + 4, TheCorners.id("textures/sky/" + id + "_lights_" + i + ".png"));
				shader.addSampler("Light" + i, RenderSystem.getShaderTexture(i + 4));

				if (shader.getUniform("leftTint" + i) != null) {
					shader
						.getUniform("leftTint" + i)
						.set(new Vector4f(hexToRGBA(CornerConfig.get().christmas.leftColors
							.get((((int) Math.floor(RenderSystem.getShaderGameTime() * 1000)) + i) % CornerConfig
								.get().christmas.leftColors.size()))));
				}

				if (shader.getUniform("rightTint" + i) != null) {
					shader
						.getUniform("rightTint" + i)
						.set(new Vector4f(hexToRGBA(CornerConfig.get().christmas.rightColors
							.get((((int) Math.floor(RenderSystem.getShaderGameTime() * 1000)) + i) % CornerConfig
								.get().christmas.rightColors.size()))));
				}

			}

		} else {

			if (shader.getUniform("christmas") != null) {
				shader.getUniform("christmas").set(0);
			}

		}

		RenderSystem.setShaderTexture(0, TheCorners.id("textures/sky/" + id + ".png"));

		for (int i = 0; i < 3; i++) {
			RenderSystem.setShaderTexture(i + 1, TheCorners.id("textures/sky/" + id + "_twinkles_" + i + ".png"));
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
	@ClientOnly
	public MutableQuad modifyQuad(ChunkRendererRegion chunkRenderRegion, BlockPos pos, BlockState state, BakedModel model,
			BakedQuad quadIn, long modelSeed, MutableQuad quad) {
		quad.getV1().setUv(new Vec2f(0.0F, 0.0F));
		quad.getV2().setUv(new Vec2f(0.0F, 1.0F));
		quad.getV3().setUv(new Vec2f(1.0F, 1.0F));
		quad.getV4().setUv(new Vec2f(1.0F, 0.0F));
		return quad;
	}

	public static float[] hexToRGBA(String hex) {
		float[] rgba = new float[4];
		hex = hex.replace("#", "");
		hex = hex.replace(" ", "");

		if (hex.length() == 6) {
			rgba[0] = Integer.parseInt(hex.substring(0, 2), 16) / 255f; // Red
			rgba[1] = Integer.parseInt(hex.substring(2, 4), 16) / 255f; // Green
			rgba[2] = Integer.parseInt(hex.substring(4, 6), 16) / 255f; // Blue
			rgba[3] = 1.0f; // Alpha (fully opaque)
		} else if (hex.length() == 8) {
			rgba[0] = Integer.parseInt(hex.substring(0, 2), 16) / 255f; // Red
			rgba[1] = Integer.parseInt(hex.substring(2, 4), 16) / 255f; // Green
			rgba[2] = Integer.parseInt(hex.substring(4, 6), 16) / 255f; // Blue
			rgba[3] = Integer.parseInt(hex.substring(6, 8), 16) / 255f; // Alpha
		} else {
			throw new IllegalArgumentException("Invalid hexadecimal color format.");
		}

		return rgba;
	}

}
