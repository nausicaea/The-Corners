package net.ludocrypt.corners.util;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;

public class RadioSoundTable {

	private final RegistryEntry.Reference<SoundEvent> musicSound;
	private final RegistryEntry.Reference<SoundEvent> staticSound;
	private final RegistryEntry.Reference<SoundEvent> radioSound;

	public RadioSoundTable(RegistryEntry.Reference<SoundEvent> musicSound, RegistryEntry.Reference<SoundEvent> staticSound,
			RegistryEntry.Reference<SoundEvent> radioSound) {
		this.musicSound = musicSound;
		this.staticSound = staticSound;
		this.radioSound = radioSound;
	}

	public RegistryEntry.Reference<SoundEvent> getMusicSound() {
		return musicSound;
	}

	public RegistryEntry.Reference<SoundEvent> getStaticSound() {
		return staticSound;
	}

	public RegistryEntry.Reference<SoundEvent> getRadioSound() {
		return radioSound;
	}

}
