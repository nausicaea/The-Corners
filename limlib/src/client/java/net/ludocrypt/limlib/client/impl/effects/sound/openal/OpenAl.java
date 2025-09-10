package net.ludocrypt.limlib.client.impl.effects.sound.openal;

import org.lwjgl.openal.AL11;
import org.lwjgl.openal.EXTEfx;

import java.util.Optional;

public class OpenAl {
	private OpenAl() {}

	public static Optional<String> toHumanReadableError(int al11ErrorCode) {
		return switch (al11ErrorCode) {
			case AL11.AL_NO_ERROR -> Optional.empty();
			case AL11.AL_INVALID_NAME -> Optional.of("E%05X: Invalid name".formatted(al11ErrorCode));
			case AL11.AL_INVALID_ENUM -> Optional.of("E%05X: Invalid enum".formatted(al11ErrorCode));
			case AL11.AL_INVALID_VALUE -> Optional.of("E%05X: Invalid value".formatted(al11ErrorCode));
			case AL11.AL_INVALID_OPERATION -> Optional.of("E%05X: Invalid operation".formatted(al11ErrorCode));
			case AL11.AL_OUT_OF_MEMORY -> Optional.of("E%05X: Out of memory".formatted(al11ErrorCode));
			default -> Optional.of("E%05X: Unknown error code".formatted(al11ErrorCode));
		};
	}
}
