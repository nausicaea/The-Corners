package net.ludocrypt.limlib.client.impl.effects.sound.reverb;

import java.util.concurrent.atomic.AtomicInteger;

import net.ludocrypt.limlib.impl.effects.sound.SoundEffectsDto;
import net.ludocrypt.limlib.api.effects.sound.reverb.ReverbEffectDto;
import net.ludocrypt.limlib.client.api.effects.sound.reverb.ReverbEffect;
import net.ludocrypt.limlib.client.api.effects.sound.reverb.ReverbEffectFactories;
import net.ludocrypt.limlib.impl.Limlib;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.client.world.ClientWorld;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.EXTEfx;

import net.ludocrypt.limlib.client.impl.effects.LookupGrabber;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.MathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReverbFilter {

	public static final Logger LOGGER = LoggerFactory.getLogger("%s.%s".formatted(Limlib.LOGGER.getName(), ReverbFilter.class.getSimpleName()));

	private static final AtomicInteger id = new AtomicInteger(-1);
	private static final AtomicInteger slot = new AtomicInteger(-1);

	public static void reload() {
		id.set(EXTEfx.alGenEffects());
		slot.set(EXTEfx.alGenAuxiliaryEffectSlots());
	}

	private static boolean update(SoundInstance soundInstance, ReverbEffectDto dto) {
		id.compareAndSet(-1, EXTEfx.alGenEffects());
		slot.compareAndSet(-1, EXTEfx.alGenAuxiliaryEffectSlots());

		MinecraftClient client = MinecraftClient.getInstance();
		ReverbEffect data = ReverbEffectFactories.resolve(dto);

		if (data.isEnabled(client, soundInstance)) {
			int currentId = id.get();
			int currentSlot = slot.get();

			EXTEfx.alAuxiliaryEffectSlotf(currentSlot, EXTEfx.AL_EFFECTSLOT_GAIN, 0);
			EXTEfx.alEffecti(currentId, EXTEfx.AL_EFFECT_TYPE, EXTEfx.AL_EFFECT_REVERB);
			EXTEfx
				.alEffectf(currentId, EXTEfx.AL_REVERB_DENSITY,
					MathHelper
						.clamp(data.getDensity(client, soundInstance), EXTEfx.AL_REVERB_MIN_DENSITY,
							EXTEfx.AL_REVERB_MAX_DENSITY));
			EXTEfx
				.alEffectf(currentId, EXTEfx.AL_REVERB_DIFFUSION,
					MathHelper
						.clamp(data.getDiffusion(client, soundInstance), EXTEfx.AL_REVERB_MIN_DIFFUSION,
							EXTEfx.AL_REVERB_MAX_DIFFUSION));
			EXTEfx
				.alEffectf(currentId, EXTEfx.AL_REVERB_GAIN, MathHelper
					.clamp(data.getGain(client, soundInstance), EXTEfx.AL_REVERB_MIN_GAIN, EXTEfx.AL_REVERB_MAX_GAIN));
			EXTEfx
				.alEffectf(currentId, EXTEfx.AL_REVERB_GAINHF, MathHelper
					.clamp(data.getGainHF(client, soundInstance), EXTEfx.AL_REVERB_MIN_GAINHF, EXTEfx.AL_REVERB_MAX_GAINHF));
			EXTEfx
				.alEffectf(currentId, EXTEfx.AL_REVERB_DECAY_TIME,
					MathHelper
						.clamp(data.getDecayTime(client, soundInstance), EXTEfx.AL_REVERB_MIN_DECAY_TIME,
							EXTEfx.AL_REVERB_MAX_DECAY_TIME));
			EXTEfx
				.alEffectf(currentId, EXTEfx.AL_REVERB_DECAY_HFRATIO,
					MathHelper
						.clamp(data.getDecayHFRatio(client, soundInstance), EXTEfx.AL_REVERB_MIN_DECAY_HFRATIO,
							EXTEfx.AL_REVERB_MAX_DECAY_HFRATIO));
			EXTEfx
				.alEffectf(currentId, EXTEfx.AL_REVERB_REFLECTIONS_GAIN,
					MathHelper
						.clamp(data.getReflectionsGainBase(client, soundInstance), EXTEfx.AL_REVERB_MIN_REFLECTIONS_GAIN,
							EXTEfx.AL_REVERB_MAX_REFLECTIONS_GAIN));
			EXTEfx
				.alEffectf(currentId, EXTEfx.AL_REVERB_REFLECTIONS_DELAY,
					MathHelper
						.clamp(data.getReflectionsDelay(client, soundInstance), EXTEfx.AL_REVERB_MIN_REFLECTIONS_DELAY,
							EXTEfx.AL_REVERB_MAX_REFLECTIONS_DELAY));
			EXTEfx
				.alEffectf(currentId, EXTEfx.AL_REVERB_LATE_REVERB_GAIN,
					MathHelper
						.clamp(data.getLateReverbGainBase(client, soundInstance), EXTEfx.AL_REVERB_MIN_LATE_REVERB_GAIN,
							EXTEfx.AL_REVERB_MAX_LATE_REVERB_GAIN));
			EXTEfx
				.alEffectf(currentId, EXTEfx.AL_REVERB_LATE_REVERB_DELAY,
					MathHelper
						.clamp(data.getLateReverbDelay(client, soundInstance), EXTEfx.AL_REVERB_MIN_LATE_REVERB_DELAY,
							EXTEfx.AL_REVERB_MAX_LATE_REVERB_DELAY));
			EXTEfx
				.alEffectf(currentId, EXTEfx.AL_REVERB_AIR_ABSORPTION_GAINHF,
					MathHelper
						.clamp(data.getAirAbsorptionGainHF(client, soundInstance),
							EXTEfx.AL_REVERB_MIN_AIR_ABSORPTION_GAINHF, EXTEfx.AL_REVERB_MAX_AIR_ABSORPTION_GAINHF));
			EXTEfx
				.alEffectf(currentId, EXTEfx.AL_REVERB_ROOM_ROLLOFF_FACTOR,
					MathHelper
						.clamp(
							soundInstance.getAttenuationType() == SoundInstance.AttenuationType.LINEAR
									? 2.0F / (Math.max(soundInstance.getVolume(), 1.0F) + 2.0F)
									: 0.0F,
							EXTEfx.AL_REVERB_MIN_ROOM_ROLLOFF_FACTOR, EXTEfx.AL_REVERB_MAX_ROOM_ROLLOFF_FACTOR));
			EXTEfx
				.alEffecti(currentId, EXTEfx.AL_REVERB_DECAY_HFLIMIT,
					MathHelper
						.clamp(data.getDecayHFLimit(client, soundInstance), EXTEfx.AL_REVERB_MIN_DECAY_HFLIMIT,
							EXTEfx.AL_REVERB_MAX_DECAY_HFLIMIT));
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
			.flatMap(SoundEffectsDto::reverb)
			.filter(r -> !r.shouldIgnore(soundInstanceId))
			.ifPresent(r -> {
				for (int i = 0; i < 2; i++) {
					AL11.alSourcei(sourceID, EXTEfx.AL_DIRECT_FILTER, 0);
					AL11
						.alSource3i(sourceID, EXTEfx.AL_AUXILIARY_SEND_FILTER,
							update(soundInstance, r) ? slot.get() : 0, 0, 0);
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
