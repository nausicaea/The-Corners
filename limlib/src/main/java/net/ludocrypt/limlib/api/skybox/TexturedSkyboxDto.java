package net.ludocrypt.limlib.api.skybox;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

public record TexturedSkyboxDto(Identifier identifier) implements SkyboxDto {
	public static final Identifier ID = new Identifier("limlib", "textured");
	public static final Codec<TexturedSkyboxDto> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Identifier.CODEC.fieldOf("skybox").stable().forGetter((sky) -> sky.identifier)).apply(instance, instance.stable(TexturedSkyboxDto::new)));

	@Override
	public Identifier getId() {
		return ID;
	}

	@Override
	public Codec<TexturedSkyboxDto> getCodec() {
		return CODEC;
	}
}
