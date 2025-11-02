package net.ludocrypt.specialmodels.client.impl.chunk;

import java.util.LinkedHashSet;

public record RenderableChunks(ChunkInfoListMap builtChunkMap, LinkedHashSet<ChunkInfo> builtChunks) {
	public RenderableChunks(int size) {
		this(new ChunkInfoListMap(size), new LinkedHashSet<>(size));
	}
}
