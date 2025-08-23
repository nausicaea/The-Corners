package net.ludocrypt.limlib.api.effects.sky;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.quiltmc.loader.api.minecraft.ClientOnly;

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

	public static final RegistryKey<Registry<Codec<? extends DimensionEffects>>> DIMENSION_EFFECTS_CODEC_KEY = RegistryKey
		.ofRegistry(new Identifier("limlib/codec/dimension_effects"));
	public static final Registry<Codec<? extends DimensionEffects>> DIMENSION_EFFECTS_CODEC = RegistriesAccessor
		.callRegisterSimple(DIMENSION_EFFECTS_CODEC_KEY, Lifecycle.stable(), (registry) -> StaticDimensionEffects.CODEC);
	public static final Codec<DimensionEffects> CODEC = DIMENSION_EFFECTS_CODEC
		.getCodec()
		.dispatchStable(DimensionEffects::getCodec, Function.identity());
	public static final RegistryKey<Registry<DimensionEffects>> DIMENSION_EFFECTS_KEY = RegistryKey
		.ofRegistry(new Identifier("limlib/dimension_effects"));

	public static final AtomicReference<RegistryWrapper<DimensionEffects>> MIXIN_WORLD_LOOKUP = new AtomicReference<RegistryWrapper<DimensionEffects>>();

	public abstract Codec<? extends DimensionEffects> getCodec();

	@ClientOnly
	public abstract net.minecraft.client.render.DimensionEffects getDimensionEffects();

	public abstract float getSkyShading();

}
