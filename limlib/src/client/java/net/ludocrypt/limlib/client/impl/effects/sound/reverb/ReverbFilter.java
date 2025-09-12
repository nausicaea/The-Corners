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

	private static final ThreadLocal<Reverb> effect = new ThreadLocal<>();
	private static final ThreadLocal<EffectSlot> effectSlot = new ThreadLocal<>();

	private static Reverb reloadEffect() throws OpenAlException {
		var gammel = effect.get();
		var ny = Reverb.generate();
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

		Reverb cfx = effect.get();
		if (cfx == null) {
			cfx = reloadEffect();
		}
		if (!cfx.isLoaded()) {
			Limlib.LOGGER.warn("Effect {} not loaded", cfx);
			return false;
		}
		cfx.setDensity(data.getDensity(client, soundInstance));
		cfx.setDiffusion(data.getDiffusion(client, soundInstance));
		cfx.setGain(data.getGain(client, soundInstance));
		cfx.setGainHF(data.getGainHF(client, soundInstance));
		cfx.setDecayTime(data.getDecayTime(client, soundInstance));
		cfx.setDecayHFRatio(data.getDecayHFRatio(client, soundInstance));
		cfx.setReflectionsGain(data.getReflectionsGainBase(client, soundInstance));
		cfx.setReflectionsDelay(data.getReflectionsDelay(client, soundInstance));
		cfx.setLateReverbGain(data.getLateReverbGainBase(client, soundInstance));
		cfx.setLateReverbDelay(data.getLateReverbDelay(client, soundInstance));
		cfx.setAirAbsorptionGainHF(data.getAirAbsorptionGainHF(client, soundInstance));
		cfx.setRoomRolloffFactor(soundInstance.getAttenuationType() == SoundInstance.AttenuationType.LINEAR ? 2.0F / (Math.max(soundInstance.getVolume(), 1.0F) + 2.0F) : 0.0F);
		cfx.setDecayHFLimit(data.getDecayHFLimit(client, soundInstance));

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
