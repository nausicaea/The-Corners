package net.ludocrypt.corners.render;

import com.mojang.serialization.Codec;
import net.ludocrypt.corners.TheCorners;
import net.ludocrypt.specialmodels.api.SpecialModelRendererDto;
import net.minecraft.util.Identifier;

public record SkyboxRendererDto(String id) implements SpecialModelRendererDto {
    @Override
    public Identifier getId() {
        return TheCorners.id(this.id);
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
