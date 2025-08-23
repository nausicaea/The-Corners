package net.ludocrypt.specialmodels.client.impl.chunk;

import java.util.Map;
import java.util.stream.Collectors;

import net.ludocrypt.specialmodels.client.api.SpecialModelRenderer;
import net.minecraft.client.render.RenderLayer;

public class SpecialBufferBuilderStorage {

	private final Map<SpecialModelRenderer, SpecialBufferBuilder> specialModelBuffers = SpecialModelRenderer.SPECIAL_MODEL_RENDERER
		.getEntrySet()
		.stream()
		.collect(Collectors
			.toMap(Map.Entry::getValue,
				entry -> new SpecialBufferBuilder(RenderLayer.getSolid().getExpectedBufferSize())));

	public SpecialBufferBuilder get(SpecialModelRenderer renderer) {
		return this.specialModelBuffers.get(renderer);
	}

	public void clear() {
		this.specialModelBuffers.values().forEach(SpecialBufferBuilder::clear);
	}

	public void reset() {
		this.specialModelBuffers.values().forEach(SpecialBufferBuilder::discard);
	}

	public Map<SpecialModelRenderer, SpecialBufferBuilder> getSpecialModelBuffers() {
		return specialModelBuffers;
	}

}
