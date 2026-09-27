package net.ludocrypt.specialmodels.client.impl.chunk;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.ludocrypt.specialmodels.client.api.SpecialModelRenderer;
import net.minecraft.client.render.chunk.ChunkOcclusionData;

import java.util.Map;

public final class RenderedChunkData {

	public final Map<SpecialModelRenderer, RenderedBuffer> renderedBuffers = new Reference2ObjectArrayMap<>();
	public final Map<SpecialModelRenderer, SortState> bufferStates = new Reference2ObjectArrayMap<>();
	public ChunkOcclusionData occlusionGraph = new ChunkOcclusionData();

}
