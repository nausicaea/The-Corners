package net.ludocrypt.limlib.api.effects.sky;

import com.mojang.serialization.Codec;

public interface DimensionEffectsDto {
	Codec<? extends DimensionEffectsDto> getCodec();
	float skyShading();
}
