package net.ludocrypt.specialmodels.client.impl.chunk;

import java.util.LinkedHashSet;

public class RenderableChunks {

	public final ChunkInfoListMap builtChunkMap;
	public final LinkedHashSet<ChunkInfo> builtChunks;

	public RenderableChunks(int size) {
		this.builtChunkMap = new ChunkInfoListMap(size);
		this.builtChunks = new LinkedHashSet<ChunkInfo>(size);
	}

}
