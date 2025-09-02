package net.ludocrypt.specialmodels.client.impl;

import com.google.common.collect.Maps;
import net.fabricmc.api.ClientModInitializer;
import net.ludocrypt.specialmodels.client.api.SpecialModelRenderer;
import net.ludocrypt.specialmodels.client.api.TexturedSpecialModelRenderer;
import net.minecraft.client.gl.ShaderProgram;

import java.util.Map;

public class SpecialModelsClient implements ClientModInitializer {
	public static final Map<SpecialModelRenderer, ShaderProgram> LOADED_SHADERS = Maps.newHashMap();

	@Override
	public void onInitializeClient() {
		TexturedSpecialModelRenderer.init();
	}
}
