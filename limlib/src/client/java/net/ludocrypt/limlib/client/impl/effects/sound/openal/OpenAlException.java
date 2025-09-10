package net.ludocrypt.limlib.client.impl.effects.sound.openal;

import org.lwjgl.openal.AL11;

import java.util.Optional;

public class OpenAlException extends Exception {
	OpenAlException(String message) {
		super(message);
	}

	public static void expect(String message) throws OpenAlException {
		Optional<String> err = OpenAl.toHumanReadableError(AL11.alGetError());
		if (err.isPresent()) {
			throw new OpenAlException("%s: %s".formatted(message, err.get()));
		}
	}
}
