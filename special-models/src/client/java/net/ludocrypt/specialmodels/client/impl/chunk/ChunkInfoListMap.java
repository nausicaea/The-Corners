package net.ludocrypt.specialmodels.client.impl.chunk;

import org.jetbrains.annotations.Nullable;

public record ChunkInfoListMap(ChunkInfo[] current) {

	public ChunkInfoListMap(int current) {
		this(new ChunkInfo[current]);
	}

	public void setInfo(BuiltChunk chunk, ChunkInfo info) {
		this.current[chunk.index] = info;
	}

	@Nullable
	public ChunkInfo getInfo(BuiltChunk chunk) {
		int i = chunk.index;
		return i >= 0 && i < this.current.length ? this.current[i] : null;
	}

}
