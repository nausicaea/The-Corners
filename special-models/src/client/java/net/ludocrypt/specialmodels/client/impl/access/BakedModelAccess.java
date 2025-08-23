package net.ludocrypt.specialmodels.client.impl.access;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.mojang.datafixers.util.Pair;

import net.ludocrypt.specialmodels.client.api.SpecialModelRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;

public interface BakedModelAccess {

	public List<Pair<SpecialModelRenderer, BakedModel>> getModels(@Nullable BlockState state);

	public void addModel(SpecialModelRenderer modelRenderer, @Nullable BlockState state, BakedModel model);

}
