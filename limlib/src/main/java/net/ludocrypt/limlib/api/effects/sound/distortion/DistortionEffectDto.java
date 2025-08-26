package net.ludocrypt.limlib.api.effects.sound.distortion;

import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;

public interface DistortionEffectDto {
	Codec<? extends DistortionEffectDto> getCodec();
	/**
	 * Whether or not a Sound Event should be ignored
	 *
	 * @param identifier the Identifier of the Sound Event
	 */
	boolean shouldIgnore(Identifier identifier);
}
