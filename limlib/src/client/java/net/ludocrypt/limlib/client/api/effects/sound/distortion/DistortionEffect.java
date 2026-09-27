package net.ludocrypt.limlib.client.api.effects.sound.distortion;

import net.ludocrypt.limlib.api.effects.sound.distortion.DistortionEffectDto;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;

/**
 * A Distortion effect controls
 */
public interface DistortionEffect {
	boolean isEnabled(MinecraftClient client, SoundInstance soundInstance);

	float getEdge(MinecraftClient client, SoundInstance soundInstance);

	float getGain(MinecraftClient client, SoundInstance soundInstance);

	float getLowpassCutoff(MinecraftClient client, SoundInstance soundInstance);

	float getEQCenter(MinecraftClient client, SoundInstance soundInstance);

	float getEQBandWidth(MinecraftClient client, SoundInstance soundInstance);

}
