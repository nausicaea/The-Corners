package net.ludocrypt.limlib.api.effects.sound.reverb;

import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;

public interface ReverbEffectDto {
	Codec<? extends ReverbEffectDto> getCodec();
	boolean shouldIgnore(Identifier identifier);
}
