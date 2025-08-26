package net.ludocrypt.limlib.api.skybox;

import com.mojang.serialization.Codec;

public interface SkyboxDto {
	Codec<? extends SkyboxDto> getCodec();
}
