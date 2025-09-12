package net.ludocrypt.limlib.client.impl.effects.sound.openal;

import org.lwjgl.openal.AL11;

import java.util.Optional;
import java.util.function.Supplier;

public class EffectsExtensionException extends OpenAlException {
	public EffectsExtensionException(String message) {
		super(message);
	}

	public static void expect(String message) throws EffectsExtensionException {
		expect(() -> message);
	}

	public static void expect(Supplier<String> message) throws EffectsExtensionException {
		if (!ENABLE_ERROR_CHECKING.get()) {
			return;
		}

		Optional<String> err = OpenAl.toHumanReadableError(AL11.alGetError());
		if (err.isPresent()) {
			throw new EffectsExtensionException("%s: %s".formatted(message.get(), err.get()));
		}
	}
}
