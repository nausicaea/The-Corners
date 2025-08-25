package net.ludocrypt.limlib.client.api.effects.sound;

import java.util.Optional;

import net.ludocrypt.limlib.client.api.effects.sound.distortion.DistortionEffect;
import net.ludocrypt.limlib.client.api.effects.sound.reverb.ReverbEffect;
import net.minecraft.sound.MusicSound;

public class SoundEffects {

	private final Optional<ReverbEffect> reverb;
	private final Optional<DistortionEffect> distortion;
	private final Optional<MusicSound> music;

	public SoundEffects() {
		this(Optional.empty(), Optional.empty(), Optional.empty());
	}

	public SoundEffects(Optional<ReverbEffect> reverb, Optional<DistortionEffect> distortion, Optional<MusicSound> music) {
		this.reverb = reverb;
		this.distortion = distortion;
		this.music = music;
	}

	public Optional<ReverbEffect> getReverb() {
		return reverb;
	}

	public Optional<DistortionEffect> getDistortion() {
		return distortion;
	}

	public Optional<MusicSound> getMusic() {
		return music;
	}

}
