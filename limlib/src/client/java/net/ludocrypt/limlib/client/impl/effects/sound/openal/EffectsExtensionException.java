package net.ludocrypt.limlib.client.impl.effects.sound.openal;

import org.lwjgl.openal.AL11;

import java.util.Optional;

public class EffectsExtensionException extends OpenAlException {
	public EffectsExtensionException(String message) {
		super(message);
	}

	public static void expect(String message) throws EffectsExtensionException {
		Optional<String> err = OpenAl.toHumanReadableError(AL11.alGetError());
		if (err.isPresent()) {
			throw new EffectsExtensionException("%s: %s".formatted(message, err.get()));
		}
	}
}
