package net.ludocrypt.limlib.api.effects.sky;

import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public interface DimensionEffectsDto {
	Identifier getId();
	@NotNull Codec<? extends DimensionEffectsDto> getCodec();
	float skyShading();
	float cloudsHeight();
	boolean alternateSkyColor();
	@NotNull
	SkyTypeDto skyType();
	boolean brightenLightning();
	boolean darkened();
	@NotNull Vec3d adjustFogColor(Vec3d color, float sunHeight);
	boolean useThickFog(int camX, int camY);
}
