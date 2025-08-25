package net.ludocrypt.limlib.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.ludocrypt.limlib.api.effects.post.EmptyPostEffect;
import net.ludocrypt.limlib.api.effects.post.PostEffect;
import net.ludocrypt.limlib.api.effects.post.StaticPostEffect;
import net.ludocrypt.limlib.impl.mixin.RegistriesAccessor;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public abstract class LimlibRegistries {
	public static class PostEffects {
		public static final RegistryKey<Registry<PostEffect>> REGISTRY_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib/post_effect"));
		public static final RegistryKey<Registry<Codec<? extends PostEffect>>> CODEC_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib/codec/post_effect"));

		public static final Registry<Codec<? extends PostEffect>> REGISTRY = RegistriesAccessor
			.callCreate(CODEC_KEY, Lifecycle.stable(), (registry) -> StaticPostEffect.CODEC);
		public static final Codec<PostEffect> CODEC = REGISTRY
			.getCodec()
			.dispatchStable(PostEffect::getCodec, Function.identity());
	}

	public static class DimensionEffects {
		public static final RegistryKey<Registry<DimensionEffects>> REGISTRY_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib/dimension_effects"));
		public static final RegistryKey<Registry<Codec<? extends DimensionEffects>>> CODEC_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib/codec/dimension_effects"));

		public static final Registry<Codec<? extends DimensionEffects>> REGISTRY = RegistriesAccessor
			.callCreate(CODEC_KEY, Lifecycle.stable(), (registry) -> StaticDimensionEffects.CODEC);
		public static final Codec<DimensionEffects> CODEC = REGISTRY
			.getCodec()
			.dispatchStable(DimensionEffects::getCodec, Function.identity());
	}

	public static class SoundEffects {
	}

	public static class Skyboxes {

	}

	public static void init() {
		Limlib.LOGGER.info("Declaring registries");
		Registry.register(PostEffects.REGISTRY, new Identifier("limlib", "static"), StaticPostEffect.CODEC);
		Registry.register(PostEffects.REGISTRY, new Identifier("limlib", "empty"), EmptyPostEffect.CODEC);
	}
}
