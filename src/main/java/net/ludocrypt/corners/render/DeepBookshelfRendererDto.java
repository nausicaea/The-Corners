package net.ludocrypt.corners.render;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ludocrypt.corners.TheCorners;
import net.ludocrypt.specialmodels.api.SpecialModelRendererDto;
import net.minecraft.util.Identifier;

public record DeepBookshelfRendererDto(boolean performOutside) implements SpecialModelRendererDto {
    public static final Identifier ID = new Identifier(TheCorners.MOD_ID, "deep_bookshelf");
    public static final Identifier DEEP_BOOKSHELF_ATLAS_TEXTURE = TheCorners.id("textures/atlas/deep.png");
    public static final Codec<DeepBookshelfRendererDto> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                Codec.BOOL.fieldOf("performOutside").stable().forGetter(DeepBookshelfRendererDto::performOutside)
            ).apply(instance, instance.stable(DeepBookshelfRendererDto::new))
    );

    public DeepBookshelfRendererDto() {
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
