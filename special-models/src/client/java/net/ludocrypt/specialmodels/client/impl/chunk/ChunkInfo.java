package net.ludocrypt.specialmodels.client.impl.chunk;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class ChunkInfo {

	public final BuiltChunk chunk;
	private byte direction;
	public byte cullingState;
	public final int propagationLevel;

	public ChunkInfo(BuiltChunk chunk, @Nullable Direction direction, int propagationLevel) {
		this.chunk = chunk;

		if (direction != null) {
			this.addDirection(direction);
		}

		this.propagationLevel = propagationLevel;
	}

	public void updateCullingState(byte parentCullingState, Direction from) {
		this.cullingState = (byte) (this.cullingState | parentCullingState | 1 << from.ordinal());
	}

	public boolean canCull(Direction from) {
		return (this.cullingState & 1 << from.ordinal()) > 0;
	}

	public void addDirection(Direction direction) {
		this.direction = (byte) (this.direction | this.direction | 1 << direction.ordinal());
	}

	public boolean hasDirection(int ordinal) {
		return (this.direction & 1 << ordinal) > 0;
	}

	public boolean hasAnyDirection() {
		return this.direction != 0;
	}

	public boolean isAxisAlignedWith(int i, int j, int k) {
		BlockPos blockPos = this.chunk.getOrigin();
		return i == blockPos.getX() / 16 || k == blockPos.getZ() / 16 || j == blockPos.getY() / 16;
	}

	public int hashCode() {
		return this.chunk.getOrigin().hashCode();
	}

	public boolean equals(Object object) {

		if (!(object instanceof ChunkInfo)) {
			return false;
		} else {
			ChunkInfo chunkInfo = (ChunkInfo) object;
			return this.chunk.getOrigin().equals(chunkInfo.chunk.getOrigin());
		}

	}

}
