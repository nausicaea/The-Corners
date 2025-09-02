package net.ludocrypt.specialmodels.client.api;

import net.ludocrypt.specialmodels.api.SpecialModelRendererDto;

@FunctionalInterface
public interface SpecialModelRendererFactory<S extends SpecialModelRendererDto> {
	SpecialModelRenderer create(S dto);
}
