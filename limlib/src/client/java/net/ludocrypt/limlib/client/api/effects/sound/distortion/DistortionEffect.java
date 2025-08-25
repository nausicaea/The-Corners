package net.ludocrypt.limlib.client.api.effects.sound.distortion;


import com.mojang.serialization.Codec;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.util.Identifier;

/**
 * A Distortion effect controls
 */
public abstract class DistortionEffect {
	public abstract Codec<? extends DistortionEffect> getCodec();

	/**
	 * Whether or not a Sound Event should be ignored
	 *
	 * @param identifier the Identifier of the Sound Event
	 */
	public abstract boolean shouldIgnore(Identifier identifier);

	public abstract boolean isEnabled(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getEdge(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getGain(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getLowpassCutoff(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getEQCenter(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getEQBandWidth(MinecraftClient client, SoundInstance soundInstance);

}
