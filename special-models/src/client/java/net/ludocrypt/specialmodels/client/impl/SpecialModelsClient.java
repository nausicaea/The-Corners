package net.ludocrypt.specialmodels.client.impl;

import net.fabricmc.api.ClientModInitializer;
import net.ludocrypt.specialmodels.client.api.TexturedSpecialModelRenderer;
import net.ludocrypt.specialmodels.impl.SpecialModels;

public class SpecialModelsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		SpecialModels.LOGGER.debug("Initializing special models client-side");
		ClientSharedMutableState.init();
		TexturedSpecialModelRenderer.init();
	}
}
