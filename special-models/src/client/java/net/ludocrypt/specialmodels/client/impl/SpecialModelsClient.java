package net.ludocrypt.specialmodels.client.impl;

import com.google.common.collect.Maps;
import net.ludocrypt.specialmodels.api.SpecialModelRenderer;
import net.ludocrypt.specialmodels.client.api.TexturedSpecialModelRenderer;
import net.minecraft.client.render.ShaderProgram;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

import java.util.Map;

public class SpecialModelsClient implements ClientModInitializer {
	public static final Map<SpecialModelRenderer, ShaderProgram> LOADED_SHADERS = Maps.newHashMap();

	@Override
	public void onInitializeClient(ModContainer mod) {
		TexturedSpecialModelRenderer.init();
	}
}
