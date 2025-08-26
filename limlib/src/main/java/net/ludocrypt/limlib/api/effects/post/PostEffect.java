package net.ludocrypt.limlib.api.effects.post;

import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;

public interface PostEffect {

	Codec<? extends PostEffect> getCodec();

	boolean shouldRender();

	void beforeRender();

	Identifier getShaderLocation();

}
