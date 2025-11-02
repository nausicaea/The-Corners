package net.ludocrypt.specialmodels.client.impl.chunk;

import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

import java.util.List;

public final class ReconstructableModel extends ForwardingBakedModel {

	private BlockStateVariantMap.QuadFunction<List<BakedQuad>, BlockState, Direction, Random, List<BakedQuad>> function;

	public ReconstructableModel(BakedModel model) {
		this.wrapped = model;
	}

	public void setFunction(
		BlockStateVariantMap.QuadFunction<List<BakedQuad>, BlockState, Direction, Random, List<BakedQuad>> function) {
		this.function = function;
	}

	@Override
	public List<BakedQuad> getQuads(BlockState blockState, Direction face, Random rand) {
		return function.apply(super.getQuads(blockState, face, rand), blockState, face, rand);
	}

}
