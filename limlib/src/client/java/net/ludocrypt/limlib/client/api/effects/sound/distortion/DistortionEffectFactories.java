package net.ludocrypt.limlib.client.api.effects.sound.distortion;

import net.ludocrypt.limlib.api.effects.sound.distortion.DistortionEffectDto;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public abstract class DistortionEffectFactories {
	private static final Map<Identifier, DistortionEffectFactory<?>> FACTORIES = new HashMap<>();

	public static <S extends DistortionEffectDto> void register(Identifier id, DistortionEffectFactory<S> factory) {
		FACTORIES.put(id, factory);
	}

	@SuppressWarnings("unchecked")
	public static <S extends DistortionEffectDto> DistortionEffect resolve(S data) {
		Identifier id = data.getId();
		DistortionEffectFactory<S> factory;
		try {
			factory = (DistortionEffectFactory<S>) FACTORIES.get(id);
		} catch (ClassCastException ex) {
			throw new IllegalStateException("No distortion effect factory for %s".formatted(id), ex);
		}
		if (factory == null) {
			throw new IllegalStateException("No distortion effect factory for %s".formatted(id));
		}
		return factory.create(data);
	}
}
