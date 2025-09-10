package net.ludocrypt.limlib.client.impl.effects.sound.openal;

sealed abstract class PropertyImpl implements Property permits EffectProperty, EffectSlotProperty, SourceProperty {
	PropertyImpl(String name, int id) {
		this.name = name;
		this.propertyId = id;
	}

	private final int propertyId;
	private final String name;

	@Override
	public int id() {
		return propertyId;
	}

	@Override
	public String toString() {
		return name;
	}
}
