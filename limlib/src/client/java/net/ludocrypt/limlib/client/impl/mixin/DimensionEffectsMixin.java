package net.ludocrypt.limlib.client.impl.mixin;

import net.ludocrypt.limlib.client.api.effects.sky.DimensionEffectsFactories;
import net.ludocrypt.limlib.client.impl.ClientSharedMutableState;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.ludocrypt.limlib.client.impl.effects.LookupGrabber;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

@Mixin(net.minecraft.client.render.DimensionEffects.class)
public class DimensionEffectsMixin {

	@Inject(method = "byDimensionType(Lnet/minecraft/world/dimension/DimensionType;)Lnet/minecraft/client/render/DimensionEffects;", at = @At("HEAD"), cancellable = true)
	private static void limlib$byDimensionType(DimensionType dimensionType,
			CallbackInfoReturnable<net.minecraft.client.render.DimensionEffects> ci) {
		var effect = dimensionType.effects();
		var dimensionEffects = LookupGrabber
			.snatch(ClientSharedMutableState.MIXIN_WORLD_LOOKUP.get(),
				RegistryKey.of(LimlibRegistries.DimFx.REGISTRY_KEY, effect))
			.orElseThrow(() -> new IllegalStateException("Client: cannot find the dimension effect '%s' in registry".formatted(effect)));

		ci.setReturnValue(DimensionEffectsFactories.resolve(dimensionEffects));
	}
}
