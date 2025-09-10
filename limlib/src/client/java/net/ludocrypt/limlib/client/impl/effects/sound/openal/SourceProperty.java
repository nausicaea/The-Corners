package net.ludocrypt.limlib.client.impl.effects.sound.openal;

import org.lwjgl.openal.EXTEfx;

public final class SourceProperty extends PropertyImpl {
	public static final SourceProperty DIRECT_FILTER = new SourceProperty("EXTEfx.AL_DIRECT_FILTER", EXTEfx.AL_DIRECT_FILTER);
	public static final SourceProperty AUXILIARY_SEND_FILTER = new SourceProperty("EXTEfx.AL_AUXILIARY_SEND_FILTER", EXTEfx.AL_AUXILIARY_SEND_FILTER);

	private SourceProperty(String name, int id) {
		super(name, id);
	}
}
