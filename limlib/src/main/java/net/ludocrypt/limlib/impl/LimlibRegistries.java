package net.ludocrypt.limlib.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.ludocrypt.limlib.api.LimlibWorld;
import net.ludocrypt.limlib.api.effects.post.EmptyPostEffect;
import net.ludocrypt.limlib.api.effects.post.PostEffect;
import net.ludocrypt.limlib.api.effects.post.StaticPostEffect;
import net.ludocrypt.limlib.impl.mixin.RegistriesAccessor;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.sound.MusicSound;
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

	public static class DistortionEffects {
		public static final RegistryKey<Registry<DistortionEffect>> REGISTRY_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib/distortion_effect"));
		public static final RegistryKey<Registry<Codec<? extends DistortionEffect>>> CODEC_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib/codec/distortion_effect"));
		public static final Registry<Codec<? extends DistortionEffect>> REGISTRY = RegistriesAccessor
			.callCreate(CODEC_KEY, Lifecycle.stable(), (registry) -> StaticDistortionEffect.CODEC);
		public static final Codec<DistortionEffect> CODEC = REGISTRY
			.getCodec()
			.dispatchStable(DistortionEffect::getCodec, Function.identity());
	}

	public static class ReverbEffects {
		public static final RegistryKey<Registry<ReverbEffect>> REGISTRY_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib/reverb_effect"));
		public static final RegistryKey<Registry<Codec<? extends ReverbEffect>>> CODEC_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib/codec/reverb_effect"));
		public static final Registry<Codec<? extends ReverbEffect>> REGISTRY = RegistriesAccessor
			.callCreate(CODEC_KEY, Lifecycle.stable(), (registry) -> StaticReverbEffect.CODEC);
		public static final Codec<ReverbEffect> CODEC = REGISTRY
			.getCodec()
			.dispatchStable(ReverbEffect::getCodec, Function.identity());
	}

	public static class SoundEffects {
		public static final RegistryKey<Registry<SoundEffects>> REGISTRY_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib/sound_effects"));

		public static final Codec<SoundEffects> CODEC = RecordCodecBuilder.create((instance) -> {
			return instance.group(ReverbEffect.CODEC.optionalFieldOf("reverb").stable().forGetter((soundEffects) -> {
				return soundEffects.reverb;
			}), DistortionEffect.CODEC.optionalFieldOf("distortion").stable().forGetter((soundEffects) -> {
				return soundEffects.distortion;
			}), MusicSound.CODEC.optionalFieldOf("music").stable().forGetter((soundEffects) -> {
				return soundEffects.music;
			})).apply(instance, instance.stable(SoundEffects::new));
		});
	}

	public static class Skyboxes {
		public static final RegistryKey<Registry<Skybox>> REGISTRY_KEY = RegistryKey.ofRegistry(new Identifier("limlib/skybox"));
		public static final RegistryKey<Registry<Codec<? extends Skybox>>> CODEC_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib/codec/skybox"));
		public static final Registry<Codec<? extends Skybox>> REGISTRY = RegistriesAccessor
			.callCreate(CODEC_KEY, Lifecycle.stable(), (registry) -> TexturedSkybox.CODEC);
		public static final Codec<Skybox> CODEC = REGISTRY.getCodec().dispatchStable(Skybox::getCodec, Function.identity());
	}

	public static class Worlds {
		public static final RegistryKey<Registry<LimlibWorld>> REGISTRY_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib", "limlib_world"));
		public static final SimpleRegistry<LimlibWorld> REGISTRY = FabricRegistryBuilder
			.createSimple(REGISTRY_KEY)
			.attribute(RegistryAttribute.SYNCED)
			.buildAndRegister();
	}

	public static void init() {
		Limlib.LOGGER.info("Declaring registries");
		Registry.register(PostEffects.REGISTRY, new Identifier("limlib", "static"), StaticPostEffect.CODEC);
		Registry.register(PostEffects.REGISTRY, new Identifier("limlib", "empty"), EmptyPostEffect.CODEC);
	}
}
