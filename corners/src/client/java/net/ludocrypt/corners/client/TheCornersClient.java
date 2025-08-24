package net.ludocrypt.corners.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.impl.client.rendering.EntityRendererRegistryImpl;
import net.ludocrypt.corners.TheCorners;
import net.ludocrypt.corners.client.init.CornerModelRenderers;
import net.ludocrypt.corners.client.render.CornerBoatEntityRenderer;
import net.ludocrypt.corners.client.render.StrongPostEffect;
import net.ludocrypt.corners.entity.CornerBoatEntity;
import net.ludocrypt.corners.init.CornerBlocks;
import net.ludocrypt.corners.init.CornerEntities;
import net.ludocrypt.corners.client.packet.ServerToClientPackets;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.PaintingEntityRenderer;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.render.entity.model.ChestBoatEntityModel;
import net.minecraft.registry.Registry;
import net.ludocrypt.limlib.api.effects.post.PostEffect;

public class TheCornersClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Registry.register(PostEffect.POST_EFFECT_CODEC, TheCorners.id("strong_shader"), StrongPostEffect.CODEC);
        CornerModelRenderers.init();
        ServerToClientPackets.manageServerToClientPackets();
        BlockRenderLayerMap.INSTANCE
                .putBlocks(RenderLayer.getCutout(), CornerBlocks.SNOWY_GLASS_PANE, CornerBlocks.SNOWY_GLASS,
                        CornerBlocks.SNOWY_GLASS_SLAB, CornerBlocks.GAIA_DOOR, CornerBlocks.GAIA_TRAPDOOR, CornerBlocks.GAIA_SAPLING,
                        CornerBlocks.POTTED_GAIA_SAPLING);
        EntityRendererRegistryImpl.register(CornerEntities.DIMENSIONAL_PAINTING_ENTITY, PaintingEntityRenderer::new);
//		EntityRendererRegistryImpl.register(CornerEntities.CORVUS_ENTITY, CorvusEntityRenderer::new);
//		EntityModelLayerRegistry.registerModelLayer(CorvusEntityModel.LAYER_LOCATION, () -> CorvusEntityModel.createBodyLayer());
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
