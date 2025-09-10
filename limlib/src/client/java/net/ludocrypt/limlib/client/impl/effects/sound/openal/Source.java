package net.ludocrypt.limlib.client.impl.effects.sound.openal;

import net.ludocrypt.limlib.client.impl.mixin.SourceAccessor;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.EXTEfx;

public record Source(int id) {
	public Source(net.minecraft.client.sound.Source source) {
		this(((SourceAccessor) source).getPointer());
	}

	public void setDirectFilter(FilterId filterId) throws OpenAlException {
		if (filterId == null) {
			setUnchecked(SourceProperty.DIRECT_FILTER, EXTEfx.AL_FILTER_NULL);
			OpenAlException.expect("Source %s: detaching direct filter".formatted(id));
		} else {
			setUnchecked(SourceProperty.DIRECT_FILTER, filterId.id());
			OpenAlException.expect("Source %s: attaching direct filter %s".formatted(id, filterId));
		}
	}

	public void setAuxiliarySendFilter(EffectSlot effectSlot, int auxiliarySendNr, FilterId filterId) throws OpenAlException {
		var filterIdRaw = filterId != null ? filterId.id() : EXTEfx.AL_FILTER_NULL;
		if (effectSlot == null) {
			setUnchecked(SourceProperty.AUXILIARY_SEND_FILTER, EXTEfx.AL_EFFECTSLOT_NULL, auxiliarySendNr, filterIdRaw);
			OpenAlException.expect("Source %s: detaching send filter".formatted(id));
		} else {
			setUnchecked(SourceProperty.AUXILIARY_SEND_FILTER, effectSlot.id(), auxiliarySendNr, filterIdRaw);
			OpenAlException.expect("Source %s: attaching send filter to effect slot %s with auxiliary send number %d and filter %s".formatted(id, effectSlot, auxiliarySendNr, filterId));
		}
	}

	void setUnchecked(SourceProperty property, int value) {
		int propertyId = property.id();
		AL11.alSourcei(id, propertyId, value);
	}

	void setUnchecked(SourceProperty property, int value1, int value2, int value3) {
		int propertyId = property.id();
		AL11.alSource3i(id, propertyId, value1, value2, value3);
	}
}
