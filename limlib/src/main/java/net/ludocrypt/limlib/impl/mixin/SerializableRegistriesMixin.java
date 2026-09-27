package net.ludocrypt.limlib.impl.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.serialization.Codec;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SerializableRegistries;

@Mixin(SerializableRegistries.class)
public abstract class SerializableRegistriesMixin {

	@Inject(method = "method_45958()Lcom/google/common/collect/ImmutableMap;", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/SerializableRegistries;add(Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/registry/RegistryKey;Lcom/mojang/serialization/Codec;)V", ordinal = 2, shift = Shift.AFTER))
	private static void limlib$makeMap$mapped(
		CallbackInfoReturnable<ImmutableMap<RegistryKey<? extends Registry<?>>, SerializableRegistries.Info<?>>> ci,
		@Local Builder<RegistryKey<? extends Registry<?>>, SerializableRegistries.Info<?>> builder) {
		add(builder, LimlibRegistries.PostFx.REGISTRY_KEY, LimlibRegistries.PostFx.CODEC);
		add(builder, LimlibRegistries.DimFx.REGISTRY_KEY, LimlibRegistries.DimFx.CODEC);
		add(builder, LimlibRegistries.SndFx.REGISTRY_KEY, LimlibRegistries.SndFx.CODEC);
		add(builder, LimlibRegistries.Skyboxes.REGISTRY_KEY, LimlibRegistries.Skyboxes.CODEC);
	}

	@Shadow
	private native static <E> void add(
			Builder<RegistryKey<? extends Registry<?>>, SerializableRegistries.Info<?>> builder,
			RegistryKey<? extends Registry<E>> registryKey, Codec<E> codec);

}
