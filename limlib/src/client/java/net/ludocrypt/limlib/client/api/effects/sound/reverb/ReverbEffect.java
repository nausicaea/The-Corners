package net.ludocrypt.limlib.client.api.effects.sound.reverb;


import com.mojang.serialization.Codec;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.util.Identifier;

/**
 * A Reverb effect controls
 */
public abstract class ReverbEffect {
	public abstract Codec<? extends ReverbEffect> getCodec();

	/**
	 * Whether or not a Sound Event should be ignored
	 *
	 * @param identifier the Identifier of the Sound Event
	 */
	public abstract boolean shouldIgnore(Identifier identifier);

	public abstract boolean isEnabled(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getAirAbsorptionGainHF(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getDecayHFRatio(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getDensity(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getDiffusion(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getGain(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getGainHF(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getLateReverbGainBase(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getDecayTime(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getReflectionsGainBase(MinecraftClient client, SoundInstance soundInstance);

	public abstract int getDecayHFLimit(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getLateReverbDelay(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getReflectionsDelay(MinecraftClient client, SoundInstance soundInstance);

}
