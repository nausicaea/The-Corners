package net.ludocrypt.limlib.client.api.effects.sky;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.client.render.DimensionEffects.SkyType;
import net.minecraft.util.math.Vec3d;

@ClientOnly
public class SkyPropertiesCreator {

	public static DimensionEffects create(float cloudHeight, boolean alternateSkyColor, String skyType,
			boolean brightenLighting, boolean darkened, boolean thickFog) {
		return new DimensionEffects(cloudHeight, alternateSkyColor, SkyType.valueOf(skyType), brightenLighting,
			darkened) {

			@Override
			public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
				return color;
			}

			@Override
			public boolean useThickFog(int camX, int camY) {
				return thickFog;
			}

		};
	}

}
