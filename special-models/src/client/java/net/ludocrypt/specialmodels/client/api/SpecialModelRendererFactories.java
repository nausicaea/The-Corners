package net.ludocrypt.specialmodels.client.api;


import net.ludocrypt.specialmodels.api.SpecialModelRendererDto;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public abstract class SpecialModelRendererFactories {
	private static final Map<Identifier, SpecialModelRendererFactory<?>> FACTORIES = new HashMap<>();

	public static <S extends SpecialModelRendererDto> void register(Identifier id, SpecialModelRendererFactory<S> factory) {
		FACTORIES.put(id, factory);
	}

	@SuppressWarnings("unchecked")
	public static <S extends SpecialModelRendererDto> SpecialModelRenderer resolve(S data) {
		Identifier id = data.getId();
		SpecialModelRendererFactory<S> factory;
		try {
			factory = (SpecialModelRendererFactory<S>) FACTORIES.get(id);
		} catch (ClassCastException ex) {
			throw new IllegalStateException("No special model renderer factory for %s".formatted(id), ex);
		}
		if (factory == null) {
			throw new IllegalStateException("No special model renderer factory for %s".formatted(id));
		}
		return factory.create(data);
	}
}
