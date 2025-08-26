package net.ludocrypt.limlib.api.effects.sky;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public record EmptyDimensionEffectsDto() implements DimensionEffectsDto {
	public static final Codec<EmptyDimensionEffectsDto> CODEC = RecordCodecBuilder
		.create((instance) -> instance.stable(new EmptyDimensionEffectsDto()));

	@Override
	public @NotNull Codec<EmptyDimensionEffectsDto> getCodec() {
		return CODEC;
	}

	@Override
	public float skyShading() {
		return 1.0F;
	}

	@Override
	public float cloudsHeight() {
		return Float.NaN;
	}

	@Override
	public boolean alternateSkyColor() {
		return false;
	}

	@Override
	public @NotNull SkyTypeDto skyType() {
		return SkyTypeDto.NONE;
	}

	@Override
	public boolean brightenLightning() {
		return false;
	}

	@Override
	public boolean darkened() {
		return false;
	}

	@Override
	public @NotNull Vec3d adjustFogColor(Vec3d color, float sunHeight) {
		return color;
	}

	@Override
	public boolean useThickFog(int camX, int camY) {
		return false;
	}
}
