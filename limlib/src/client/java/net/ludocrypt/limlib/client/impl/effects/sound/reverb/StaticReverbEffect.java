package net.ludocrypt.limlib.client.impl.effects.sound.reverb;

import net.ludocrypt.limlib.api.effects.sound.reverb.StaticReverbEffectDto;
import net.ludocrypt.limlib.client.api.effects.sound.reverb.ReverbEffect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;

/**
 * A Reverb effect controls
 * <p>
 * This is a simplification of the base {@link ReverbEffect} class, where each
 * setting is a static, non-changing value
 */
public record StaticReverbEffect(StaticReverbEffectDto dto) implements ReverbEffect {
	@Override
	public boolean isEnabled(MinecraftClient client, SoundInstance soundInstance) {
		return this.dto.enabled();
	}

	@Override
	public float getAirAbsorptionGainHF(MinecraftClient client, SoundInstance soundInstance) {
		return this.dto.airAbsorptionGainHF();
	}

	@Override
	public float getDecayHFRatio(MinecraftClient client, SoundInstance soundInstance) {
		return this.dto.decayHFRatio();
	}

	@Override
	public float getDensity(MinecraftClient client, SoundInstance soundInstance) {
		return this.dto.density();
	}

	@Override
	public float getDiffusion(MinecraftClient client, SoundInstance soundInstance) {
		return this.dto.diffusion();
	}

	@Override
	public float getGain(MinecraftClient client, SoundInstance soundInstance) {
		return this.dto.gain();
	}

	@Override
	public float getGainHF(MinecraftClient client, SoundInstance soundInstance) {
		return this.dto.gainHF();
	}

	@Override
	public float getLateReverbGainBase(MinecraftClient client, SoundInstance soundInstance) {
		return this.dto.lateReverbGainBase();
	}

	@Override
	public float getDecayTime(MinecraftClient client, SoundInstance soundInstance) {
		return this.dto.decayTime();
	}

	@Override
	public float getReflectionsGainBase(MinecraftClient client, SoundInstance soundInstance) {
		return this.dto.reflectionsGainBase();
	}

	@Override
	public int getDecayHFLimit(MinecraftClient client, SoundInstance soundInstance) {
		return this.dto.decayHFLimit();
	}

	@Override
	public float getLateReverbDelay(MinecraftClient client, SoundInstance soundInstance) {
		return this.dto.lateReverbDelay();
	}

	@Override
	public float getReflectionsDelay(MinecraftClient client, SoundInstance soundInstance) {
		return this.dto.reflectionsDelay();
	}
}
