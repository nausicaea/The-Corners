package net.ludocrypt.limlib.client.impl;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;

import net.ludocrypt.limlib.client.api.effects.sound.distortion.DistortionEffect;
import net.ludocrypt.limlib.client.api.effects.sound.distortion.StaticDistortionEffect;
import net.ludocrypt.limlib.client.impl.shader.PostProcesserManager;
import net.minecraft.registry.Registry;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class LimlibClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(PostProcesserManager.INSTANCE);
		Registry
			.register(DistortionEffect.DISTORTION_EFFECT_CODEC, new Identifier("limlib", "static"),
				StaticDistortionEffect.CODEC);
	}

}
