package net.ludocrypt.specialmodels.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ludocrypt.specialmodels.impl.SpecialModelsRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

public record TexturedSpecialModelRendererDto(boolean performOutside) implements SpecialModelRendererDto {
	public static final Identifier ID = new Identifier("specialmodels", "textured");
	public static final Codec<TexturedSpecialModelRendererDto> CODEC = RecordCodecBuilder.create(instance -> instance
		.group(Codec.BOOL.fieldOf("performOutside").stable().forGetter(TexturedSpecialModelRendererDto::performOutside))
		.apply(instance, instance.stable(TexturedSpecialModelRendererDto::new))
	);

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
