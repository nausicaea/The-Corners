package net.ludocrypt.limlib.client.impl.effects.sound.openal;

import net.ludocrypt.limlib.impl.Limlib;
import org.lwjgl.openal.EXTEfx;

public final class Reverb extends Effect {
	Reverb(int id) {
		super(id);
	}

	public static Reverb generate() throws OpenAlException {
		Limlib.LOGGER.debug("Generating a new reverb effect");
		int effectId = EXTEfx.alGenEffects();
		EffectsExtensionException.expect("Generating a new effect");
		if (effectId < 0) {
			throw new EffectsExtensionException("Invalid effect ID: %d".formatted(effectId));
		}
		if (!EXTEfx.alIsEffect(effectId)) {
			throw new EffectsExtensionException("Effect ID %d is not an effect!?".formatted(effectId));
		}
		var fx = new Reverb(effectId);
		fx.setType(EffectType.Reverb);
		return fx;
	}

	public void setDensity(float density) throws OpenAlException {
		setClampedFloat(EffectProperty.REVERB_DENSITY, density, EXTEfx.AL_REVERB_MIN_DENSITY, EXTEfx.AL_REVERB_MAX_DENSITY);
	}

	public void setDiffusion(float diffusion) throws OpenAlException {
		setClampedFloat(EffectProperty.REVERB_DIFFUSION, diffusion, EXTEfx.AL_REVERB_MIN_DIFFUSION, EXTEfx.AL_REVERB_MAX_DIFFUSION);
	}

	public void setGain(float gain) throws OpenAlException {
		setClampedFloat(EffectProperty.REVERB_GAIN, gain, EXTEfx.AL_REVERB_MIN_GAIN, EXTEfx.AL_REVERB_MAX_GAIN);
	}

	public void setGainHF(float gain) throws OpenAlException {
		setClampedFloat(EffectProperty.REVERB_GAINHF, gain, EXTEfx.AL_REVERB_MIN_GAINHF, EXTEfx.AL_REVERB_MAX_GAINHF);
	}

	public void setDecayTime(float decayTime) throws OpenAlException {
		setClampedFloat(EffectProperty.REVERB_DECAY_TIME, decayTime, EXTEfx.AL_REVERB_MIN_DECAY_TIME, EXTEfx.AL_REVERB_MAX_DECAY_TIME);
	}

	public void setDecayHFRatio(float decayHFRatio) throws OpenAlException {
		setClampedFloat(EffectProperty.REVERB_DECAY_HFRATIO, decayHFRatio, EXTEfx.AL_REVERB_MIN_DECAY_HFRATIO, EXTEfx.AL_REVERB_MAX_DECAY_HFRATIO);
	}

	public void setReflectionsGain(float reflectionsGain) throws OpenAlException {
		setClampedFloat(EffectProperty.REVERB_REFLECTIONS_GAIN, reflectionsGain, EXTEfx.AL_REVERB_MIN_REFLECTIONS_GAIN,  EXTEfx.AL_REVERB_MAX_REFLECTIONS_GAIN);
	}

	public void setReflectionsDelay(float reflectionsDelay) throws OpenAlException {
		setClampedFloat(EffectProperty.REVERB_REFLECTIONS_DELAY, reflectionsDelay, EXTEfx.AL_REVERB_MIN_REFLECTIONS_DELAY,  EXTEfx.AL_REVERB_MAX_REFLECTIONS_DELAY);
	}

	public void setLateReverbGain(float lateReverbGain) throws OpenAlException {
		setClampedFloat(EffectProperty.REVERB_LATE_REVERB_GAIN, lateReverbGain, EXTEfx.AL_REVERB_MIN_LATE_REVERB_GAIN, EXTEfx.AL_REVERB_MAX_LATE_REVERB_GAIN);
	}

	public void setLateReverbDelay(float lateReverbDelay) throws OpenAlException {
		setClampedFloat(EffectProperty.REVERB_LATE_REVERB_DELAY, lateReverbDelay, EXTEfx.AL_REVERB_MIN_LATE_REVERB_DELAY, EXTEfx.AL_REVERB_MAX_LATE_REVERB_DELAY);
	}

	public void setAirAbsorptionGainHF(float airAbsorptionGainHF) throws OpenAlException {
		setClampedFloat(EffectProperty.REVERB_AIR_ABSORPTION_GAINHF, airAbsorptionGainHF, EXTEfx.AL_REVERB_MIN_AIR_ABSORPTION_GAINHF, EXTEfx.AL_REVERB_MAX_AIR_ABSORPTION_GAINHF);
	}

	public void setRoomRolloffFactor(float roomRolloffFactor) throws OpenAlException {
		setClampedFloat(EffectProperty.REVERB_ROOM_ROLLOFF_FACTOR, roomRolloffFactor, EXTEfx.AL_REVERB_MIN_ROOM_ROLLOFF_FACTOR, EXTEfx.AL_REVERB_MAX_ROOM_ROLLOFF_FACTOR);
	}

	public void setDecayHFLimit(boolean decayHFLimit) throws OpenAlException {
		setUnchecked(EffectProperty.REVERB_DECAY_HFLIMIT, decayHFLimit ? 1 : 0);
		EffectsExtensionException.expect("Reverb effect %s: setting decay high-frequency limiting to %b".formatted(id, decayHFLimit));
	}

	private void setClampedFloat(EffectProperty property, float value, float min, float max) throws OpenAlException {
		assert getType() == EffectType.Reverb;
		var clamped = Math.clamp(value, min, max);
		setUnchecked(property, clamped);
		EffectsExtensionException.expect("Reverb effect %s: setting property %s to %f (clamped to %f)".formatted(id, property, value, clamped));
	}
}
