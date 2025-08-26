package net.ludocrypt.limlib.client.impl.skybox;

import com.mojang.blaze3d.systems.RenderSystem;
import net.ludocrypt.limlib.api.skybox.TexturedSkyboxDto;
import net.ludocrypt.limlib.client.api.skybox.Skybox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public record TexturedSkybox(TexturedSkyboxDto dto) implements Skybox {
	@Override
	public void render(ClientWorld world, Camera camera, MatrixStack matrices, float tickDelta) {
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());

		Vec3d color = world.getSkyColor(camera.getPos(), tickDelta).multiply(255);
		int r = (int) Math.floor(color.x);
		int g = (int) Math.floor(color.y);
		int b = (int) Math.floor(color.z);
		int a = 255;
		RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();

		for (int i = 0; i < 6; ++i) {
			matrices.push();

			if (i == 0) {
				matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
				matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
			}

			if (i == 1) {
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
			}

			if (i == 2) {
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
				matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
			}

			if (i == 3) {
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
				matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
			}

			if (i == 4) {
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
				matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90.0F));
			}

			Matrix4f matrix4f = matrices.peek().getPositionMatrix();

			RenderSystem.setShaderTexture(0, new Identifier(dto.identifier().toString() + "_" + i + ".png"));
			bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).texture(0.0F, 0.0F).color(r, g, b, a).next();
			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).texture(0.0F, 1.0F).color(r, g, b, a).next();
			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).texture(1.0F, 1.0F).color(r, g, b, a).next();
			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).texture(1.0F, 0.0F).color(r, g, b, a).next();
			BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
			matrices.pop();
		}

		RenderSystem.depthMask(true);
		RenderSystem.disableBlend();
	}
}
