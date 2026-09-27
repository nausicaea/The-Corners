package net.ludocrypt.specialmodels.client.impl.chunk;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.ludocrypt.specialmodels.client.impl.mixin.render.UsageAccessor;
import net.ludocrypt.specialmodels.client.impl.mixin.render.VertexBufferAccessor;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.VertexFormat;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;

public class RenderedBuffer {

	private final SpecialBufferBuilder specialBufferBuilder;
	private final int pointer;
	private final DrawArrayParameters parameters;
	private boolean released;

	RenderedBuffer(SpecialBufferBuilder specialBufferBuilder, int pointer, DrawArrayParameters parameters) {
		this.specialBufferBuilder = specialBufferBuilder;
		this.pointer = pointer;
		this.parameters = parameters;
	}

	public ByteBuffer getVertexBuffer() {
		int start = this.pointer;
		int end = this.pointer + this.parameters.getVertexBufferEnd();
		return specialBufferBuilder.getBuffer(start, end);
	}

	public ByteBuffer getIndexBuffer() {
		int start = this.pointer + this.parameters.getIndexBufferStart();
		int end = this.pointer + this.parameters.getIndexBufferEnd();
		return specialBufferBuilder.getBuffer(start, end);
	}

	public DrawArrayParameters getParameters() {
		return this.parameters;
	}

	public boolean isEmpty() {
		return this.parameters.vertexCount() == 0;
	}

	public void release() {

		if (this.released) {
			throw new IllegalStateException("Buffer has already been released!");
		} else {
			specialBufferBuilder.popBatch();
			this.released = true;
		}

	}

	public void upload(VertexBuffer buffer) {

		if (!buffer.isClosed()) {
			RenderSystem.assertOnRenderThread();

			try {
				DrawArrayParameters params = this.getParameters();
				((VertexBufferAccessor) buffer)
					.setVertexFormat(this.uploadAndBindFormat(buffer, params, this.getVertexBuffer()));
				((VertexBufferAccessor) buffer)
					.setSharedSequentialIndexBuffer(this.uploadIndexBuffer(buffer, params, this.getIndexBuffer()));
				((VertexBufferAccessor) buffer).setIndexCount(params.getIndexCount());
				((VertexBufferAccessor) buffer).setIndexType(params.getIndexType());
				((VertexBufferAccessor) buffer).setDrawMode(params.getMode());
			} finally {
				this.release();
			}

		}

	}

	private VertexFormat uploadAndBindFormat(VertexBuffer buffer, DrawArrayParameters parameters, ByteBuffer bytes) {
		boolean rebind = false;

		if (!parameters.getVertexFormat().equals(((VertexBufferAccessor) buffer).getVertexFormat())) {

			if (((VertexBufferAccessor) buffer).getVertexFormat() != null) {
				((VertexBufferAccessor) buffer).getVertexFormat().clearState();
			}

			GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, ((VertexBufferAccessor) buffer).getVertexBufferId());
			parameters.getVertexFormat().setupState();
			rebind = true;
		}

		if (!parameters.getIndexOnly()) {

			if (!rebind) {
				GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, ((VertexBufferAccessor) buffer).getVertexBufferId());
			}

			RenderSystem
				.glBufferData(34962, bytes,
					((UsageAccessor) (Object) ((VertexBufferAccessor) buffer).getUsage()).getId());
		}

		return parameters.getVertexFormat();
	}

	@Nullable
	private RenderSystem.ShapeIndexBuffer uploadIndexBuffer(VertexBuffer buffer, DrawArrayParameters parameters,
															ByteBuffer bytes) {

		if (!parameters.isTextured()) {
			GlStateManager
				._glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ((VertexBufferAccessor) buffer).getIndexBufferId());
			RenderSystem
				.glBufferData(34963, bytes,
					((UsageAccessor) (Object) ((VertexBufferAccessor) buffer).getUsage()).getId());
			return null;
		} else {
			RenderSystem.ShapeIndexBuffer indexBuffer = RenderSystem.getSequentialBuffer(parameters.getMode());

			if (indexBuffer != ((VertexBufferAccessor) buffer).getSharedSequentialIndexBuffer() || !indexBuffer
				.isLargeEnough(parameters.getIndexCount())) {
				indexBuffer.bindAndGrow(parameters.getIndexCount());
			}

			return indexBuffer;
		}

	}

}
