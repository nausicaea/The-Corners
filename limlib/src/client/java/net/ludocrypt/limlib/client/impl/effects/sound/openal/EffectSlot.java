package net.ludocrypt.limlib.client.impl.effects.sound.openal;

import org.lwjgl.openal.EXTEfx;

public record EffectSlot(int id) implements AutoCloseable {
	public static EffectSlot generate() throws OpenAlException {
		int slotId = EXTEfx.alGenAuxiliaryEffectSlots();
		EffectsExtensionException.expect("Generating a new auxiliary effect slot");
		if (slotId < 0) {
			throw new EffectsExtensionException("Invalid slot ID: %d".formatted(slotId));
		}
		if (!EXTEfx.alIsAuxiliaryEffectSlot(slotId)) {
			throw new EffectsExtensionException("Slot ID %d is not an auxiliary effect slot!?".formatted(slotId));
		}
		return new EffectSlot(slotId);
	}

	public boolean isLoaded() {
		return EXTEfx.alIsAuxiliaryEffectSlot(id);
	}

	public void setGain(float gain) throws OpenAlException {
		if (gain < 0 || gain > 1) {
			throw new EffectsExtensionException("Gain must be a float in [0, 1], got %f".formatted(gain));
		}
		setUnchecked(EffectSlotProperty.GAIN, gain);
		EffectsExtensionException.expect("Auxiliary effect slot %s: setting gain to %f".formatted(id, gain));
	}

	public void setEffect(Effect effect) throws OpenAlException {
		if (effect == null) {
			setUnchecked(EffectSlotProperty.EFFECT, EXTEfx.AL_EFFECTSLOT_NULL);
			EffectsExtensionException.expect("Auxiliary effect slot %s: detaching effect".formatted(id));
		} else {
			setUnchecked(EffectSlotProperty.EFFECT, effect.id());
			EffectsExtensionException.expect("Auxiliary effect slot %s: attaching effect %s".formatted(id, effect));
		}
	}

	void setUnchecked(EffectSlotProperty property, int value) {
		int propertyId = property.id();
		EXTEfx.alAuxiliaryEffectSlotf(id, propertyId, value);
	}

	void setUnchecked(EffectSlotProperty property, float value) {
		int propertyId = property.id();
		EXTEfx.alAuxiliaryEffectSlotf(id, propertyId, value);
	}

	@Override
	public void close() {
		EXTEfx.alDeleteAuxiliaryEffectSlots(id);
	}
}
