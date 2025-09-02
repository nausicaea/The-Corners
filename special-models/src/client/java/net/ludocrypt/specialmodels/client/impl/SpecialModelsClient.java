package net.ludocrypt.specialmodels.client.impl;

import net.fabricmc.api.ClientModInitializer;
import net.ludocrypt.specialmodels.api.TexturedSpecialModelRendererDto;
import net.ludocrypt.specialmodels.client.api.SpecialModelRendererFactories;
import net.ludocrypt.specialmodels.client.api.TexturedSpecialModelRenderer;

public class SpecialModelsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientSharedMutableState.init();
		SpecialModelRendererFactories.register(TexturedSpecialModelRendererDto.ID, dto -> new TexturedSpecialModelRenderer(dto));
	}
}
