package net.ludocrypt.specialmodels.client.impl.mixin.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;

import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.ludocrypt.specialmodels.client.api.SpecialModelRenderer;
import net.ludocrypt.specialmodels.client.impl.access.BakedModelAccess;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BasicBakedModel;
import net.minecraft.client.render.model.BuiltinBakedModel;
import net.minecraft.client.render.model.WeightedBakedModel;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { BasicBakedModel.class, BuiltinBakedModel.class, WeightedBakedModel.class, ForwardingBakedModel.class })
public class GeneralBakedModelMixin implements BakedModelAccess {

    private Map<BlockState, List<Pair<SpecialModelRenderer, BakedModel>>> modelsMap = new HashMap<>();
    private List<Pair<SpecialModelRenderer, BakedModel>> defaultModels = new ArrayList<>();

    @Override
    public List<Pair<SpecialModelRenderer, BakedModel>> specialmodels$getModels(@Nullable BlockState state) {
        return modelsMap.getOrDefault(state, defaultModels);
    }

    @Override
    public void specialmodels$addModel(SpecialModelRenderer modelRenderer, @Nullable BlockState state, BakedModel model) {
        if (state == null) {
            defaultModels.add(Pair.of(modelRenderer, model));
        } else {
            List<Pair<SpecialModelRenderer, BakedModel>> list = modelsMap.get(state);

            if (list == null) {
                list = Lists.newArrayList();
                modelsMap.put(state, list);
            }

            list.add(Pair.of(modelRenderer, model));
        }

    }

}
