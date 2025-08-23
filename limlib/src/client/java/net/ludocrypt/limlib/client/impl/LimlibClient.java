package net.ludocrypt.limlib.client.impl;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.ludocrypt.limlib.client.api.effects.sky.DimensionEffects;
import net.ludocrypt.limlib.client.api.effects.sky.EmptyDimensionEffects;
import net.ludocrypt.limlib.client.api.effects.sky.StaticDimensionEffects;
import net.ludocrypt.limlib.client.api.effects.sound.distortion.DistortionEffect;
import net.ludocrypt.limlib.client.api.effects.sound.distortion.StaticDistortionEffect;
import net.ludocrypt.limlib.client.api.effects.sound.reverb.ReverbEffect;
import net.ludocrypt.limlib.client.api.effects.sound.reverb.StaticReverbEffect;
import net.ludocrypt.limlib.client.api.skybox.EmptySkybox;
import net.ludocrypt.limlib.client.api.skybox.Skybox;
import net.ludocrypt.limlib.client.api.skybox.TexturedSkybox;
import net.ludocrypt.limlib.client.impl.shader.PostProcesserManager;
import net.minecraft.registry.Registry;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class LimlibClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Registry.register(ReverbEffect.REVERB_EFFECT_CODEC, new Identifier("limlib", "static"), StaticReverbEffect.CODEC);
		Registry
			.register(DistortionEffect.DISTORTION_EFFECT_CODEC, new Identifier("limlib", "static"),
				StaticDistortionEffect.CODEC);
		Registry
			.register(DimensionEffects.DIMENSION_EFFECTS_CODEC, new Identifier("limlib", "static"),
				StaticDimensionEffects.CODEC);
		Registry
			.register(DimensionEffects.DIMENSION_EFFECTS_CODEC, new Identifier("limlib", "empty"),
				EmptyDimensionEffects.CODEC);
		Registry.register(Skybox.SKYBOX_CODEC, new Identifier("limlib", "empty"), EmptySkybox.CODEC);
		Registry.register(Skybox.SKYBOX_CODEC, new Identifier("limlib", "textured"), TexturedSkybox.CODEC);
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(PostProcesserManager.INSTANCE);
	}
}
