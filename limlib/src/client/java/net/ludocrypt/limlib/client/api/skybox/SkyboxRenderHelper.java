package net.ludocrypt.limlib.client.api.skybox;

import net.ludocrypt.limlib.api.effects.LookupGrabber;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;

public abstract class SkyboxRenderHelper {
	public static void render(ClientWorld world, Camera camera, MatrixStack matrices, float tickDelta) {
		var dynamicRegistries = world.getRegistryManager();
		var skyboxRegistry = dynamicRegistries
			.getOptionalWrapper(LimlibRegistries.Skyboxes.REGISTRY_KEY)
			.orElseThrow(() -> new IllegalStateException("Client: Cannot find skybox registry (key: %s)".formatted(LimlibRegistries.Skyboxes.REGISTRY_KEY)));

		var effect = world.getRegistryKey().getValue();
		LookupGrabber
			.snatch(skyboxRegistry,
				RegistryKey.of(LimlibRegistries.Skyboxes.REGISTRY_KEY, effect))
			.ifPresent(d -> {
				Skybox s = SkyboxFactories.resolve(d);
				s.render(world, camera, matrices, tickDelta);
			});
	}
}
