package net.ludocrypt.limlib.api.skybox;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

public record EmptySkyboxDto() implements SkyboxDto {
	public static final Identifier ID = new Identifier("limlib", "empty");
	public static final Codec<EmptySkyboxDto> CODEC = RecordCodecBuilder
		.create((instance) -> instance.stable(new EmptySkyboxDto()));

	@Override
	public Identifier getId() {
		return ID;
	}

	@Override
	public Codec<EmptySkyboxDto> getCodec() {
		return CODEC;
	}
}
