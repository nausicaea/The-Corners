package net.ludocrypt.limlib.api;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;

public interface LimlibRegistryProvider {
	<T> RegistryEntryLookup<T> get(RegistryKey<Registry<T>> key);
}
