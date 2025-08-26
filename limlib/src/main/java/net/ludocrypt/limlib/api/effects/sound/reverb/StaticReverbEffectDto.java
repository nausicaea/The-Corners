package net.ludocrypt.limlib.api.effects.sound.reverb;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import org.lwjgl.openal.EXTEfx;

public record StaticReverbEffectDto(boolean enabled, float density, float diffusion, float gain, float gainHF,
									float decayTime, float decayHFRatio, float airAbsorptionGainHF,
									float reflectionsGainBase, float lateReverbGainBase, float reflectionsDelay,
									float lateReverbDelay, int decayHFLimit) implements ReverbEffectDto {
	public static final Codec<StaticReverbEffectDto> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Codec.BOOL.optionalFieldOf("enabled", true).stable().forGetter((reverb) -> {
					return reverb.enabled;
				}), Codec
					.floatRange(EXTEfx.AL_REVERB_MIN_DENSITY, EXTEfx.AL_REVERB_MAX_DENSITY)
					.optionalFieldOf("density", EXTEfx.AL_REVERB_DEFAULT_DENSITY)
					.stable()
					.forGetter((reverb) -> {
						return reverb.density;
					}),
				Codec
					.floatRange(EXTEfx.AL_REVERB_MIN_DIFFUSION, EXTEfx.AL_REVERB_MAX_DIFFUSION)
					.optionalFieldOf("diffusion", EXTEfx.AL_REVERB_DEFAULT_DIFFUSION)
					.stable()
					.forGetter((reverb) -> {
						return reverb.diffusion;
					}),
				Codec
					.floatRange(EXTEfx.AL_REVERB_MIN_GAIN, EXTEfx.AL_REVERB_MAX_GAIN)
					.optionalFieldOf("gain", EXTEfx.AL_REVERB_DEFAULT_GAIN)
					.stable()
					.forGetter((reverb) -> {
						return reverb.gain;
					}),
				Codec
					.floatRange(EXTEfx.AL_REVERB_MIN_GAINHF, EXTEfx.AL_REVERB_MAX_GAINHF)
					.optionalFieldOf("gain_hf", EXTEfx.AL_REVERB_DEFAULT_GAINHF)
					.stable()
					.forGetter((reverb) -> {
						return reverb.gainHF;
					}),
				Codec
					.floatRange(EXTEfx.AL_REVERB_MIN_DECAY_TIME, EXTEfx.AL_REVERB_MAX_DECAY_TIME)
					.optionalFieldOf("decay_time", EXTEfx.AL_REVERB_DEFAULT_DECAY_TIME)
					.stable()
					.forGetter((reverb) -> {
						return reverb.decayTime;
					}),
				Codec
					.floatRange(EXTEfx.AL_REVERB_MIN_DECAY_HFRATIO, EXTEfx.AL_REVERB_MAX_DECAY_HFRATIO)
					.optionalFieldOf("decay_hf_ratio", EXTEfx.AL_REVERB_DEFAULT_DECAY_HFRATIO)
					.stable()
					.forGetter((reverb) -> {
						return reverb.decayHFRatio;
					}),
				Codec
					.floatRange(EXTEfx.AL_REVERB_MIN_AIR_ABSORPTION_GAINHF, EXTEfx.AL_REVERB_MAX_AIR_ABSORPTION_GAINHF)
					.optionalFieldOf("air_absorption_gain_hf", EXTEfx.AL_REVERB_DEFAULT_AIR_ABSORPTION_GAINHF)
					.stable()
					.forGetter((reverb) -> {
						return reverb.airAbsorptionGainHF;
					}),
				Codec
					.floatRange(EXTEfx.AL_REVERB_MIN_REFLECTIONS_GAIN, EXTEfx.AL_REVERB_MAX_REFLECTIONS_GAIN)
					.optionalFieldOf("max_reflections_gain", EXTEfx.AL_REVERB_DEFAULT_REFLECTIONS_GAIN)
					.stable()
					.forGetter((reverb) -> {
						return reverb.reflectionsGainBase;
					}),
				Codec
					.floatRange(EXTEfx.AL_REVERB_MIN_LATE_REVERB_GAIN, EXTEfx.AL_REVERB_MAX_LATE_REVERB_GAIN)
					.optionalFieldOf("late_reverb_gain", EXTEfx.AL_REVERB_DEFAULT_LATE_REVERB_GAIN)
					.stable()
					.forGetter((reverb) -> {
						return reverb.lateReverbGainBase;
					}),
				Codec
					.floatRange(EXTEfx.AL_REVERB_MIN_REFLECTIONS_DELAY, EXTEfx.AL_REVERB_MAX_REFLECTIONS_DELAY)
					.optionalFieldOf("reflections_delay", EXTEfx.AL_REVERB_DEFAULT_REFLECTIONS_DELAY)
					.stable()
					.forGetter((reverb) -> {
						return reverb.reflectionsDelay;
					}),
				Codec
					.floatRange(EXTEfx.AL_REVERB_MIN_LATE_REVERB_DELAY, EXTEfx.AL_REVERB_MAX_LATE_REVERB_DELAY)
					.optionalFieldOf("late_reverb_delay", EXTEfx.AL_REVERB_DEFAULT_LATE_REVERB_DELAY)
					.stable()
					.forGetter((reverb) -> {
						return reverb.lateReverbDelay;
					}),
				Codec
					.intRange(EXTEfx.AL_REVERB_MIN_DECAY_HFLIMIT, EXTEfx.AL_REVERB_MAX_DECAY_HFLIMIT)
					.optionalFieldOf("decay_hf_limit", EXTEfx.AL_REVERB_DEFAULT_DECAY_HFLIMIT)
					.stable()
					.forGetter((reverb) -> {
						return reverb.decayHFLimit;
					}))
			.apply(instance, instance.stable(StaticReverbEffectDto::new));
	});

	@Override
	public Codec<? extends ReverbEffectDto> getCodec() {
		return CODEC;
	}

	@Override
	public boolean shouldIgnore(Identifier identifier) {
		return identifier.getPath().contains("ui.") || identifier.getPath().contains("music.") || identifier
			.getPath()
			.contains("block.lava.pop") || identifier.getPath().contains("weather.") || identifier
			.getPath()
			.startsWith("atmosfera") || identifier.getPath().startsWith("dynmus");
	}
}
