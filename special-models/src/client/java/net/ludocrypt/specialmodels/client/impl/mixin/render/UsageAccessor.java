package net.ludocrypt.specialmodels.impl.mixin.render;

import net.minecraft.client.gl.VertexBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VertexBuffer.Usage.class)
public interface UsageAccessor {

	@Accessor
	int getId();

}
