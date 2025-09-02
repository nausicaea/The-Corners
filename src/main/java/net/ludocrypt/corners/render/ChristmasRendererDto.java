package net.ludocrypt.corners.render;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ludocrypt.corners.TheCorners;
import net.ludocrypt.specialmodels.api.SpecialModelRendererDto;
import net.minecraft.util.Identifier;

public record ChristmasRendererDto(String id, boolean performOutside) implements SpecialModelRendererDto {
    public static final Codec<ChristmasRendererDto> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    Codec.STRING.fieldOf("id").stable().forGetter(ChristmasRendererDto::id),
                    Codec.BOOL.fieldOf("performOutside").stable().forGetter(ChristmasRendererDto::performOutside)
            ).apply(instance, instance.stable(ChristmasRendererDto::new))
    );

    public ChristmasRendererDto(String id) {
        this(id, true);
    }

    @Override
    public Identifier getId() {
        return TheCorners.id(this.id);
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
