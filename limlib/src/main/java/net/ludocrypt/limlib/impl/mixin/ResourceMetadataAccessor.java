package net.ludocrypt.limlib.impl.mixin;

import com.google.gson.JsonObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net/minecraft/resource/metadata/ResourceMetadata$2")
public interface ResourceMetadataAccessor {
	@Accessor(value = "field_38689")
	JsonObject getJsonObject();
}
