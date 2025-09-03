package net.ludocrypt.specialmodels.client.impl.chunk;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.function.Supplier;

import net.ludocrypt.specialmodels.impl.SpecialModels;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;
import com.mojang.blaze3d.systems.VertexSorter;

import it.unimi.dsi.fastutil.ints.IntConsumer;
import net.ludocrypt.specialmodels.client.impl.render.SpecialVertexFormats;
import net.ludocrypt.specialmodels.impl.render.Vec4b;
import net.minecraft.client.render.BufferVertexConsumer;
import net.minecraft.client.render.FixedColorVertexConsumer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormat.IndexType;
import net.minecraft.client.render.VertexFormatElement;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.GlAllocationUtils;
import net.minecraft.util.math.MathHelper;

public class SpecialBufferBuilder extends FixedColorVertexConsumer implements BufferVertexConsumer {

	private ByteBuffer buffer;

	private int renderedBufferCount;
	private int renderedBufferPointer;

	private int elementOffset;

	private int vertexCount;

	@Nullable
	private VertexFormatElement currentElement;

	private int elementIndex;

	private VertexFormat format;
	private DrawMode drawMode;

	private boolean textured;
	private boolean hasOverlay;
	private boolean building;

	@Nullable
	private Vector3f[] sortingPoints;

	@Nullable
	private VertexSorter quadSorting;

	private boolean indexOnly;

	@Nullable
	private Supplier<Vec4b> state;

	public SpecialBufferBuilder(int initialCapacity) {
		this.buffer = GlAllocationUtils.allocateByteBuffer(initialCapacity * 6);
	}

	private void grow() {
		this.grow(this.format.getVertexSizeByte());
	}

	private void grow(int size) {

		if (this.elementOffset + size > this.buffer.capacity()) {
			int oldSize = this.buffer.capacity();
			int newSize = oldSize + roundBufferSize(size);
			SpecialModels.LOGGER.debug("Needed to grow BufferBuilder buffer: Old size {} bytes, new size {} bytes.", oldSize, newSize);
			ByteBuffer byteBuffer = GlAllocationUtils.resizeByteBuffer(this.buffer, newSize);
			byteBuffer.rewind();
			this.buffer = byteBuffer;
		}

	}

	private static int roundBufferSize(int amount) {
		int size = 2097152;

		if (amount == 0) {
			return size;
		} else {

			if (amount < 0) {
				size *= -1;
			}

			int j = amount % size;
			return j == 0 ? amount : amount + size - j;
		}

	}

	public void setQuadSorting(VertexSorter sorting) {

		if (this.drawMode == VertexFormat.DrawMode.QUADS) {
			this.quadSorting = sorting;

			if (this.sortingPoints == null) {
				this.sortingPoints = this.createSortingPoints();
			}

		}

	}

	public SortState popState() {
		return new SortState(this.drawMode, this.vertexCount, this.sortingPoints, this.quadSorting);
	}

	public void restoreState(SortState state) {
		this.buffer.rewind();
		this.drawMode = state.drawMode();
		this.vertexCount = state.vertexCount();
		this.elementOffset = this.renderedBufferPointer;
		this.sortingPoints = state.sortingPoints();
		this.quadSorting = state.quadSorting();
		this.indexOnly = true;
	}

	public void begin(DrawMode drawMode, VertexFormat format) {

		if (this.building) {
			throw new IllegalStateException("Already building!");
		} else {
			this.building = true;
			this.drawMode = drawMode;
			this.setFormat(format);
			this.currentElement = format.getElements().get(0);
			this.elementIndex = 0;
			this.buffer.rewind();
		}

	}

	private void setFormat(VertexFormat format) {

		if (this.format != format) {
			this.format = format;
			boolean hasTextureAndOverlay = format == VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL;
			boolean hasTexture = format == VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL;
			boolean hasTextureAndState = format == SpecialVertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL_STATE;
			this.textured = hasTextureAndOverlay || hasTexture || hasTextureAndState;
			this.hasOverlay = hasTextureAndOverlay;
		}

	}

	private IntConsumer createConsumer(int i, IndexType indexType) {
		MutableInt mutableInt = new MutableInt(i);

		return switch (indexType) {
			case SHORT -> value -> this.buffer.putShort(mutableInt.getAndAdd(2), (short) value);
			case INT -> value -> this.buffer.putInt(mutableInt.getAndAdd(4), value);
		};
	}

	private Vector3f[] createSortingPoints() {
		FloatBuffer floatBuffer = this.buffer.asFloatBuffer();
		int pointer = this.renderedBufferPointer / 4;
		int formatSize = this.format.getVertexSizeInteger();
		int drawSize = formatSize * this.drawMode.additionalVertexCount;
		int expected = this.vertexCount / this.drawMode.additionalVertexCount;
		Vector3f[] sort = new Vector3f[expected];

		for (int i = 0; i < expected; ++i) {
			float x1 = floatBuffer.get(pointer + i * drawSize + 0);
			float y1 = floatBuffer.get(pointer + i * drawSize + 1);
			float z1 = floatBuffer.get(pointer + i * drawSize + 2);
			float x2 = floatBuffer.get(pointer + i * drawSize + formatSize * 2 + 0);
			float y2 = floatBuffer.get(pointer + i * drawSize + formatSize * 2 + 1);
			float z2 = floatBuffer.get(pointer + i * drawSize + formatSize * 2 + 2);
			float x = (x1 + x2) / 2.0F;
			float y = (y1 + y2) / 2.0F;
			float z = (z1 + z2) / 2.0F;
			sort[i] = new Vector3f(x, y, z);
		}

		return sort;
	}

	private void putSortedIndices(IndexType indexType) {

		if (this.sortingPoints != null && this.quadSorting != null) {
			int[] sorted = this.quadSorting.sort(this.sortingPoints);
			IntConsumer consumer = this.createConsumer(this.elementOffset, indexType);

			for (int i : sorted) {
				consumer.accept(i * this.drawMode.additionalVertexCount + 0);
				consumer.accept(i * this.drawMode.additionalVertexCount + 1);
				consumer.accept(i * this.drawMode.additionalVertexCount + 2);
				consumer.accept(i * this.drawMode.additionalVertexCount + 2);
				consumer.accept(i * this.drawMode.additionalVertexCount + 3);
				consumer.accept(i * this.drawMode.additionalVertexCount + 0);
			}

		} else {
			throw new IllegalStateException("Sorting state uninitialized");
		}

	}

	public boolean isCurrentBatchEmpty() {
		return this.vertexCount == 0;
	}

	@Nullable
	public RenderedBuffer endOrDiscard() {
		this.ensureDrawing();

		if (this.isCurrentBatchEmpty()) {
			this.reset();
			return null;
		} else {
			RenderedBuffer buffer = this.buildBatchParameters();
			this.reset();
			return buffer;
		}

	}

	public RenderedBuffer end() {
		this.ensureDrawing();
		RenderedBuffer buffer = this.buildBatchParameters();
		this.reset();
		return buffer;
	}

	private void ensureDrawing() {

		if (!this.building) {
			throw new IllegalStateException("Not building!");
		}

	}

	private RenderedBuffer buildBatchParameters() {
		int drawCount = this.drawMode.getIndexCount(this.vertexCount);
		int vertexSize = !this.indexOnly ? this.vertexCount * this.format.getVertexSizeByte() : 0;
		IndexType type = IndexType.smallestFor(drawCount);
		boolean textured = true;
		int offset = vertexSize;

		if (this.sortingPoints != null) {
			int growth = MathHelper.roundUpToMultiple(drawCount * type.size, 4);
			this.grow(growth);
			this.putSortedIndices(type);
			this.elementOffset += growth;
			offset = vertexSize + growth;
			textured = false;
		}

		int pointer = this.renderedBufferPointer;

		this.renderedBufferPointer += offset;
		this.renderedBufferCount++;

		DrawArrayParameters params = new DrawArrayParameters(this.format, this.vertexCount, drawCount, this.drawMode, type,
			this.indexOnly, textured);

		return new RenderedBuffer(this, pointer, params);
	}

	private void reset() {
		this.building = false;
		this.vertexCount = 0;
		this.currentElement = null;
		this.elementIndex = 0;
		this.sortingPoints = null;
		this.quadSorting = null;
		this.indexOnly = false;
	}

	@Override
	public void putByte(int index, byte value) {
		this.buffer.put(this.elementOffset + index, value);
	}

	@Override
	public void putShort(int index, short value) {
		this.buffer.putShort(this.elementOffset + index, value);
	}

	@Override
	public void putFloat(int index, float value) {
		this.buffer.putFloat(this.elementOffset + index, value);
	}

	@Override
	public void next() {

		if (this.elementIndex != 0) {
			throw new IllegalStateException("Not filled all elements of the vertex");
		} else {
			this.vertexCount++;
			this.grow();

			if (this.drawMode == VertexFormat.DrawMode.LINES || this.drawMode == VertexFormat.DrawMode.LINE_STRIP) {
				int size = this.format.getVertexSizeByte();
				this.buffer.put(this.elementOffset, this.buffer, this.elementOffset - size, size);
				this.elementOffset += size;
				this.vertexCount++;
				this.grow();
			}

		}

	}

	@Override
	public void nextElement() {
		List<VertexFormatElement> elements = this.format.getElements();

		this.elementIndex = (this.elementIndex + 1) % elements.size();
		this.elementOffset += this.currentElement.getByteLength();

		VertexFormatElement element = elements.get(this.elementIndex);
		this.currentElement = element;

		if (element.getType() == VertexFormatElement.Type.PADDING) {
			this.nextElement();
		}

		if (this.colorFixed && this.currentElement.getType() == VertexFormatElement.Type.COLOR) {
			BufferVertexConsumer.super.color(this.fixedRed, this.fixedGreen, this.fixedBlue, this.fixedAlpha);
		}

	}

	@Override
	public VertexConsumer color(int red, int green, int blue, int alpha) {

		if (this.colorFixed) {
			throw new IllegalStateException();
		} else {
			return BufferVertexConsumer.super.color(red, green, blue, alpha);
		}

	}

	@Override
	public void vertex(float x, float y, float z, float red, float green, float blue, float alpha, float u, float v,
			int overlay, int light, float normalX, float normalY, float normalZ) {

		if (this.colorFixed) {
			throw new IllegalStateException();
		} else if (this.textured) {
			this.putFloat(0, x);
			this.putFloat(4, y);
			this.putFloat(8, z);
			this.putByte(12, (byte) (red * 255.0F));
			this.putByte(13, (byte) (green * 255.0F));
			this.putByte(14, (byte) (blue * 255.0F));
			this.putByte(15, (byte) (alpha * 255.0F));
			this.putFloat(16, u);
			this.putFloat(20, v);
			int o = 24;

			if (this.hasOverlay) {
				this.putShort(24, (short) (overlay & 65535));
				this.putShort(26, (short) (overlay >> 16 & 65535));
				o += 4;
			}

			this.putShort(o + 0, (short) (light & 65535));
			this.putShort(o + 2, (short) (light >> 16 & 65535));
			this.putByte(o + 4, BufferVertexConsumer.packByte(normalX));
			this.putByte(o + 5, BufferVertexConsumer.packByte(normalY));
			this.putByte(o + 6, BufferVertexConsumer.packByte(normalZ));

			if (format == SpecialVertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL_STATE) {
				Vec4b state = this.state.get();
				this.putByte(o + 7, state.x());
				this.putByte(o + 8, state.y());
				this.putByte(o + 9, state.z());
				this.putByte(o + 10, state.w());
				o += 4;
			}

			this.elementOffset += o + 8;
			this.next();
		} else {
			super.vertex(x, y, z, red, green, blue, alpha, u, v, overlay, light, normalX, normalY, normalZ);
		}

	}

	void popBatch() {

		if (this.renderedBufferCount > 0 && --this.renderedBufferCount == 0) {
			this.clear();
		}

	}

	public void clear() {

		if (this.renderedBufferCount > 0) {
			SpecialModels.LOGGER.warn("Clearing BufferBuilder with unused batches");
		}

		this.discard();
	}

	public void discard() {
		this.renderedBufferCount = 0;
		this.renderedBufferPointer = 0;
		this.elementOffset = 0;
	}

	@Override
	public VertexFormatElement getCurrentElement() {

		if (this.currentElement == null) {
			throw new IllegalStateException("BufferBuilder not started");
		} else {
			return this.currentElement;
		}

	}

	public boolean isBuilding() {
		return this.building;
	}

	ByteBuffer getBuffer(int start, int end) {
		return MemoryUtil.memSlice(this.buffer, start, end - start);
	}

	public Supplier<Vec4b> getState() {
		return state;
	}

	public void setState(Supplier<Vec4b> state) {
		this.state = state;
	}

}
