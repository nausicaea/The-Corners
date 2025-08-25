package net.ludocrypt.limlib.client.api.skybox;

import org.joml.Matrix4f;

import com.mojang.serialization.Codec;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;

public abstract class Skybox {

	public abstract Codec<? extends Skybox> getCodec();

	public abstract void renderSky(WorldRenderer worldRenderer, MinecraftClient client, MatrixStack matrices,
			Matrix4f projectionMatrix, float tickDelta);

}
