package net.ludocrypt.specialmodels.impl.render;

public record Vec4b(byte x, byte y, byte z, byte w) {
	public Vec4b(int x, int y, int z, int w) {
		this((byte) x, (byte) y, (byte) z, (byte) w);
	}
}
