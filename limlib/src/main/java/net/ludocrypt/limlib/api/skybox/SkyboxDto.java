package net.ludocrypt.limlib.api.skybox;

import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;

public interface SkyboxDto {
	Identifier getId();
	Codec<? extends SkyboxDto> getCodec();
}
