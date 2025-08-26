package net.ludocrypt.limlib.client.api.effects.sky;

import net.ludocrypt.limlib.api.effects.sky.DimensionEffectsDto;
import net.minecraft.client.render.DimensionEffects;

@FunctionalInterface
public interface DimensionEffectsFactory<S extends DimensionEffectsDto> {
	DimensionEffects create(S dto);
}
