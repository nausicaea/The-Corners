package net.ludocrypt.limlib.client.impl.skybox;

import net.ludocrypt.limlib.impl.skybox.EmptySkyboxDto;
import net.ludocrypt.limlib.client.api.skybox.Skybox;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;

public record EmptySkybox(EmptySkyboxDto dto) implements Skybox {
	@Override
	public void render(ClientWorld world, Camera camera, MatrixStack matrices, float tickDelta) {}
}
