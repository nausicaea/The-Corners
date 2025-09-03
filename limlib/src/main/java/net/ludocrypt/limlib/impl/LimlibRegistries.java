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
import net.ludocrypt.limlib.api.effects.sky.DimensionEffectsDto;
import net.ludocrypt.limlib.api.effects.sky.EmptyDimensionEffectsDto;
import net.ludocrypt.limlib.api.effects.sky.StaticDimensionEffectsDto;
import net.ludocrypt.limlib.impl.effects.sound.SoundEffectsDto;
import net.ludocrypt.limlib.api.effects.sound.distortion.DistortionEffectDto;
import net.ludocrypt.limlib.api.effects.sound.distortion.StaticDistortionEffectDto;
import net.ludocrypt.limlib.api.effects.sound.reverb.ReverbEffectDto;
import net.ludocrypt.limlib.api.effects.sound.reverb.StaticReverbEffectDto;
import net.ludocrypt.limlib.api.skybox.EmptySkyboxDto;
import net.ludocrypt.limlib.api.skybox.SkyboxDto;
import net.ludocrypt.limlib.api.skybox.TexturedSkyboxDto;
import net.ludocrypt.limlib.impl.mixin.RegistriesAccessor;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.sound.MusicSound;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public abstract class LimlibRegistries {
	public static class PostFx {
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

	public static class DimFx {
		public static final RegistryKey<Registry<DimensionEffectsDto>> REGISTRY_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib/dimension_effects"));
		public static final RegistryKey<Registry<Codec<? extends DimensionEffectsDto>>> CODEC_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib/codec/dimension_effects"));

		public static final Registry<Codec<? extends DimensionEffectsDto>> REGISTRY = RegistriesAccessor
			.callCreate(CODEC_KEY, Lifecycle.stable(), (registry) -> StaticDimensionEffectsDto.CODEC);
		public static final Codec<DimensionEffectsDto> CODEC = REGISTRY
			.getCodec()
			.dispatchStable(DimensionEffectsDto::getCodec, Function.identity());
	}

	public static class DistFx {
		public static final RegistryKey<Registry<DistortionEffectDto>> REGISTRY_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib/distortion_effect"));
		public static final RegistryKey<Registry<Codec<? extends DistortionEffectDto>>> CODEC_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib/codec/distortion_effect"));
		public static final Registry<Codec<? extends DistortionEffectDto>> REGISTRY = RegistriesAccessor
			.callCreate(CODEC_KEY, Lifecycle.stable(), (registry) -> StaticDistortionEffectDto.CODEC);
		public static final Codec<DistortionEffectDto> CODEC = REGISTRY
			.getCodec()
			.dispatchStable(DistortionEffectDto::getCodec, Function.identity());
	}

	public static class RevFx {
		public static final RegistryKey<Registry<ReverbEffectDto>> REGISTRY_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib/reverb_effect"));
		public static final RegistryKey<Registry<Codec<? extends ReverbEffectDto>>> CODEC_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib/codec/reverb_effect"));
		public static final Registry<Codec<? extends ReverbEffectDto>> REGISTRY = RegistriesAccessor
			.callCreate(CODEC_KEY, Lifecycle.stable(), (registry) -> StaticReverbEffectDto.CODEC);
		public static final Codec<ReverbEffectDto> CODEC = REGISTRY
			.getCodec()
			.dispatchStable(ReverbEffectDto::getCodec, Function.identity());
	}

	public static class SndFx {
		public static final RegistryKey<Registry<SoundEffectsDto>> REGISTRY_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib/sound_effects"));
		public static final Codec<SoundEffectsDto> CODEC = RecordCodecBuilder.create((instance) -> instance
			.group(LimlibRegistries.RevFx.CODEC
					.optionalFieldOf("reverb")
					.stable()
					.forGetter(SoundEffectsDto::reverb),
				LimlibRegistries.DistFx.CODEC
					.optionalFieldOf("distortion")
					.stable()
					.forGetter(SoundEffectsDto::distortion),
				MusicSound.CODEC.optionalFieldOf("music")
					.stable()
					.forGetter(SoundEffectsDto::music))
			.apply(instance, instance.stable(SoundEffectsDto::new)));
	}

	public static class Skyboxes {
		public static final RegistryKey<Registry<SkyboxDto>> REGISTRY_KEY = RegistryKey.ofRegistry(new Identifier("limlib/skybox"));
		public static final RegistryKey<Registry<Codec<? extends SkyboxDto>>> CODEC_KEY = RegistryKey
			.ofRegistry(new Identifier("limlib/codec/skybox"));
		public static final Registry<Codec<? extends SkyboxDto>> REGISTRY = RegistriesAccessor
			.callCreate(CODEC_KEY, Lifecycle.stable(), (registry) -> TexturedSkyboxDto.CODEC);
		public static final Codec<SkyboxDto> CODEC = REGISTRY.getCodec().dispatchStable(SkyboxDto::getCodec, Function.identity());
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
		Limlib.LOGGER.debug("Declaring custom registries");
		Registry.register(PostFx.REGISTRY, new Identifier("limlib", "static"), StaticPostEffect.CODEC);
		Registry.register(PostFx.REGISTRY, new Identifier("limlib", "empty"), EmptyPostEffect.CODEC);
		Registry.register(DistFx.REGISTRY, StaticDistortionEffectDto.ID, StaticDistortionEffectDto.CODEC);
		Registry.register(RevFx.REGISTRY, StaticReverbEffectDto.ID, StaticReverbEffectDto.CODEC);
		Registry.register(Skyboxes.REGISTRY, EmptySkyboxDto.ID, EmptySkyboxDto.CODEC);
		Registry.register(Skyboxes.REGISTRY, TexturedSkyboxDto.ID, TexturedSkyboxDto.CODEC);
		Registry.register(DimFx.REGISTRY, StaticDimensionEffectsDto.ID, StaticDimensionEffectsDto.CODEC);
		Registry.register(DimFx.REGISTRY, EmptyDimensionEffectsDto.ID, EmptyDimensionEffectsDto.CODEC);
	}
}
