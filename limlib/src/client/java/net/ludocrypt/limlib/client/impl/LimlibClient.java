package net.ludocrypt.limlib.client.impl;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;

import net.ludocrypt.limlib.impl.effects.sky.EmptyDimensionEffectsDto;
import net.ludocrypt.limlib.impl.effects.sky.StaticDimensionEffectsDto;
import net.ludocrypt.limlib.impl.effects.sound.distortion.StaticDistortionEffectDto;
import net.ludocrypt.limlib.impl.effects.sound.reverb.StaticReverbEffectDto;
import net.ludocrypt.limlib.impl.skybox.EmptySkyboxDto;
import net.ludocrypt.limlib.impl.skybox.TexturedSkyboxDto;
import net.ludocrypt.limlib.client.api.effects.sky.DimensionEffectsFactories;
import net.ludocrypt.limlib.client.impl.effects.sky.DimensionEffectsHelper;
import net.ludocrypt.limlib.client.api.effects.sound.distortion.DistortionEffectFactories;
import net.ludocrypt.limlib.client.impl.effects.sound.distortion.StaticDistortionEffect;
import net.ludocrypt.limlib.client.api.effects.sound.reverb.ReverbEffectFactories;
import net.ludocrypt.limlib.client.impl.effects.sound.reverb.StaticReverbEffect;
import net.ludocrypt.limlib.client.impl.skybox.EmptySkybox;
import net.ludocrypt.limlib.client.api.skybox.SkyboxFactories;
import net.ludocrypt.limlib.client.impl.skybox.TexturedSkybox;
import net.ludocrypt.limlib.client.impl.shader.PostProcesserManager;
import net.minecraft.resource.ResourceType;

public class LimlibClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(PostProcesserManager.INSTANCE);
		DistortionEffectFactories.register(StaticDistortionEffectDto.ID, dto -> new StaticDistortionEffect((StaticDistortionEffectDto) dto));
		ReverbEffectFactories.register(StaticReverbEffectDto.ID, dto -> new StaticReverbEffect((StaticReverbEffectDto) dto));
		SkyboxFactories.register(EmptySkyboxDto.ID, dto -> new EmptySkybox((EmptySkyboxDto) dto));
		SkyboxFactories.register(TexturedSkyboxDto.ID, dto -> new TexturedSkybox((TexturedSkyboxDto) dto));
		DimensionEffectsFactories.register(EmptyDimensionEffectsDto.ID, DimensionEffectsHelper::createDefault);
		DimensionEffectsFactories.register(StaticDimensionEffectsDto.ID, DimensionEffectsHelper::createDefault);
	}

}
