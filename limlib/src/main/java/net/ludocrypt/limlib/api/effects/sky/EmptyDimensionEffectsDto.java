package net.ludocrypt.limlib.api.effects.sky;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record EmptyDimensionEffectsDto() implements DimensionEffectsDto {
	public static final Codec<EmptyDimensionEffectsDto> CODEC = RecordCodecBuilder
		.create((instance) -> instance.stable(new EmptyDimensionEffectsDto()));

	@Override
	public Codec<EmptyDimensionEffectsDto> getCodec() {
		return CODEC;
	}

	@Override
	public float skyShading() {
		return 1.0F;
	}
}
