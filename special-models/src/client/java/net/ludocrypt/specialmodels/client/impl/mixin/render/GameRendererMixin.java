package net.ludocrypt.specialmodels.client.impl.mixin.render;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;

import com.llamalad7.mixinextras.sugar.Local;
import net.ludocrypt.specialmodels.client.impl.ClientSharedMutableState;
import net.ludocrypt.specialmodels.impl.SpecialModelsRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.datafixers.util.Pair;

import net.ludocrypt.specialmodels.client.api.SpecialModelRenderer;
import net.ludocrypt.specialmodels.impl.SpecialModels;
import net.ludocrypt.specialmodels.client.impl.render.SpecialVertexFormats;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.ResourceFactory;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Inject(method = "loadPrograms", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 58, shift = Shift.AFTER))
	private void specialModels$loadShaders(ResourceFactory manager, CallbackInfo ci,
										   @Local(ordinal = 1) List<Pair<ShaderProgram, Consumer<ShaderProgram>>> list2) {
		ClientSharedMutableState.LOADED_SHADERS.clear();
		SpecialModelsRegistries.Renderer.REGISTRY
			.getEntrySet()
			.stream()
			.map(Entry::getKey)
			.map(RegistryKey::getValue)
			.forEach((id) -> {

				SpecialModelRenderer renderer = SpecialModelsRegistries.Renderer.REGISTRY.get(id);

				if (renderer == null || !renderer.performOutside()) {
					return;
				}

				try {
					list2
						.add(Pair
							.of(new ShaderProgram(manager, "rendertype_" + id.getNamespace() + "_" + id.getPath(),
								SpecialVertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL_STATE),
								(shader) -> ClientSharedMutableState.LOADED_SHADERS.put(renderer, shader)));
				} catch (IOException e) {
					SpecialModels.LOGGER.error("Could not reload shader: {}", id);
					e.printStackTrace();

					try {
						list2
							.add(Pair
								.of(new ShaderProgram(manager, "rendertype_specialmodels_textured",
									SpecialVertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL_STATE),
									(shader) -> ClientSharedMutableState.LOADED_SHADERS.put(renderer, shader)));
					} catch (IOException e2) {
						list2.forEach((pair) -> pair.getFirst().close());
						e2.printStackTrace();
						throw new RuntimeException();
					}

				}

			});
	}

}
