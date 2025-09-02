package net.ludocrypt.limlib.client.impl;

import net.ludocrypt.limlib.api.effects.sky.DimensionEffectsDto;
import net.ludocrypt.limlib.impl.Limlib;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.atomic.AtomicReference;

public abstract class ClientSharedMutableState {
	public static final AtomicReference<RegistryWrapper<DimensionEffectsDto>> MIXIN_WORLD_LOOKUP = new AtomicReference<>();

	private ClientSharedMutableState() {}

	public static void init() {
		Limlib.LOGGER.info("Loading client-side shared mutable state");
	}
}
