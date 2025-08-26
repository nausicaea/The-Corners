package net.ludocrypt.limlib.api.effects.sky;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public record StaticDimensionEffectsDto(Optional<Float> cloudHeight, boolean alternateSkyColor, String skyType,
										boolean brightenLighting, boolean darkened, boolean thickFog,
										float skyShading) implements DimensionEffectsDto {
	public static final Codec<StaticDimensionEffectsDto> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
		Codec.FLOAT.optionalFieldOf("cloud_height")
			.stable()
			.forGetter((effects) -> effects.cloudHeight),
		Codec.BOOL.fieldOf("alternate_sky_color")
			.stable()
			.forGetter((effects) -> effects.alternateSkyColor),
		Codec.STRING.fieldOf("sky_type")
			.stable()
			.forGetter((effects) -> effects.skyType),
		Codec.BOOL.fieldOf("brighten_lighting")
			.stable()
			.forGetter((effects) -> effects.brightenLighting),
		Codec.BOOL.fieldOf("darkened")
			.stable()
			.forGetter((effects) -> effects.darkened),
		Codec.BOOL.fieldOf("thick_fog")
			.stable()
			.forGetter((effects) -> effects.thickFog),
		Codec.FLOAT.fieldOf("sky_shading")
			.stable()
			.forGetter((effects) -> effects.skyShading)
	).apply(instance, instance.stable(StaticDimensionEffectsDto::new)));

	public Codec<StaticDimensionEffectsDto> getCodec() {
		return CODEC;
	}


}
