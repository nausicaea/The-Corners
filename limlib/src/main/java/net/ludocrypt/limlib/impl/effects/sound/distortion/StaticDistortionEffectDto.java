package net.ludocrypt.limlib.impl.effects.sound.distortion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ludocrypt.limlib.api.effects.sound.distortion.DistortionEffectDto;
import net.minecraft.util.Identifier;
import org.lwjgl.openal.EXTEfx;

public record StaticDistortionEffectDto(boolean enabled, float edge, float gain, float lowpassCutoff, float eqCenter,
										float eqBandWidth) implements DistortionEffectDto {
	public static final Identifier ID = new Identifier("limlib", "static");
	public static final Codec<StaticDistortionEffectDto> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Codec.BOOL.optionalFieldOf("enabled", true).stable().forGetter((distortion) -> distortion.enabled), Codec
					.floatRange(EXTEfx.AL_DISTORTION_MIN_EDGE, EXTEfx.AL_DISTORTION_MAX_EDGE)
					.optionalFieldOf("edge", EXTEfx.AL_DISTORTION_DEFAULT_EDGE)
					.stable()
					.forGetter((distortion) -> distortion.edge),
				Codec
					.floatRange(EXTEfx.AL_DISTORTION_MIN_GAIN, EXTEfx.AL_DISTORTION_MAX_GAIN)
					.optionalFieldOf("gain", EXTEfx.AL_DISTORTION_DEFAULT_GAIN)
					.stable()
					.forGetter((distortion) -> distortion.gain),
				Codec
					.floatRange(EXTEfx.AL_DISTORTION_MIN_LOWPASS_CUTOFF, EXTEfx.AL_DISTORTION_MAX_LOWPASS_CUTOFF)
					.optionalFieldOf("lowpass_cutoff", EXTEfx.AL_DISTORTION_DEFAULT_LOWPASS_CUTOFF)
					.stable()
					.forGetter((distortion) -> distortion.lowpassCutoff),
				Codec
					.floatRange(EXTEfx.AL_DISTORTION_MIN_EQCENTER, EXTEfx.AL_DISTORTION_MAX_EQCENTER)
					.optionalFieldOf("eq_center", EXTEfx.AL_DISTORTION_DEFAULT_EQCENTER)
					.stable()
					.forGetter((distortion) -> distortion.eqCenter),
				Codec
					.floatRange(EXTEfx.AL_DISTORTION_MIN_EQBANDWIDTH, EXTEfx.AL_DISTORTION_MAX_EQBANDWIDTH)
					.optionalFieldOf("eq_band_width", EXTEfx.AL_DISTORTION_DEFAULT_EQBANDWIDTH)
					.stable()
					.forGetter((distortion) -> distortion.eqBandWidth))
			.apply(instance, instance.stable(StaticDistortionEffectDto::new));
	});

	@Override
	public Identifier getId() {
		return ID;
	}

	@Override
	public Codec<StaticDistortionEffectDto> getCodec() {
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

	public static class Builder {

		private boolean enabled = true;
		private float edge = EXTEfx.AL_DISTORTION_DEFAULT_EDGE;
		private float gain = EXTEfx.AL_DISTORTION_DEFAULT_GAIN;
		private float lowpassCutoff = EXTEfx.AL_DISTORTION_DEFAULT_LOWPASS_CUTOFF;
		private float eqCenter = EXTEfx.AL_DISTORTION_DEFAULT_EQCENTER;
		private float eqBandWidth = EXTEfx.AL_DISTORTION_DEFAULT_EQBANDWIDTH;

		public Builder setEnabled(boolean enabled) {
			this.enabled = enabled;
			return this;
		}

		public Builder setEdge(float edge) {
			this.edge = edge;
			return this;
		}

		public Builder setGain(float gain) {
			this.gain = gain;
			return this;
		}

		public Builder setLowpassCutoff(float lowpassCutoff) {
			this.lowpassCutoff = lowpassCutoff;
			return this;
		}

		public Builder setEqCenter(float eqCenter) {
			this.eqCenter = eqCenter;
			return this;
		}

		public Builder setEqBandWidth(float eqBandWidth) {
			this.eqBandWidth = eqBandWidth;
			return this;
		}

		public StaticDistortionEffectDto build() {
			return new StaticDistortionEffectDto(this.enabled, this.edge, this.gain, this.lowpassCutoff, this.eqCenter,
				this.eqBandWidth);
		}

	}
}
