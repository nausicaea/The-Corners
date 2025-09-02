package net.ludocrypt.corners.render;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ludocrypt.corners.TheCorners;
import net.ludocrypt.specialmodels.api.SpecialModelRendererDto;
import net.minecraft.util.Identifier;

public record SkyboxRendererDto(String id, boolean performOutside) implements SpecialModelRendererDto {
    public static final Codec<SkyboxRendererDto> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    Codec.STRING.fieldOf("id").stable().forGetter(SkyboxRendererDto::id),
                    Codec.BOOL.fieldOf("performOutside").stable().forGetter(SkyboxRendererDto::performOutside)
            ).apply(instance, instance.stable(SkyboxRendererDto::new))
    );

    public SkyboxRendererDto(String id) {
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
        return this.performOutside;
    }
}
