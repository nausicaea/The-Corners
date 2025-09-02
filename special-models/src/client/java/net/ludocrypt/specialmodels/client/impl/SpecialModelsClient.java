package net.ludocrypt.specialmodels.client.impl;

import net.fabricmc.api.ClientModInitializer;
import net.ludocrypt.specialmodels.client.api.TexturedSpecialModelRenderer;

public class SpecialModelsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		TexturedSpecialModelRenderer.init();
	}
}
