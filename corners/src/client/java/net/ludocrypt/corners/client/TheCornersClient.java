package net.ludocrypt.corners.client;

import net.fabricmc.api.ClientModInitializer;

public class TheCornersClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ServerToClientPackets.manageServerToClientPackets();
        BlockRenderLayerMap
                .put(RenderLayer.getCutout(), CornerBlocks.SNOWY_GLASS_PANE, CornerBlocks.SNOWY_GLASS,
                        CornerBlocks.SNOWY_GLASS_SLAB, CornerBlocks.GAIA_DOOR, CornerBlocks.GAIA_TRAPDOOR, CornerBlocks.GAIA_SAPLING,
                        CornerBlocks.POTTED_GAIA_SAPLING);
        EntityRendererRegistryImpl.register(CornerEntities.DIMENSIONAL_PAINTING_ENTITY, PaintingEntityRenderer::new);
//		EntityRendererRegistryImpl.register(CornerEntities.CORVUS_ENTITY, CorvusEntityRenderer::new);
//		EntityModelLayerRegistry.registerModelLayer(CorvusEntityModel.LAYER_LOCATION, () -> CorvusEntityModel.createBodyLayer());
        EntityRendererRegistry
                .register(CornerBoat.GAIA.entityType(false),
                        context -> new CornerBoatEntityRenderer(context, false, CornerBoat.GAIA));
        EntityModelLayerRegistry
                .registerModelLayer(CornerBoatEntityRenderer.getModelLayer(CornerBoat.GAIA, false),
                        () -> BoatEntityModel.getTexturedModelData());
        EntityRendererRegistry
                .register(CornerBoat.GAIA.entityType(true),
                        context -> new CornerBoatEntityRenderer(context, true, CornerBoat.GAIA));
        EntityModelLayerRegistry
                .registerModelLayer(CornerBoatEntityRenderer.getModelLayer(CornerBoat.GAIA, true),
                        () -> ChestBoatEntityModel.getTexturedModelData());
    }
}
