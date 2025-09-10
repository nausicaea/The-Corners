package net.ludocrypt.limlib.client.impl.effects.sound.openal;

import org.lwjgl.openal.EXTEfx;

public record Effect(int id) implements AutoCloseable {
	public static Effect generate() throws OpenAlException {
		int effectId = EXTEfx.alGenEffects();
		EffectsExtensionException.expect("Generating a new effect");
		if (effectId < 0) {
			throw new EffectsExtensionException("Invalid effect ID: %d".formatted(effectId));
		}
		if (!EXTEfx.alIsEffect(effectId)) {
			throw new EffectsExtensionException("Effect ID %d is not an effect!?".formatted(effectId));
		}
		return new Effect(effectId);
	}

	public boolean isLoaded() {
		return EXTEfx.alIsEffect(id);
	}

	public EffectType getType() throws OpenAlException {
		int effectTypeId = EXTEfx.alGetEffecti(id, EffectProperty.EFFECT_TYPE.id());
		EffectsExtensionException.expect("Effect %s: retrieving type".formatted(id));
		return EffectType.fromId(effectTypeId);
	}

	public void setType(EffectType t) throws OpenAlException {
		EXTEfx.alEffecti(id, EffectProperty.EFFECT_TYPE.id(), t.id());
		EffectsExtensionException.expect("Effect %s: assigning type %s(%d)".formatted(id, t.name(), t.id()));
	}

	public void setUnchecked(EffectProperty property, int value) throws OpenAlException {
		int propertyId = property.id();
		EXTEfx.alEffecti(id, propertyId, value);
		EffectsExtensionException.expect("Effect %s: error setting effect property %s=%d".formatted(id, property, value));
	}

	public void setUnchecked(EffectProperty property, float value) throws OpenAlException {
		int propertyId = property.id();
		EXTEfx.alEffectf(id, propertyId, value);
		EffectsExtensionException.expect("Effect %s: error setting effect property %s=%f".formatted(id, property, value));
	}

	@Override
	public void close() {
		EXTEfx.alDeleteEffects(id);
	}
}
