package net.ludocrypt.limlib.client.api.effects.sound.distortion;

import java.util.concurrent.atomic.AtomicInteger;

import net.ludocrypt.limlib.api.effects.sound.SoundEffectsDto;
import net.ludocrypt.limlib.api.effects.sound.distortion.DistortionEffectDto;
import net.ludocrypt.limlib.impl.Limlib;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.client.world.ClientWorld;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.EXTEfx;

import net.ludocrypt.limlib.api.effects.LookupGrabber;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.MathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistortionFilter {

	public static final Logger LOGGER = LoggerFactory.getLogger("%s.%s".formatted(Limlib.LOGGER.getName(), DistortionFilter.class.getSimpleName()));

	private static final AtomicInteger id = new AtomicInteger(-1);
	private static final AtomicInteger slot = new AtomicInteger(-1);

	public static void reload() {
		id.set(EXTEfx.alGenEffects());
		slot.set(EXTEfx.alGenAuxiliaryEffectSlots());
	}

	private static boolean update(SoundInstance soundInstance, DistortionEffectDto dto) {
		id.compareAndSet(-1, EXTEfx.alGenEffects());
		slot.compareAndSet(-1, EXTEfx.alGenAuxiliaryEffectSlots());

		MinecraftClient client = MinecraftClient.getInstance();
		DistortionEffect data = DistortionEffectFactories.resolve(dto);

		if (data.isEnabled(client, soundInstance)) {
			int currentId = id.get();
			int currentSlot = slot.get();
			EXTEfx.alAuxiliaryEffectSlotf(currentSlot, EXTEfx.AL_EFFECTSLOT_GAIN, 0);
			EXTEfx.alEffecti(currentId, EXTEfx.AL_EFFECT_TYPE, EXTEfx.AL_EFFECT_DISTORTION);
			EXTEfx
				.alEffectf(currentId, EXTEfx.AL_DISTORTION_EDGE,
					MathHelper
						.clamp(data.getEdge(client, soundInstance), EXTEfx.AL_DISTORTION_MIN_EDGE,
							EXTEfx.AL_DISTORTION_MAX_EDGE));
			EXTEfx
				.alEffectf(currentId, EXTEfx.AL_DISTORTION_GAIN,
					MathHelper
						.clamp(data.getGain(client, soundInstance), EXTEfx.AL_DISTORTION_MIN_GAIN,
							EXTEfx.AL_DISTORTION_MAX_GAIN));
			EXTEfx
				.alEffectf(currentId, EXTEfx.AL_DISTORTION_LOWPASS_CUTOFF,
					MathHelper
						.clamp(data.getLowpassCutoff(client, soundInstance), EXTEfx.AL_DISTORTION_MIN_LOWPASS_CUTOFF,
							EXTEfx.AL_DISTORTION_MAX_LOWPASS_CUTOFF));
			EXTEfx
				.alEffectf(currentId, EXTEfx.AL_DISTORTION_EQCENTER,
					MathHelper
						.clamp(data.getEQCenter(client, soundInstance), EXTEfx.AL_DISTORTION_MIN_EQCENTER,
							EXTEfx.AL_DISTORTION_MAX_EQCENTER));
			EXTEfx
				.alEffectf(currentId, EXTEfx.AL_DISTORTION_EQBANDWIDTH,
					MathHelper
						.clamp(data.getEQBandWidth(client, soundInstance), EXTEfx.AL_DISTORTION_MIN_EQBANDWIDTH,
							EXTEfx.AL_DISTORTION_MAX_EQBANDWIDTH));
			EXTEfx.alAuxiliaryEffectSloti(currentSlot, EXTEfx.AL_EFFECTSLOT_EFFECT, currentId);
			EXTEfx.alAuxiliaryEffectSlotf(currentSlot, EXTEfx.AL_EFFECTSLOT_GAIN, 1);

			return true;
		}

		return false;
	}

	public static void update(SoundInstance soundInstance, int sourceID) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client == null) {
			return;
		}
		ClientWorld world = client.world;
		if (world == null) {
			return;
		}

		var dynamicRegistries = world.getRegistryManager();
		var soundEffectsRegistry = dynamicRegistries
			.getOptionalWrapper(LimlibRegistries.SndFx.REGISTRY_KEY)
			.orElseThrow(() -> new IllegalStateException("Client: Cannot find sound effects registry (key: %s)".formatted(LimlibRegistries.SndFx.REGISTRY_KEY)));

		var soundInstanceId = soundInstance.getId();
		var effect = world.getRegistryKey().getValue();
		LookupGrabber
			.snatch(soundEffectsRegistry,
				RegistryKey.of(LimlibRegistries.SndFx.REGISTRY_KEY, effect))
			.flatMap(SoundEffectsDto::distortion)
			.filter(d -> !d.shouldIgnore(soundInstanceId))
			.ifPresent(d -> {
				for (int i = 0; i < 2; i++) {
					AL11.alSourcei(sourceID, EXTEfx.AL_DIRECT_FILTER, 0);
					AL11
						.alSource3i(sourceID, EXTEfx.AL_AUXILIARY_SEND_FILTER,
							update(soundInstance, d) ? slot.get() : 0, 0, 0);
					int error = AL11.alGetError();

					if (error == AL11.AL_NO_ERROR) {
						break;
					} else {
						LOGGER.warn("OpenAl Error {}", error);
					}

				}
			});
	}

}
