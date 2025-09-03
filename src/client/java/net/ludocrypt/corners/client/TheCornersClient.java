package net.ludocrypt.corners.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.impl.client.rendering.EntityRendererRegistryImpl;
import net.ludocrypt.corners.TheCorners;
import net.ludocrypt.corners.client.init.CornerModelRenderers;
import net.ludocrypt.corners.client.render.CornerBoatEntityRenderer;
import net.ludocrypt.corners.entity.CornerBoatEntity;
import net.ludocrypt.corners.init.CornerBlocks;
import net.ludocrypt.corners.init.CornerEntities;
import net.ludocrypt.corners.client.packet.ServerToClientPackets;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.PaintingEntityRenderer;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.render.entity.model.ChestBoatEntityModel;

public class TheCornersClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TheCorners.LOGGER.info("Initializing the corners mod client-side");
        CornerModelRenderers.init();
        ServerToClientPackets.manageServerToClientPackets();

        TheCorners.LOGGER.debug("Assigning custom block states to a render layer");
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                CornerBlocks.SNOWY_GLASS_PANE, CornerBlocks.SNOWY_GLASS,
                CornerBlocks.SNOWY_GLASS_SLAB, CornerBlocks.GAIA_DOOR,
                CornerBlocks.GAIA_TRAPDOOR, CornerBlocks.GAIA_SAPLING,
                CornerBlocks.POTTED_GAIA_SAPLING);

        TheCorners.LOGGER.debug("Registering custom entity renderers for the dimensional paintings");
        // FIXME: better alternative? EntityRendererRegistry.register(CornerEntities.DIMENSIONAL_PAINTING_ENTITY, PaintingEntityRenderer::new);
        EntityRendererRegistryImpl.register(CornerEntities.DIMENSIONAL_PAINTING_ENTITY, PaintingEntityRenderer::new);

        // TheCorners.LOGGER.debug("Registering custom entity renderers for the crow");
        // EntityRendererRegistryImpl.register(CornerEntities.CORVUS_ENTITY, CorvusEntityRenderer::new);
        // EntityModelLayerRegistry.registerModelLayer(CorvusEntityModel.LAYER_LOCATION, () -> CorvusEntityModel.createBodyLayer());

        TheCorners.LOGGER.debug("Registering custom entity renderers for custom boat entity");
        EntityRendererRegistry
                .register(CornerBoatEntity.CornerBoat.GAIA.entityType(false),
                        context -> new CornerBoatEntityRenderer(context, false, CornerBoatEntity.CornerBoat.GAIA));
        EntityModelLayerRegistry
                .registerModelLayer(CornerBoatEntityRenderer.getModelLayer(CornerBoatEntity.CornerBoat.GAIA, false),
                        () -> BoatEntityModel.getTexturedModelData());
        EntityRendererRegistry
                .register(CornerBoatEntity.CornerBoat.GAIA.entityType(true),
                        context -> new CornerBoatEntityRenderer(context, true, CornerBoatEntity.CornerBoat.GAIA));
        EntityModelLayerRegistry
                .registerModelLayer(CornerBoatEntityRenderer.getModelLayer(CornerBoatEntity.CornerBoat.GAIA, true),
                        () -> ChestBoatEntityModel.getTexturedModelData());
    }
}
