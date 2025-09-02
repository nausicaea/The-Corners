package net.ludocrypt.specialmodels.api;

import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;

public interface SpecialModelRendererDto {
	Identifier getId();
	Codec<? extends SpecialModelRendererDto> getCodec();
	boolean performOutside();
}
