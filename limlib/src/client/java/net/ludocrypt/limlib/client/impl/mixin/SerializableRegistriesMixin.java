package net.ludocrypt.limlib.client.impl.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.serialization.Codec;

import net.ludocrypt.limlib.api.effects.post.PostEffect;
import net.ludocrypt.limlib.client.api.effects.sky.DimensionEffects;
import net.ludocrypt.limlib.client.api.effects.sound.SoundEffects;
import net.ludocrypt.limlib.client.api.skybox.Skybox;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SerializableRegistries;

@Mixin(SerializableRegistries.class)
public abstract class SerializableRegistriesMixin {

	@Inject(method = "method_45958()Lcom/google/common/collect/ImmutableMap;", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/SerializableRegistries;add(Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/registry/RegistryKey;Lcom/mojang/serialization/Codec;)V", ordinal = 2, shift = Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void limlib$makeMap$mapped(
			CallbackInfoReturnable<ImmutableMap<RegistryKey<? extends Registry<?>>, SerializableRegistries.Info<?>>> ci,
			Builder<RegistryKey<? extends Registry<?>>, SerializableRegistries.Info<?>> builder) {
		add(builder, PostEffect.POST_EFFECT_KEY, PostEffect.CODEC);
		add(builder, DimensionEffects.DIMENSION_EFFECTS_KEY, DimensionEffects.CODEC);
		add(builder, SoundEffects.SOUND_EFFECTS_KEY, SoundEffects.CODEC);
		add(builder, Skybox.SKYBOX_KEY, Skybox.CODEC);
	}

	@Shadow
	private native static <E> void add(
			Builder<RegistryKey<? extends Registry<?>>, SerializableRegistries.Info<?>> builder,
			RegistryKey<? extends Registry<E>> registryKey, Codec<E> codec);

}
