package net.ludocrypt.corners.client.mixin;

import java.util.HashMap;
import java.util.Map;

import net.ludocrypt.corners.render.DeepBookshelfRendererDto;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import net.ludocrypt.corners.TheCorners;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.util.Identifier;

@Mixin(BakedModelManager.class)
public class BakedModelManagerMixin {

	@Shadow
	@Final
	@Mutable
	private static Map<Identifier, Identifier> LAYERS_TO_LOADERS;
	static {
		Map<Identifier, Identifier> newAtli = new HashMap<Identifier, Identifier>();
		newAtli.putAll(LAYERS_TO_LOADERS);
		newAtli.put(DeepBookshelfRendererDto.DEEP_BOOKSHELF_ATLAS_TEXTURE, TheCorners.id("deep"));
		LAYERS_TO_LOADERS = Map.copyOf(newAtli);
	}

}
