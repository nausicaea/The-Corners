package net.ludocrypt.limlib.client.impl.effects.sound.openal;

import org.lwjgl.openal.EXTEfx;

public enum EffectType {
	EaxReverb(EXTEfx.AL_EFFECT_EAXREVERB),
	Reverb(EXTEfx.AL_EFFECT_REVERB),
	Chorus(EXTEfx.AL_EFFECT_CHORUS),
	Distortion(EXTEfx.AL_EFFECT_DISTORTION),
	Echo(EXTEfx.AL_EFFECT_ECHO),
	Flanger(EXTEfx.AL_EFFECT_FLANGER),
	FrequencyShifter(EXTEfx.AL_EFFECT_FREQUENCY_SHIFTER),
	VocalMorpher(EXTEfx.AL_EFFECT_VOCAL_MORPHER),
	PitchShifter(EXTEfx.AL_EFFECT_PITCH_SHIFTER),
	RingModulator(EXTEfx.AL_EFFECT_RING_MODULATOR),
	Autowah(EXTEfx.AL_EFFECT_AUTOWAH),
	Compressor(EXTEfx.AL_EFFECT_COMPRESSOR),
	Equalizer(EXTEfx.AL_EFFECT_EQUALIZER);

	public static EffectType fromId(int id) {
		for (EffectType type : EffectType.values()) {
			if (type.id == id) {
				return type;
			}
		}
		return null;
	}

	EffectType(int id) {
		this.id = id;
	}

	private final int id;

	public int id() { return id; }
}
