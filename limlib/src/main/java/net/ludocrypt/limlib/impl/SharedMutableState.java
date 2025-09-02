package net.ludocrypt.limlib.impl;

public abstract class SharedMutableState {
	private SharedMutableState() {}

	public static void init() {
		Limlib.LOGGER.info("Initializing server-side shared mutable state");
	}
}
