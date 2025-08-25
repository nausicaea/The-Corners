package net.ludocrypt.limlib.client.api.effects.sky;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;

import net.ludocrypt.limlib.impl.mixin.RegistriesAccessor;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

/**
 * A non-client-side clone of {@link net.minecraft.client.render.DimensionEffects}
 */
public abstract class DimensionEffects {

	public static final AtomicReference<RegistryWrapper<DimensionEffects>> MIXIN_WORLD_LOOKUP = new AtomicReference<RegistryWrapper<DimensionEffects>>();

	public abstract Codec<? extends DimensionEffects> getCodec();

	public abstract net.minecraft.client.render.DimensionEffects getDimensionEffects();

	public abstract float getSkyShading();

}
