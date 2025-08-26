package net.ludocrypt.limlib.client.impl.mixin;

import net.ludocrypt.limlib.api.effects.sound.SoundEffectsDto;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.ludocrypt.limlib.api.effects.LookupGrabber;
import net.ludocrypt.limlib.client.impl.shader.PostProcesserManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.MusicSound;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow
	public ClientPlayerEntity player;

	@Shadow
	public ClientWorld world;

	@Final
	@Shadow
	private Window window;

	@Inject(method = "getMusicType()Lnet/minecraft/sound/MusicSound;", at = @At("HEAD"), cancellable = true)
	private void limlib$getMusic(CallbackInfoReturnable<MusicSound> ci) {
		if (player == null) {
			return;
		}

		var dynamicRegistries = world.getRegistryManager();
		var soundEffectsRegistry = dynamicRegistries
			.getOptionalWrapper(LimlibRegistries.SndFx.REGISTRY_KEY)
			.orElseThrow(() -> new IllegalStateException("Client: Cannot find sound effects registry (key: %s)".formatted(LimlibRegistries.SndFx.REGISTRY_KEY)));

		var effect = world.getRegistryKey().getValue();
		LookupGrabber.snatch(
			soundEffectsRegistry,
			RegistryKey.of(LimlibRegistries.SndFx.REGISTRY_KEY, effect)
		)
			.flatMap(SoundEffectsDto::music)
			.ifPresent(ci::setReturnValue);
	}

	@Inject(method = "onResolutionChanged", at = @At("RETURN"))
	private void limlib$onResolutionChanged(CallbackInfo info) {
		PostProcesserManager.INSTANCE
			.onResolutionChanged(this.window.getFramebufferWidth(), this.window.getFramebufferHeight());
	}

}
