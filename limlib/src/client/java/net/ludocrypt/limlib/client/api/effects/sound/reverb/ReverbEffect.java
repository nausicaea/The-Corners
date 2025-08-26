package net.ludocrypt.limlib.client.api.effects.sound.reverb;



import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;

/**
 * A Reverb effect controls
 */
public interface ReverbEffect {
	boolean isEnabled(MinecraftClient client, SoundInstance soundInstance);

	float getAirAbsorptionGainHF(MinecraftClient client, SoundInstance soundInstance);

	float getDecayHFRatio(MinecraftClient client, SoundInstance soundInstance);

	float getDensity(MinecraftClient client, SoundInstance soundInstance);

	float getDiffusion(MinecraftClient client, SoundInstance soundInstance);

	float getGain(MinecraftClient client, SoundInstance soundInstance);

	float getGainHF(MinecraftClient client, SoundInstance soundInstance);

	float getLateReverbGainBase(MinecraftClient client, SoundInstance soundInstance);

	float getDecayTime(MinecraftClient client, SoundInstance soundInstance);

	float getReflectionsGainBase(MinecraftClient client, SoundInstance soundInstance);

	int getDecayHFLimit(MinecraftClient client, SoundInstance soundInstance);

	float getLateReverbDelay(MinecraftClient client, SoundInstance soundInstance);

	float getReflectionsDelay(MinecraftClient client, SoundInstance soundInstance);

}
