package net.ludocrypt.limlib.client.impl.mixin;

import java.util.Optional;

import net.ludocrypt.limlib.client.api.effects.sky.DimensionEffects;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.ludocrypt.limlib.api.effects.LookupGrabber;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

@Mixin(net.minecraft.client.render.DimensionEffects.class)
public class DimensionEffectsMixin {

	@Inject(method = "byDimensionType", at = @At("HEAD"), cancellable = true)
	private static void limlib$byDimensionType(DimensionType dimensionType,
			CallbackInfoReturnable<net.minecraft.client.render.DimensionEffects> ci) {
		Optional<DimensionEffects> dimensionEffects = LookupGrabber
			.snatch(DimensionEffects.MIXIN_WORLD_LOOKUP.get(),
				RegistryKey.of(LimlibRegistries.DimFx.REGISTRY_KEY, dimensionType.effects()));

		if (dimensionEffects.isPresent()) {
			ci.setReturnValue(dimensionEffects.get().getDimensionEffects());
		}

	}

}
