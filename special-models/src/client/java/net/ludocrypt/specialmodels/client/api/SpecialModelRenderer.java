package net.ludocrypt.specialmodels.client.api;

import net.ludocrypt.specialmodels.client.impl.SpecialModelsClient;
import org.joml.Matrix4f;

import com.mojang.serialization.Lifecycle;

import net.ludocrypt.specialmodels.impl.SpecialModels;
import net.ludocrypt.specialmodels.impl.mixin.registry.RegistriesAccessor;
import net.ludocrypt.specialmodels.impl.render.MutableQuad;
import net.ludocrypt.specialmodels.impl.render.Vec4b;
import net.minecraft.block.BlockState;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public abstract class SpecialModelRenderer {

	public static final RegistryKey<Registry<SpecialModelRenderer>> SPECIAL_MODEL_RENDERER_KEY = RegistryKey
		.ofRegistry(new Identifier("limlib/special_model_renderer"));
	public static final Registry<SpecialModelRenderer> SPECIAL_MODEL_RENDERER = RegistriesAccessor
		.callCreate(SPECIAL_MODEL_RENDERER_KEY, Lifecycle.stable(),
			registry -> TexturedSpecialModelRenderer.TEXTURED);

	public final boolean performOutside;

	public SpecialModelRenderer() {
		this.performOutside = true;
	}

	public SpecialModelRenderer(boolean performOutside) {
		this.performOutside = performOutside;
	}

	public abstract void setup(MatrixStack matrices, Matrix4f viewMatrix, Matrix4f positionMatrix, float tickDelta,
			ShaderProgram shader, BlockPos chunkOrigin);

	public MutableQuad modifyQuad(ChunkRendererRegion chunkRenderRegion, BlockPos pos, BlockState state, BakedModel model,
			BakedQuad quadIn, long modelSeed, MutableQuad quad) {
		return quad;
	}

	public Matrix4f positionMatrix(Matrix4f in) {
		return in;
	}

	public Matrix4f viewMatrix(Matrix4f in) {
		return in;
	}

	public Vec4b appendState(ChunkRendererRegion chunkRenderRegion, BlockPos pos, BlockState state, BakedModel model,
			long modelSeed) {
		return new Vec4b(0, 0, 0, 0);
	}

	public ShaderProgram getShaderProgram(MatrixStack matrices, float tickDelta) {
		return SpecialModelsClient.LOADED_SHADERS.get(this);
	}

}
