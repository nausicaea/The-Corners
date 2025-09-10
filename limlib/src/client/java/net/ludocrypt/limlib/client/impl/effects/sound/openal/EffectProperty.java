package net.ludocrypt.limlib.client.impl.effects.sound.openal;

import org.lwjgl.openal.EXTEfx;

public final class EffectProperty extends PropertyImpl {
	public static final EffectProperty EFFECT_TYPE = new EffectProperty("EXTEfx.AL_EFFECT_TYPE", EXTEfx.AL_EFFECT_TYPE);
	public static final EffectProperty REVERB_DENSITY = new EffectProperty("EXTEfx.AL_REVERB_DENSITY", EXTEfx.AL_REVERB_DENSITY);
	public static final EffectProperty REVERB_DIFFUSION = new EffectProperty("EXTEfx.AL_REVERB_DIFFUSION", EXTEfx.AL_REVERB_DIFFUSION);
	public static final EffectProperty REVERB_GAIN = new EffectProperty("EXTEfx.AL_REVERB_GAIN", EXTEfx.AL_REVERB_GAIN);
	public static final EffectProperty REVERB_GAINHF = new EffectProperty("EXTEfx.AL_REVERB_GAINHF", EXTEfx.AL_REVERB_GAINHF);
	public static final EffectProperty REVERB_DECAY_TIME = new EffectProperty("EXTEfx.AL_REVERB_DECAY_TIME", EXTEfx.AL_REVERB_DECAY_TIME);
	public static final EffectProperty REVERB_DECAY_HFRATIO = new EffectProperty("EXTEfx.AL_REVERB_DECAY_HFRATIO", EXTEfx.AL_REVERB_DECAY_HFRATIO);
	public static final EffectProperty REVERB_REFLECTIONS_GAIN = new EffectProperty("EXTEfx.AL_REVERB_REFLECTIONS_GAIN", EXTEfx.AL_REVERB_REFLECTIONS_GAIN);
	public static final EffectProperty REVERB_REFLECTIONS_DELAY = new EffectProperty("EXTEfx.AL_REVERB_REFLECTIONS_DELAY", EXTEfx.AL_REVERB_REFLECTIONS_DELAY);
	public static final EffectProperty REVERB_LATE_REVERB_GAIN = new EffectProperty("EXTEfx.AL_REVERB_LATE_REVERB_GAIN", EXTEfx.AL_REVERB_LATE_REVERB_GAIN);
	public static final EffectProperty REVERB_LATE_REVERB_DELAY = new EffectProperty("EXTEfx.AL_REVERB_LATE_REVERB_DELAY", EXTEfx.AL_REVERB_LATE_REVERB_DELAY);
	public static final EffectProperty REVERB_AIR_ABSORPTION_GAINHF = new EffectProperty("EXTEfx.AL_REVERB_AIR_ABSORPTION_GAINHF", EXTEfx.AL_REVERB_AIR_ABSORPTION_GAINHF);
	public static final EffectProperty REVERB_ROOM_ROLLOFF_FACTOR = new EffectProperty("EXTEfx.AL_REVERB_ROOM_ROLLOFF_FACTOR", EXTEfx.AL_REVERB_ROOM_ROLLOFF_FACTOR);
	public static final EffectProperty REVERB_DECAY_HFLIMIT = new EffectProperty("EXTEfx.AL_REVERB_DECAY_HFLIMIT", EXTEfx.AL_REVERB_DECAY_HFLIMIT);

	private EffectProperty(String name, int id) {
		super(name, id);
	}
}
