package net.ludocrypt.limlib.impl;

import net.ludocrypt.limlib.api.effects.sky.DimensionEffectsDto;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.atomic.AtomicReference;

public abstract class SharedMutableState {
	public static final AtomicReference<RegistryWrapper<DimensionEffectsDto>> MIXIN_WORLD_LOOKUP = new AtomicReference<>();

	private SharedMutableState() {}
}
