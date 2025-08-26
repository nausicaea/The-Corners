package net.ludocrypt.limlib.client.api.skybox;

import net.ludocrypt.limlib.api.skybox.SkyboxDto;

@FunctionalInterface
public interface SkyboxFactory<S extends SkyboxDto> {
	Skybox create(S dto);
}
