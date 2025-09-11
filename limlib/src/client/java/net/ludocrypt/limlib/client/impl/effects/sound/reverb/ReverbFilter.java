package net.ludocrypt.limlib.client.impl.effects.sound.reverb;

import net.ludocrypt.limlib.client.impl.effects.sound.openal.*;
import net.ludocrypt.limlib.impl.effects.sound.SoundEffectsDto;
import net.ludocrypt.limlib.api.effects.sound.reverb.ReverbEffectDto;
import net.ludocrypt.limlib.client.api.effects.sound.reverb.ReverbEffect;
import net.ludocrypt.limlib.client.api.effects.sound.reverb.ReverbEffectFactories;
import net.ludocrypt.limlib.impl.Limlib;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.client.world.ClientWorld;
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

	private static final ThreadLocal<Effect> effect = new ThreadLocal<>();
	private static final ThreadLocal<EffectSlot> effectSlot = new ThreadLocal<>();

	private static Effect reloadEffect() throws OpenAlException {
		var gammel = effect.get();
		var ny = Effect.generate();
		effect.set(ny);
		if (gammel != null) {
			gammel.close();
		}
		return ny;
	}

	private static EffectSlot reloadEffectSlot() throws OpenAlException {
		var gammel = effectSlot.get();
		var ny = EffectSlot.generate();
		effectSlot.set(ny);
		if (gammel != null) {
			gammel.close();
		}
		return ny;
	}

	public static void reload() {
		try {
			reloadEffect();
			reloadEffectSlot();
		} catch (OpenAlException e) {
			throw new RuntimeException("Cannot reload reverb filter", e);
		}
	}

	private static boolean updateEffect(SoundInstance soundInstance, ReverbEffectDto dto) throws OpenAlException {

		MinecraftClient client = MinecraftClient.getInstance();
		ReverbEffect data = ReverbEffectFactories.resolve(dto);

		if (!data.isEnabled(client, soundInstance)) {
			return false;
		}

		Effect cfx = effect.get();
		if (cfx == null) {
			cfx = reloadEffect();
		}
		if (!cfx.isLoaded()) {
			Limlib.LOGGER.warn("Effect {} not loaded", cfx);
			return false;
		}
		cfx.setType(EffectType.Reverb);
		cfx.setUnchecked(EffectProperty.REVERB_DENSITY,
			MathHelper
				.clamp(data.getDensity(client, soundInstance), EXTEfx.AL_REVERB_MIN_DENSITY,
					EXTEfx.AL_REVERB_MAX_DENSITY)
		);
		cfx.setUnchecked(EffectProperty.REVERB_DIFFUSION,
			MathHelper
				.clamp(data.getDiffusion(client, soundInstance), EXTEfx.AL_REVERB_MIN_DIFFUSION,
					EXTEfx.AL_REVERB_MAX_DIFFUSION)
		);
		cfx.setUnchecked(EffectProperty.REVERB_GAIN, MathHelper
			.clamp(data.getGain(client, soundInstance), EXTEfx.AL_REVERB_MIN_GAIN, EXTEfx.AL_REVERB_MAX_GAIN));
		cfx.setUnchecked(EffectProperty.REVERB_GAINHF, MathHelper
			.clamp(data.getGainHF(client, soundInstance), EXTEfx.AL_REVERB_MIN_GAINHF, EXTEfx.AL_REVERB_MAX_GAINHF));
		cfx.setUnchecked(EffectProperty.REVERB_DECAY_TIME, MathHelper
			.clamp(data.getDecayTime(client, soundInstance), EXTEfx.AL_REVERB_MIN_DECAY_TIME,
				EXTEfx.AL_REVERB_MAX_DECAY_TIME));
		cfx.setUnchecked(EffectProperty.REVERB_DECAY_HFRATIO, MathHelper
			.clamp(data.getDecayHFRatio(client, soundInstance), EXTEfx.AL_REVERB_MIN_DECAY_HFRATIO,
				EXTEfx.AL_REVERB_MAX_DECAY_HFRATIO));
		cfx.setUnchecked(EffectProperty.REVERB_REFLECTIONS_GAIN, MathHelper
			.clamp(data.getReflectionsGainBase(client, soundInstance), EXTEfx.AL_REVERB_MIN_REFLECTIONS_GAIN,
				EXTEfx.AL_REVERB_MAX_REFLECTIONS_GAIN));
		cfx.setUnchecked(EffectProperty.REVERB_REFLECTIONS_DELAY, MathHelper
			.clamp(data.getReflectionsDelay(client, soundInstance), EXTEfx.AL_REVERB_MIN_REFLECTIONS_DELAY,
				EXTEfx.AL_REVERB_MAX_REFLECTIONS_DELAY));
		cfx.setUnchecked(EffectProperty.REVERB_LATE_REVERB_GAIN, MathHelper
			.clamp(data.getLateReverbGainBase(client, soundInstance), EXTEfx.AL_REVERB_MIN_LATE_REVERB_GAIN,
				EXTEfx.AL_REVERB_MAX_LATE_REVERB_GAIN));
		cfx.setUnchecked(EffectProperty.REVERB_LATE_REVERB_DELAY,
				MathHelper
					.clamp(data.getLateReverbDelay(client, soundInstance), EXTEfx.AL_REVERB_MIN_LATE_REVERB_DELAY,
						EXTEfx.AL_REVERB_MAX_LATE_REVERB_DELAY));
		cfx.setUnchecked(EffectProperty.REVERB_AIR_ABSORPTION_GAINHF,
				MathHelper
					.clamp(data.getAirAbsorptionGainHF(client, soundInstance),
						EXTEfx.AL_REVERB_MIN_AIR_ABSORPTION_GAINHF, EXTEfx.AL_REVERB_MAX_AIR_ABSORPTION_GAINHF));
		cfx.setUnchecked(EffectProperty.REVERB_ROOM_ROLLOFF_FACTOR,
				MathHelper
					.clamp(
						soundInstance.getAttenuationType() == SoundInstance.AttenuationType.LINEAR
								? 2.0F / (Math.max(soundInstance.getVolume(), 1.0F) + 2.0F)
								: 0.0F,
						EXTEfx.AL_REVERB_MIN_ROOM_ROLLOFF_FACTOR, EXTEfx.AL_REVERB_MAX_ROOM_ROLLOFF_FACTOR));
		cfx.setUnchecked(EffectProperty.REVERB_DECAY_HFLIMIT,
				MathHelper
					.clamp(data.getDecayHFLimit(client, soundInstance), EXTEfx.AL_REVERB_MIN_DECAY_HFLIMIT,
						EXTEfx.AL_REVERB_MAX_DECAY_HFLIMIT));

		EffectSlot cslot = effectSlot.get();
		if (cslot == null) {
			cslot = reloadEffectSlot();
		}
		if (!cslot.isLoaded()) {
			Limlib.LOGGER.warn("Auxiliary effects slot {} not loaded", cslot);
			return false;
		}
		cslot.setGain(0);
		cslot.setEffect(cfx);
		cslot.setGain(1);

		return true;

	}

	public static void update(SoundInstance soundInstance, Source source) {
		if (!source.isLoaded()) {
			Limlib.LOGGER.warn("Source {} is not loaded", source);
			return;
		}
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
			.orElseThrow(() -> new IllegalStateException("Cannot find sound effects registry (key: %s)".formatted(LimlibRegistries.SndFx.REGISTRY_KEY)));

		var soundInstanceId = soundInstance.getId();
		var effect = world.getRegistryKey().getValue();
		LookupGrabber
			.snatch(soundEffectsRegistry,
				RegistryKey.of(LimlibRegistries.SndFx.REGISTRY_KEY, effect))
			.flatMap(SoundEffectsDto::reverb)
			.filter(r -> !r.shouldIgnore(soundInstanceId))
			.ifPresent(r -> {
				try {
					if (updateEffect(soundInstance, r)) {
						source.setDirectFilter(null);
						source.setAuxiliarySendFilter(effectSlot.get(), 0, null);
					}
				} catch (OpenAlException e) {
					throw new RuntimeException("Cannot update sound for source=%s, effect=%s, effectSlot=%s".formatted(source, r, effectSlot.get()), e);
				}
			});

	}

}
