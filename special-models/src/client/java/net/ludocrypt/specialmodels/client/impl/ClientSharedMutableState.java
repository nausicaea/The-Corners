package net.ludocrypt.specialmodels.client.impl;

import com.google.common.collect.Maps;
import net.ludocrypt.specialmodels.client.api.SpecialModelRenderer;
import net.ludocrypt.specialmodels.impl.SpecialModels;
import net.minecraft.client.gl.ShaderProgram;

import java.util.Map;

public class ClientSharedMutableState {
	public static final Map<SpecialModelRenderer, ShaderProgram> LOADED_SHADERS = Maps.newHashMap();

	public static void init() {
		SpecialModels.LOGGER.debug("Registering client-side shared mutable state");
	}
}
