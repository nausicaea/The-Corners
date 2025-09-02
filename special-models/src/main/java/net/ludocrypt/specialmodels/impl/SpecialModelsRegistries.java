package net.ludocrypt.specialmodels.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.ludocrypt.specialmodels.api.SpecialModelRendererDto;
import net.ludocrypt.specialmodels.api.TexturedSpecialModelRendererDto;
import net.ludocrypt.specialmodels.impl.mixin.registry.RegistriesAccessor;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public abstract class SpecialModelsRegistries {
	public static class Renderer {
		public static final RegistryKey<Registry<SpecialModelRendererDto>> REGISTRY_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib/special_model_renderer"));
		public static final RegistryKey<Registry<Codec<? extends SpecialModelRendererDto>>> CODEC_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib/codec/special_model_renderer"));
		public static final Registry<Codec<? extends SpecialModelRendererDto>> REGISTRY = RegistriesAccessor
			.callCreate(CODEC_KEY, Lifecycle.stable(),
				registry -> TexturedSpecialModelRendererDto.CODEC);
		public static final Codec<SpecialModelRendererDto> CODEC = REGISTRY
			.getCodec()
			.dispatchStable(SpecialModelRendererDto::getCodec, Function.identity());
	}

	public static void init() {
		SpecialModels.LOGGER.info("Declaring custom registries");
		Registry.register(Renderer.REGISTRY, TexturedSpecialModelRendererDto.ID, TexturedSpecialModelRendererDto.CODEC);
	}
}
