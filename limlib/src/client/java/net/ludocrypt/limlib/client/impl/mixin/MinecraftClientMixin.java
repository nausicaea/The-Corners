package net.ludocrypt.limlib.client.impl.mixin;

import java.util.Optional;

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
import net.ludocrypt.limlib.client.api.effects.sound.SoundEffects;
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

		if (this.player != null) {
			Optional<SoundEffects> soundEffects = LookupGrabber
				.snatch(world.getRegistryManager().getOptionalWrapper(LimlibRegistries.SndFx.REGISTRY_KEY).get(),
					RegistryKey.of(LimlibRegistries.SndFx.REGISTRY_KEY, world.getRegistryKey().getValue()));

			if (soundEffects.isPresent()) {
				Optional<MusicSound> musicSound = soundEffects.get().getMusic();

				if (musicSound.isPresent()) {
					ci.setReturnValue(musicSound.get());
				}

			}

		}

	}

	@Inject(method = "onResolutionChanged", at = @At("RETURN"))
	private void limlib$onResolutionChanged(CallbackInfo info) {
		PostProcesserManager.INSTANCE
			.onResolutionChanged(this.window.getFramebufferWidth(), this.window.getFramebufferHeight());
	}

}
