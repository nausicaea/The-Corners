package net.ludocrypt.limlib.client.api.effects.sound.reverb;

import net.ludocrypt.limlib.api.effects.sound.reverb.ReverbEffectDto;

@FunctionalInterface
public interface ReverbEffectFactory<S extends ReverbEffectDto> {
	ReverbEffect create(S dto);
}
