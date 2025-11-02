package net.ludocrypt.limlib.client.impl.effects.sky;

import net.ludocrypt.limlib.api.effects.sky.DimensionEffectsDto;
import net.ludocrypt.limlib.api.effects.sky.SkyTypeDto;
import net.minecraft.util.math.Vec3d;

public abstract class DimensionEffectsHelper {
	public static net.minecraft.client.render.DimensionEffects createDefault(DimensionEffectsDto dto) {
		return new net.minecraft.client.render.DimensionEffects(
			dto.cloudsHeight(),
			dto.alternateSkyColor(),
			create(dto.skyType()),
			dto.brightenLightning(),
			dto.darkened()) {

			@Override
			public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
				return dto.adjustFogColor(color, sunHeight);
			}

			@Override
			public boolean useThickFog(int camX, int camY) {
				return dto.useThickFog(camX, camY);
			}
		};
	}

	public static net.minecraft.client.render.DimensionEffects.SkyType create(SkyTypeDto dto) {
		switch (dto) {
			case NONE -> {
				return net.minecraft.client.render.DimensionEffects.SkyType.NONE;
			}
			case NORMAL -> {
				return net.minecraft.client.render.DimensionEffects.SkyType.NORMAL;
			}
			case END -> {
				return net.minecraft.client.render.DimensionEffects.SkyType.END;
			}
			default -> throw new RuntimeException("Cannot resolve %s to a corresponding vanilla Minecraft sky type".formatted(dto));
		}
	}
}
