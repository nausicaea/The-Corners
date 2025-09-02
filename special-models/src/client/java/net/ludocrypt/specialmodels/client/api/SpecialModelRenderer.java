package net.ludocrypt.specialmodels.client.api;

import net.ludocrypt.specialmodels.client.impl.ClientSharedMutableState;
import org.joml.Matrix4f;


import net.ludocrypt.specialmodels.impl.render.MutableQuad;
import net.ludocrypt.specialmodels.impl.render.Vec4b;
import net.minecraft.block.BlockState;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

public interface SpecialModelRenderer {
	void setup(MatrixStack matrices, Matrix4f viewMatrix, Matrix4f positionMatrix, float tickDelta,
			ShaderProgram shader, BlockPos chunkOrigin);

	default MutableQuad modifyQuad(ChunkRendererRegion chunkRenderRegion, BlockPos pos, BlockState state, BakedModel model,
			BakedQuad quadIn, long modelSeed, MutableQuad quad) {
		return quad;
	}

	default Matrix4f positionMatrix(Matrix4f in) {
		return in;
	}

	default Matrix4f viewMatrix(Matrix4f in) {
		return in;
	}

	default Vec4b appendState(ChunkRendererRegion chunkRenderRegion, BlockPos pos, BlockState state, BakedModel model,
			long modelSeed) {
		return new Vec4b(0, 0, 0, 0);
	}

	default ShaderProgram getShaderProgram(MatrixStack matrices, float tickDelta) {
		return ClientSharedMutableState.LOADED_SHADERS.get(this);
	}

}
