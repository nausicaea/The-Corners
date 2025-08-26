package net.ludocrypt.limlib.client.api.effects.sky;

import net.ludocrypt.limlib.api.effects.sky.DimensionEffectsDto;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public abstract class DimensionEffectsFactories {
	private static final Map<Identifier, DimensionEffectsFactory<?>> FACTORIES = new HashMap<>();

	public static <S extends DimensionEffectsDto> void register(Identifier id, DimensionEffectsFactory<S> factory) {
		FACTORIES.put(id, factory);
	}

	@SuppressWarnings("unchecked")
	public static <S extends DimensionEffectsDto> DimensionEffects resolve(S data) {
		Identifier id = data.getId();
		DimensionEffectsFactory<S> factory;
		try {
			factory = (DimensionEffectsFactory<S>) FACTORIES.get(id);
		} catch (ClassCastException ex) {
			throw new IllegalStateException("No reverb effect factory for %s".formatted(id), ex);
		}
		if (factory == null) {
			throw new IllegalStateException("No reverb effect factory for %s".formatted(id));
		}
		return factory.create(data);
	}
}
