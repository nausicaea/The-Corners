package net.ludocrypt.specialmodels.client.impl.chunk;

import net.minecraft.client.render.VertexFormat;

public record DrawArrayParameters(VertexFormat vertexFormat, int vertexCount, int indexCount,
								  VertexFormat.DrawMode mode,
								  VertexFormat.IndexType indexType, boolean indexOnly, boolean textured) {

	public int getVertexBufferSize() {
		return this.vertexCount * this.vertexFormat.getVertexSizeByte();
	}

	public int getVertexBufferEnd() {
		return this.getVertexBufferSize();
	}

	public int getIndexBufferStart() {
		return this.indexOnly ? 0 : this.getVertexBufferEnd();
	}

	public int getIndexBufferEnd() {
		return this.getIndexBufferStart() + this.getIndexBufferSize();
	}

	private int getIndexBufferSize() {
		return this.textured ? 0 : this.indexCount * this.indexType.size;
	}

	public int getTotalBufferSize() {
		return this.getIndexBufferEnd();
	}

	public VertexFormat getVertexFormat() {
		return this.vertexFormat;
	}

	public int getVertexCount() {
		return this.vertexCount;
	}

	public int getIndexCount() {
		return this.indexCount;
	}

	public VertexFormat.DrawMode getMode() {
		return this.mode;
	}

	public VertexFormat.IndexType getIndexType() {
		return this.indexType;
	}

	public boolean getIndexOnly() {
		return this.indexOnly;
	}

	public boolean isTextured() {
		return this.textured;
	}

}
