package net.ludocrypt.limlib.api.effects.sky;

import net.minecraft.util.StringIdentifiable;

public enum SkyTypeDto implements StringIdentifiable {
	NONE("none"),
	NORMAL("normal"),
	END("end");

	public static final Codec<SkyTypeDto> CODEC = StringIdentifiable.createCodec(SkyTypeDto::values);
	private final String name;

	SkyTypeDto(String name) {
		this.name = name;
	}

	@Override
	public String asString() {
		return name;
	}
}
