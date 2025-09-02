package net.ludocrypt.corners.render;

import com.mojang.serialization.Codec;
import net.ludocrypt.corners.TheCorners;
import net.ludocrypt.specialmodels.api.SpecialModelRendererDto;
import net.minecraft.util.Identifier;

public class DeepBookshelfRendererDto implements SpecialModelRendererDto {
    public static final Identifier ID = new Identifier(TheCorners.MOD_ID, "deep_bookshelf");
    public static final Identifier DEEP_BOOKSHELF_ATLAS_TEXTURE = TheCorners.id("textures/atlas/deep.png");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public Codec<? extends SpecialModelRendererDto> getCodec() {
        return null;
    }

    @Override
    public boolean performOutside() {
        return false;
    }
}
