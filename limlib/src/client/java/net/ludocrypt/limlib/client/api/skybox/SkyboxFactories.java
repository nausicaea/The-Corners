package net.ludocrypt.limlib.client.api.skybox;

import net.ludocrypt.limlib.api.effects.sound.reverb.ReverbEffectDto;
import net.ludocrypt.limlib.api.skybox.SkyboxDto;
import net.ludocrypt.limlib.client.api.effects.sound.reverb.ReverbEffect;
import net.ludocrypt.limlib.client.api.effects.sound.reverb.ReverbEffectFactory;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public abstract class SkyboxFactories {
	private static final Map<Identifier, SkyboxFactory<?>> FACTORIES = new HashMap<>();

	public static <S extends SkyboxDto> void register(Identifier id, SkyboxFactory<S> factory) {
		FACTORIES.put(id, factory);
	}

	@SuppressWarnings("unchecked")
	public static <S extends SkyboxDto> Skybox resolve(S data) {
		Identifier id = data.getId();
		SkyboxFactory<S> factory;
		try {
			factory = (SkyboxFactory<S>) FACTORIES.get(id);
		} catch (ClassCastException ex) {
			throw new IllegalStateException("No skybox factory for %s".formatted(id), ex);
		}
		if (factory == null) {
			throw new IllegalStateException("No skybox factory for %s".formatted(id));
		}
		return factory.create(data);
	}
}
