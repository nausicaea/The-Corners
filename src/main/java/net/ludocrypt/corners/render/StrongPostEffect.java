package net.ludocrypt.corners.render;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.ludocrypt.corners.config.CornerConfig;
import net.ludocrypt.limlib.api.effects.post.PostEffect;
import net.minecraft.util.Identifier;

public record StrongPostEffect(Identifier shaderName, Identifier fallbackShaderName) implements PostEffect {

    public static final Codec<StrongPostEffect> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Identifier.CODEC.fieldOf("shader_name").stable().forGetter((postEffect) -> {
            return postEffect.shaderName;
        }), Identifier.CODEC.fieldOf("fallback_shader_name").stable().forGetter((postEffect) -> {
            return postEffect.fallbackShaderName;
        })).apply(instance, instance.stable(StrongPostEffect::new));
    });

    @Override
    public Codec<? extends PostEffect> getCodec() {
        return CODEC;
    }

    @Override
    public boolean shouldRender() {
        return true;
    }

    @Override
    public void beforeRender() {
    }

    @Override
    public Identifier getShaderLocation() {
        return CornerConfig.get().disableStrongShaders ? this.getFallbackShaderLocation() : this.getStrongShaderLocation();
    }

    public Identifier getStrongShaderLocation() {
        return new Identifier(shaderName.getNamespace(), "shaders/post/" + shaderName.getPath() + ".json");
    }

    public Identifier getFallbackShaderLocation() {
        return new Identifier(fallbackShaderName.getNamespace(), "shaders/post/" + fallbackShaderName.getPath() + ".json");
    }

}
