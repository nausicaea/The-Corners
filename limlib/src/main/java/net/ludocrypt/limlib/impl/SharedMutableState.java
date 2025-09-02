package net.ludocrypt.limlib.impl;

import net.minecraft.registry.DynamicRegistryManager;

import java.util.concurrent.atomic.AtomicReference;

public abstract class SharedMutableState {
	public static final AtomicReference<DynamicRegistryManager.Immutable> LOADED_REGISTRY = new AtomicReference<DynamicRegistryManager.Immutable>();

	private SharedMutableState() {}

	public static void init() {
		Limlib.LOGGER.info("Initializing server-side shared mutable state");
	}
}
