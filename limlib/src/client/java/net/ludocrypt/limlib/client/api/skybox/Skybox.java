package net.ludocrypt.limlib.client.api.skybox;

import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;

public interface Skybox {
	void render(ClientWorld world, Camera camera, MatrixStack matrices, float tickDelta);
}
