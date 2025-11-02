package net.ludocrypt.corners;

import net.fabricmc.api.ModInitializer;
import net.ludocrypt.corners.render.StrongPostEffect;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.minecraft.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.ludocrypt.corners.config.CornerConfig;
import net.ludocrypt.corners.init.CornerBiomes;
import net.ludocrypt.corners.init.CornerBlocks;
import net.ludocrypt.corners.init.CornerEntities;
import net.ludocrypt.corners.init.CornerPaintings;
import net.ludocrypt.corners.init.CornerRadioRegistry;
import net.ludocrypt.corners.init.CornerSoundEvents;
import net.ludocrypt.corners.packet.ClientToServerPackets;
import net.minecraft.util.Identifier;

public class TheCorners implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("The Corners");

	public static Identifier id(String id) {
		return new Identifier("corners", id);
	}

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing the corners mod");
        AutoConfig.register(CornerConfig.class, GsonConfigSerializer::new);
        Registry.register(LimlibRegistries.PostFx.REGISTRY, TheCorners.id("strong_shader"), StrongPostEffect.CODEC);
        CornerBlocks.init();
        CornerBiomes.init();
        CornerEntities.init();
        CornerPaintings.init();
        CornerSoundEvents.init();
        CornerRadioRegistry.init();
        ClientToServerPackets.manageClientToServerPackets();
    }
}
