package net.ludocrypt.specialmodels.client.impl.mixin.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import net.ludocrypt.specialmodels.impl.SpecialModels;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Maps;

import net.ludocrypt.specialmodels.client.api.SpecialModelRenderer;
import net.ludocrypt.specialmodels.client.impl.access.BakedModelAccess;
import net.ludocrypt.specialmodels.client.impl.access.UnbakedModelAccess;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

@Mixin(JsonUnbakedModel.class)
public abstract class JsonUnbakedModelMixin implements UnbakedModelAccess {

	private final Map<SpecialModelRenderer, Identifier> subModels = new HashMap<>();

	@Inject(method = "bake(Lnet/minecraft/client/render/model/Baker;Lnet/minecraft/client/render/model/json/JsonUnbakedModel;Ljava/util/function/Function;Lnet/minecraft/client/render/model/ModelBakeSettings;Lnet/minecraft/util/Identifier;Z)Lnet/minecraft/client/render/model/BakedModel;", at = @At("RETURN"))
	private void specialModels$bake(Baker loader, JsonUnbakedModel parent, Function<SpriteIdentifier, Sprite> textureGetter,
			ModelBakeSettings settings, Identifier id, boolean hasDepth, CallbackInfoReturnable<BakedModel> ci) {
		this.specialmodels$getSubModels().forEach((modelRenderer, modelId) -> {
			SpecialModels.LOGGER.debug("Processing identifier {}, submodel {}", id, modelId);
			if (!modelId.equals(id)) {
				UnbakedModel model = loader.getOrLoadModel(modelId);
				model.setParents(loader::getOrLoadModel);
				BakedModel bakedModel = model.bake(loader, textureGetter, settings, modelId);

				SpecialModels.LOGGER.debug("Adding model({}) {} as baked model {} to model renderer {} with settings {}", modelId, model, bakedModel, modelRenderer, settings);
				((BakedModelAccess) ci.getReturnValue()).specialmodels$addModel(modelRenderer, null, bakedModel);
			} else {
				SpecialModels.LOGGER.warn("Model '{}' caught in chain! Renderer '{}' caught model '{}'", id, modelRenderer, modelId);
			}

		});
	}

	@Override
	public Map<SpecialModelRenderer, Identifier> specialmodels$getSubModels() {
		return subModels;
	}

}
