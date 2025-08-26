package net.ludocrypt.limlib.api.skybox;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record EmptySkyboxDto() implements SkyboxDto {
	public static final Codec<EmptySkyboxDto> CODEC = RecordCodecBuilder
		.create((instance) -> instance.stable(new EmptySkyboxDto()));

	@Override
	public Codec<EmptySkyboxDto> getCodec() {
		return CODEC;
	}
}
