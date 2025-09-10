package net.ludocrypt.limlib.client.impl.effects.sound.reverb;

@FunctionalInterface
public interface FallibleSupplier<T, E extends Throwable> {
	T get() throws E;
}
