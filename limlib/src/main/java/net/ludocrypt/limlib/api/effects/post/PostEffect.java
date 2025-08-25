package net.ludocrypt.limlib.api.effects.post;

import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;

public abstract class PostEffect {

	public abstract Codec<? extends PostEffect> getCodec();

	public abstract boolean shouldRender();

	public abstract void beforeRender();

	public abstract Identifier getShaderLocation();

}
