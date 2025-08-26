package net.ludocrypt.limlib.api.effects.sky;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public record StaticDimensionEffectsDto(float cloudsHeight, boolean alternateSkyColor, SkyTypeDto skyType,
										boolean brightenLightning, boolean darkened, boolean thickFog,
										float skyShading) implements DimensionEffectsDto {
	public static final Codec<StaticDimensionEffectsDto> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
		Codec.FLOAT.fieldOf("cloud_height")
			.stable()
			.forGetter((effects) -> effects.cloudsHeight),
		Codec.BOOL.fieldOf("alternate_sky_color")
			.stable()
			.forGetter((effects) -> effects.alternateSkyColor),
		SkyTypeDto.CODEC.fieldOf("sky_type")
			.stable()
			.forGetter((effects) -> effects.skyType),
		Codec.BOOL.fieldOf("brighten_lighting")
			.stable()
			.forGetter((effects) -> effects.brightenLightning),
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

	public @NotNull Codec<StaticDimensionEffectsDto> getCodec() {
		return CODEC;
	}

	@Override
	public @NotNull Vec3d adjustFogColor(Vec3d color, float sunHeight) {
		return color;
	}

	@Override
	public boolean useThickFog(int camX, int camY) {
		return thickFog;
	}


}
