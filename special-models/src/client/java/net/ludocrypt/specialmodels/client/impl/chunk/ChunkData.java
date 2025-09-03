package net.ludocrypt.specialmodels.client.impl.chunk;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.ludocrypt.specialmodels.client.api.SpecialModelRenderer;
import net.minecraft.client.render.chunk.ChunkOcclusionData;
import net.minecraft.util.math.Direction;

import java.util.Map;

public class ChunkData {

	public static final ChunkData EMPTY = new ChunkData() {

		@Override
		public boolean isVisibleThrough(Direction from, Direction to) {
			return false;
		}

	};

	public final Map<SpecialModelRenderer, RenderedBuffer> renderedBuffers = new Reference2ObjectArrayMap<>();
	public final Map<SpecialModelRenderer, SortState> bufferStates = new Reference2ObjectArrayMap<>();

	public ChunkOcclusionData occlusionGraph = new ChunkOcclusionData();

	public boolean isEmpty() {
		return this.renderedBuffers.isEmpty();
	}

	public boolean isEmpty(SpecialModelRenderer layer) {
		return !this.renderedBuffers
			.containsKey(
				layer) || (this.renderedBuffers.containsKey(layer) && this.renderedBuffers.get(layer).isEmpty());
	}

	public boolean isVisibleThrough(Direction from, Direction to) {
		return this.occlusionGraph.isVisibleThrough(from, to);
	}

}
