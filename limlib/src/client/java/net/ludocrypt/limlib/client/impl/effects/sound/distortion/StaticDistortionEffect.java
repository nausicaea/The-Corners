package net.ludocrypt.limlib.client.impl.effects.sound.distortion;

import net.ludocrypt.limlib.impl.effects.sound.distortion.StaticDistortionEffectDto;

import net.ludocrypt.limlib.client.api.effects.sound.distortion.DistortionEffect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;

/**
 * A Distortion effect controls
 * <p>
 * This is a simplification of the base {@link DistortionEffect} class, where
 * each setting is a static, non-changing value
 */
public record StaticDistortionEffect(StaticDistortionEffectDto dto) implements DistortionEffect {
	@Override
	public boolean isEnabled(MinecraftClient client, SoundInstance soundInstance) {
		return this.dto.enabled();
	}

	@Override
	public float getEdge(MinecraftClient client, SoundInstance soundInstance) {
		return this.dto.edge();
	}

	@Override
	public float getGain(MinecraftClient client, SoundInstance soundInstance) {
		return this.dto.gain();
	}

	@Override
	public float getLowpassCutoff(MinecraftClient client, SoundInstance soundInstance) {
		return this.dto.lowpassCutoff();
	}

	@Override
	public float getEQCenter(MinecraftClient client, SoundInstance soundInstance) {
		return this.dto.eqCenter();
	}

	@Override
	public float getEQBandWidth(MinecraftClient client, SoundInstance soundInstance) {
		return this.dto.eqBandWidth();
	}
}
