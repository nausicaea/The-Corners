package net.ludocrypt.limlib.client.impl;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;

import net.ludocrypt.limlib.impl.shader.PostProcesserManager;
import net.minecraft.resource.ResourceType;

public class LimlibClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(PostProcesserManager.INSTANCE);
	}

}
