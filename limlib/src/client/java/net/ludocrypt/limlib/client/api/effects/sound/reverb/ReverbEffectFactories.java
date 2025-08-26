package net.ludocrypt.limlib.client.api.effects.sound.reverb;

import net.ludocrypt.limlib.api.effects.sound.reverb.ReverbEffectDto;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public abstract class ReverbEffectFactories {
	private static final Map<Identifier, ReverbEffectFactory<?>> FACTORIES = new HashMap<>();

	public static <S extends ReverbEffectDto> void register(Identifier id, ReverbEffectFactory<S> factory) {
		FACTORIES.put(id, factory);
	}

	@SuppressWarnings("unchecked")
	public static <S extends ReverbEffectDto> ReverbEffect resolve(S data) {
		Identifier id = data.getId();
		ReverbEffectFactory<S> factory;
		try {
			factory = (ReverbEffectFactory<S>) FACTORIES.get(id);
		} catch (ClassCastException ex) {
			throw new IllegalStateException("No reverb effect factory for %s".formatted(id), ex);
		}
		if (factory == null) {
			throw new IllegalStateException("No reverb effect factory for %s".formatted(id));
		}
		return factory.create(data);
	}
}
