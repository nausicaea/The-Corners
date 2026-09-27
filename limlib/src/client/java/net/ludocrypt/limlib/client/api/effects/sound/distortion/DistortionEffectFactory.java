package net.ludocrypt.limlib.client.api.effects.sound.distortion;

import net.ludocrypt.limlib.api.effects.sound.distortion.DistortionEffectDto;

@FunctionalInterface
public interface DistortionEffectFactory<S extends DistortionEffectDto> {
	DistortionEffect create(S dto);
}
