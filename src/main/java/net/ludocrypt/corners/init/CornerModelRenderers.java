package net.ludocrypt.corners.init;

import net.ludocrypt.corners.TheCorners;
import net.ludocrypt.corners.render.ChristmasRendererDto;
import net.ludocrypt.corners.render.DeepBookshelfRendererDto;
import net.ludocrypt.corners.render.SkyboxRendererDto;
import net.ludocrypt.specialmodels.api.SpecialModelRendererDto;
import net.ludocrypt.specialmodels.impl.SpecialModelsRegistries;
import net.minecraft.registry.Registry;

public class CornerModelRenderers {

	public static final SpecialModelRendererDto SNOWY_SKYBOX_RENDERER = register("snowy_skybox", new ChristmasRendererDto("snow"));
	public static final SpecialModelRendererDto OFFICE_SKYBOX_RENDERER = register("office_skybox", new SkyboxRendererDto("office"));
	public static final SpecialModelRendererDto SUNBEACH_SKYBOX_RENDERER = register("sunbeach_skybox",
		new SkyboxRendererDto("sunbeach"));
	public static final SpecialModelRendererDto DEEP_BOOKSHELF = register("deep_bookshelf", new DeepBookshelfRendererDto());

	public static void init() {
        TheCorners.LOGGER.info("Registering custom renderers");
	}

	private static <S extends SpecialModelRendererDto> S register(String id, S modelRenderer) {
		Registry.register(SpecialModelsRegistries.Renderer.REGISTRY, TheCorners.id(id), modelRenderer.getCodec());
        return modelRenderer;
	}

}
