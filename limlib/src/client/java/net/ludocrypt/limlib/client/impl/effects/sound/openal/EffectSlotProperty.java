package net.ludocrypt.limlib.client.impl.effects.sound.openal;

import org.lwjgl.openal.EXTEfx;

public final class EffectSlotProperty extends PropertyImpl {
	public static final EffectSlotProperty GAIN = new EffectSlotProperty("EXTEfx.AL_EFFECTSLOT_GAIN", EXTEfx.AL_EFFECTSLOT_GAIN);
	public static final EffectSlotProperty EFFECT = new EffectSlotProperty("EXTEfx.AL_EFFECTSLOT_EFFECT", EXTEfx.AL_EFFECTSLOT_EFFECT);

	private EffectSlotProperty(String name, int id) {
		super(name, id);
	}
}
