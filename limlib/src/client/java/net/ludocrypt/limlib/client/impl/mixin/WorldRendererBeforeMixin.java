package net.ludocrypt.limlib.client.impl.mixin;

import net.ludocrypt.limlib.client.impl.skybox.SkyboxRenderHelper;
import net.minecraft.client.world.ClientWorld;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.ludocrypt.limlib.impl.bridge.IrisBridge;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(value = WorldRenderer.class, priority = 950)
public abstract class WorldRendererBeforeMixin {
	@Shadow
	private ClientWorld world;

	@Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lorg/joml/Matrix4f;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clear(IZ)V", ordinal = 0, shift = At.Shift.AFTER, remap = false))
	private void limlib$render$clear(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline,
			Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix,
			CallbackInfo ci) {

		if (IrisBridge.IRIS_LOADED && IrisBridge.areShadersInUse()) {
			return;
		}

		SkyboxRenderHelper.render(world, camera, matrices, tickDelta);
	}

}
