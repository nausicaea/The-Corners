package net.ludocrypt.limlib.client.impl.mixin;

import net.ludocrypt.limlib.api.effects.sky.DimensionEffectsDto;
import net.ludocrypt.limlib.impl.Limlib;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.ludocrypt.limlib.api.effects.LookupGrabber;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.registry.RegistryKey;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {

	@ModifyVariable(method = "render(Lnet/minecraft/client/render/Camera;FLnet/minecraft/client/world/ClientWorld;IF)V", at = @At(value = "STORE", ordinal = 3), ordinal = 2)
	private static float limlib$modifySkyColor(float original) {
		MinecraftClient client = MinecraftClient.getInstance();
		ClientWorld world = client.world;

		if (world == null) {
			return original;
		}

		var dynamicRegistries = world.getRegistryManager();
		var dimensionEffectsRegistry = dynamicRegistries
			.getOptionalWrapper(LimlibRegistries.DimFx.REGISTRY_KEY)
			.orElseThrow(() -> new IllegalStateException("Client: Cannot find dimension effects registry (key: %s)".formatted(LimlibRegistries.DimFx.REGISTRY_KEY)));

		var effect = world.getRegistryKey().getValue();
		return LookupGrabber
			.snatch(dimensionEffectsRegistry,
				RegistryKey.of(LimlibRegistries.DimFx.REGISTRY_KEY, effect))
			.map(DimensionEffectsDto::skyShading)
			.orElse(original);
	}

}
