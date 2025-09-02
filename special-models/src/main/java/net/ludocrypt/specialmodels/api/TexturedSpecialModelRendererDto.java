package net.ludocrypt.specialmodels.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ludocrypt.specialmodels.impl.SpecialModelsRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public record TexturedSpecialModelRendererDto(boolean performOutside) implements SpecialModelRendererDto {
	public static final Identifier ID = new Identifier("specialmodels", "textured");
	public static final Codec<TexturedSpecialModelRendererDto> CODEC = RecordCodecBuilder.create(instance -> instance.stable(new TexturedSpecialModelRendererDto()));

	public TexturedSpecialModelRendererDto() {
		this(true);
	}

	@Override
	public Identifier getId() {
		return ID;
	}

	@Override
	public Codec<? extends SpecialModelRendererDto> getCodec() {
		return CODEC;
	}

	@Override
	public boolean performOutside() {
		return performOutside;
	}
}
