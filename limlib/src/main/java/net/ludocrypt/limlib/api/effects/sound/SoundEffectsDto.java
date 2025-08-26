package net.ludocrypt.limlib.api.effects.sound;

import net.ludocrypt.limlib.api.effects.sound.distortion.DistortionEffectDto;
import net.ludocrypt.limlib.api.effects.sound.reverb.ReverbEffectDto;
import net.minecraft.sound.MusicSound;

import java.util.Optional;

public record SoundEffectsDto(Optional<ReverbEffectDto> reverb,
							  Optional<DistortionEffectDto> distortion,
							  Optional<MusicSound> music) {
	public SoundEffectsDto() {
		this(Optional.empty(), Optional.empty(), Optional.empty());
	}
}
