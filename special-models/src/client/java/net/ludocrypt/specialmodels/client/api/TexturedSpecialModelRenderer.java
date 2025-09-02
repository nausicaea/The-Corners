package net.ludocrypt.specialmodels.client.api;

import net.ludocrypt.specialmodels.api.TexturedSpecialModelRendererDto;
import net.ludocrypt.specialmodels.impl.SpecialModels;
import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.Registry;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record TexturedSpecialModelRenderer(TexturedSpecialModelRendererDto dto) implements SpecialModelRenderer {
	@Override
	public void setup(MatrixStack matrices, Matrix4f viewMatrix, Matrix4f positionMatrix, float tickDelta,
			ShaderProgram shader, BlockPos origin) {
		RenderSystem.setShaderTexture(0, PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
	}
}
