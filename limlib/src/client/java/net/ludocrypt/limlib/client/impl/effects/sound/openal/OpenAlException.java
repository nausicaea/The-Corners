package net.ludocrypt.limlib.client.impl.effects.sound.openal;

import org.lwjgl.openal.AL11;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class OpenAlException extends Exception {
	OpenAlException(String message) {
		super(message);
	}

	public static AtomicBoolean ENABLE_ERROR_CHECKING = new AtomicBoolean(true);

	public static void expect(String message) throws OpenAlException {
		expect(() -> message);
	}

	public static void expect(Supplier<String> message) throws OpenAlException {
		if (!ENABLE_ERROR_CHECKING.get()) {
			return;
		}

		Optional<String> err = OpenAl.toHumanReadableError(AL11.alGetError());
		if (err.isPresent()) {
			throw new OpenAlException("%s: %s".formatted(message.get(), err.get()));
		}
	}
}
