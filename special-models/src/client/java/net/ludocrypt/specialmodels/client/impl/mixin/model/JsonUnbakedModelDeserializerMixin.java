package net.ludocrypt.specialmodels.client.impl.mixin.model;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;

import net.ludocrypt.specialmodels.impl.SpecialModelsRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.ludocrypt.specialmodels.client.api.SpecialModelRenderer;
import net.ludocrypt.specialmodels.client.impl.access.UnbakedModelAccess;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

@Mixin(JsonUnbakedModel.Deserializer.class)
public abstract class JsonUnbakedModelDeserializerMixin {

	@Inject(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/render/model/json/JsonUnbakedModel;", at = @At("RETURN"))
	private void specialModels$deserialize(JsonElement jsonElement, Type type,
			JsonDeserializationContext jsonDeserializationContext, CallbackInfoReturnable<JsonUnbakedModel> ci) {
		Map<SpecialModelRenderer, Identifier> map = Maps.newHashMap();
		JsonObject jsonObject = jsonElement.getAsJsonObject();

		if (jsonObject.has("specialmodels")) {
			JsonObject limlibExtra = jsonObject.get("specialmodels").getAsJsonObject();

			for (Entry<String, JsonElement> entry : limlibExtra.entrySet()) {

				if (SpecialModelsRegistries.Renderer.REGISTRY
					.contains(
						RegistryKey.of(SpecialModelsRegistries.Renderer.REGISTRY_KEY, new Identifier(entry.getKey())))) {
					map
						.put(SpecialModelsRegistries.Renderer.REGISTRY.get(new Identifier(entry.getKey())),
							new Identifier(entry.getValue().getAsString()));
				}

			}

		}

		((UnbakedModelAccess) ci.getReturnValue()).specialmodels$getSubModels().putAll(map);
	}

}
