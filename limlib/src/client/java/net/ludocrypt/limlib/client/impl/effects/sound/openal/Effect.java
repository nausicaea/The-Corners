package net.ludocrypt.limlib.client.impl.effects.sound.openal;

import net.ludocrypt.limlib.impl.Limlib;
import org.lwjgl.openal.EXTEfx;

import java.util.Arrays;

public sealed abstract class Effect implements AutoCloseable permits Reverb {
	final int id;

	Effect(int id) {
		this.id = id;
	}

	public int id() { return id; }

	@Override
	public String toString() {
		return "%s(%d)".formatted(getClass().getSimpleName(), id);
	}

	public boolean isLoaded() {
		return EXTEfx.alIsEffect(id);
	}

	public EffectType getType() throws OpenAlException {
		int effectTypeId = EXTEfx.alGetEffecti(id, EffectProperty.EFFECT_TYPE.id());
		EffectsExtensionException.expect("%s: retrieving type".formatted(id));
		return EffectType.fromId(effectTypeId);
	}

	void setType(EffectType t) throws OpenAlException {
		// TODO(maybe this assertion is necessary): assert getType() == EffectType.None : "effect types cannot be changed";
		setUnchecked(EffectProperty.EFFECT_TYPE, t.id());
		EffectsExtensionException.expect("%s: assigning type %s(%d)".formatted(id, t.name(), t.id()));
	}

	public void setUnchecked(EffectProperty property, int value) {
		int propertyId = property.id();
		EXTEfx.alEffecti(id, propertyId, value);
	}

	public void setUnchecked(EffectProperty property, float value) {
		int propertyId = property.id();
		EXTEfx.alEffectf(id, propertyId, value);
	}

	@Override
	public void close() throws EffectsExtensionException {
		EXTEfx.alDeleteEffects(id);
		EffectsExtensionException.expect("Deleting the effect %s".formatted(this));
		Limlib.LOGGER.debug("Deleting the effect {}\n{}", this, Arrays.toString(Thread.currentThread().getStackTrace()));
	}
}
