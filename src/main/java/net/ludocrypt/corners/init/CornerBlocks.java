package net.ludocrypt.corners.init;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.ModifyEntries;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.ludocrypt.corners.TheCorners;
import net.ludocrypt.corners.block.CornerBoatItem;
import net.ludocrypt.corners.block.CornerStairsBlock;
import net.ludocrypt.corners.block.OrientableBlock;
import net.ludocrypt.corners.block.RadioBlock;
import net.ludocrypt.corners.block.RailingBlock;
import net.ludocrypt.corners.block.SkyboxGlassBlock;
import net.ludocrypt.corners.block.SkyboxGlassPaneBlock;
import net.ludocrypt.corners.block.SkyboxGlassSlabBlock;
import net.ludocrypt.corners.block.ThinPillarBlock;
import net.ludocrypt.corners.entity.CornerBoatDispensorBehavior;
import net.ludocrypt.corners.entity.CornerBoatEntity.CornerBoat;
import net.ludocrypt.corners.mixin.SignTypeAccessor;
import net.ludocrypt.corners.world.feature.GaiaSaplingGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.HangingSignBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SignBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.WallHangingSignBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.HangingSignItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.SignItem;
import net.minecraft.item.TallBlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.Direction;

public class CornerBlocks {

	public static final Block STONE_PILLAR = get("stone_pillar",
		new ThinPillarBlock(FabricBlockSettings.copyOf(Blocks.STONE_BRICKS)));
	public static final RadioBlock GROWN_RADIO = get("grown_radio",
		new RadioBlock(null, null, FabricBlockSettings.copyOf(Blocks.OAK_WOOD)));
	public static final RadioBlock BROKEN_RADIO = get("broken_radio",
		new RadioBlock(null, GROWN_RADIO, FabricBlockSettings.copyOf(Blocks.OAK_WOOD)));
	public static final RadioBlock WOODEN_RADIO = get("wooden_radio",
		new RadioBlock(Items.GOLD_INGOT, BROKEN_RADIO, FabricBlockSettings.copyOf(Blocks.OAK_WOOD)));
	public static final RadioBlock TUNED_RADIO = get("tuned_radio",
		new RadioBlock(Items.AMETHYST_SHARD, BROKEN_RADIO, FabricBlockSettings.copyOf(Blocks.OAK_WOOD)));
	public static final Block DRYWALL = get("drywall", new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)));
	public static final Block NYLON_FIBER_BLOCK = get("nylon_fiber_block",
		new Block(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL)));
	public static final Block NYLON_FIBER_STAIRS = get("nylon_fiber_stairs",
		new CornerStairsBlock(NYLON_FIBER_BLOCK.getDefaultState(), FabricBlockSettings.copyOf(Blocks.WHITE_WOOL)));
	public static final Block NYLON_FIBER_SLAB = get("nylon_fiber_slab",
		new SlabBlock(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL)));
	public static final Block SNOWY_GLASS = get("snowy_glass",
		new SkyboxGlassBlock(FabricBlockSettings.copyOf(Blocks.GLASS).luminance(3)));
	public static final Block SNOWY_GLASS_PANE = get("snowy_glass_pane",
		new SkyboxGlassPaneBlock(FabricBlockSettings.copyOf(Blocks.GLASS_PANE).luminance(3)));
	public static final Block SNOWY_GLASS_SLAB = get("snowy_glass_slab",
		new SkyboxGlassSlabBlock(FabricBlockSettings.copyOf(Blocks.GLASS).luminance(3)));
	public static final Block DARK_RAILING = get("dark_railing", new RailingBlock(FabricBlockSettings.copyOf(Blocks.STONE)));
	public static final Block DEEP_BOOKSHELF = get("deep_bookshelf",
		new ChiseledBookshelfBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD)));
	// Gaia
	public static final BlockSetType GAIA_SET_TYPE = BlockSetTypeBuilder
		.copyOf(BlockSetType.SPRUCE)
		.build(TheCorners.id("gaia"));
	public static final WoodType GAIA_SIGN_TYPE = SignTypeAccessor
		.callRegister(new WoodType("corners:gaia", BlockSetType.SPRUCE));
	public static final Block GAIA_PLANKS = get("gaia_planks", new Block(FabricBlockSettings.copyOf(Blocks.SPRUCE_PLANKS)));
	public static final Block CARVED_GAIA = get("carved_gaia",
		new OrientableBlock(FabricBlockSettings.copyOf(Blocks.SPRUCE_PLANKS)));
	public static final Block GAIA_SAPLING = get("gaia_sapling", new SaplingBlock(new GaiaSaplingGenerator(),
		FabricBlockSettings.copyOf(Blocks.SPRUCE_SAPLING).mapColor(MapColor.GOLD)));
	public static final Block GAIA_LOG = get("gaia_log",
		new PillarBlock(FabricBlockSettings
			.copyOf(Blocks.SPRUCE_LOG)
			.mapColor(state -> state.get(PillarBlock.AXIS) == Direction.Axis.Y ? MapColor.SPRUCE_BROWN : MapColor.GOLD)));
	public static final Block STRIPPED_GAIA_LOG = get("stripped_gaia_log",
		new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_SPRUCE_LOG).mapColor(MapColor.SPRUCE_BROWN)));
	public static final Block GAIA_WOOD = get("gaia_wood",
		new PillarBlock(FabricBlockSettings.copyOf(Blocks.SPRUCE_WOOD).mapColor(MapColor.GOLD)));
	public static final Block STRIPPED_GAIA_WOOD = get("stripped_gaia_wood",
		new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_SPRUCE_WOOD)));
	public static final Block GAIA_LEAVES = get("gaia_leaves",
		new LeavesBlock(FabricBlockSettings.copyOf(Blocks.SPRUCE_LEAVES)));
	public static final Block GAIA_SIGN = getSingle("gaia_sign",
		new SignBlock(FabricBlockSettings.copyOf(Blocks.SPRUCE_SIGN), GAIA_SIGN_TYPE));
	public static final Block GAIA_WALL_SIGN = getSingle("gaia_wall_sign",
		new WallSignBlock(FabricBlockSettings.copyOf(Blocks.SPRUCE_WALL_SIGN).dropsLike(GAIA_SIGN), GAIA_SIGN_TYPE));
	public static final Block GAIA_HANGING_SIGN = getSingle("gaia_hanging_sign",
		new HangingSignBlock(FabricBlockSettings.copyOf(Blocks.SPRUCE_HANGING_SIGN), GAIA_SIGN_TYPE));
	public static final Block GAIA_WALL_HANGING_SIGN = getSingle("gaia_wall_hanging_sign", new WallHangingSignBlock(
		FabricBlockSettings.copyOf(Blocks.SPRUCE_WALL_HANGING_SIGN).dropsLike(GAIA_HANGING_SIGN), GAIA_SIGN_TYPE));
	public static final Block GAIA_PRESSURE_PLATE = get("gaia_pressure_plate",
		new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING,
			FabricBlockSettings.copyOf(Blocks.SPRUCE_PRESSURE_PLATE), GAIA_SET_TYPE));
	public static final Block GAIA_TRAPDOOR = get("gaia_trapdoor",
		new TrapdoorBlock(FabricBlockSettings.copyOf(Blocks.SPRUCE_TRAPDOOR), GAIA_SET_TYPE));
	public static final Block POTTED_GAIA_SAPLING = getSingle("potted_gaia_sapling",
		Blocks.createFlowerPotBlock(GAIA_SAPLING));
	public static final Block GAIA_BUTTON = get("gaia_button", Blocks.createWoodenButtonBlock(GAIA_SET_TYPE));
	public static final Block GAIA_STAIRS = get("gaia_stairs",
		new StairsBlock(GAIA_PLANKS.getDefaultState(), FabricBlockSettings.copyOf(GAIA_PLANKS)));
	public static final Block GAIA_SLAB = get("gaia_slab", new SlabBlock(FabricBlockSettings.copyOf(Blocks.SPRUCE_SLAB)));
	public static final Block GAIA_FENCE_GATE = get("gaia_fence_gate",
		new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.SPRUCE_FENCE_GATE), GAIA_SIGN_TYPE));
	public static final Block GAIA_FENCE = get("gaia_fence", new FenceBlock(FabricBlockSettings.copyOf(Blocks.SPRUCE_FENCE)));
	public static final Block GAIA_DOOR = getSingle("gaia_door",
		new DoorBlock(FabricBlockSettings.copyOf(Blocks.SPRUCE_DOOR), GAIA_SET_TYPE));
	public static final Item GAIA_BOAT = get("gaia_boat",
		new CornerBoatItem(false, CornerBoat.GAIA, new Item.Settings().maxCount(1)));
	public static final Item GAIA_CHEST_BOAT = get("gaia_chest_boat",
		new CornerBoatItem(true, CornerBoat.GAIA, new Item.Settings().maxCount(1)));
	public static final Item GAIA_SIGN_ITEM = get("gaia_sign",
		new SignItem(new Item.Settings().maxCount(16), GAIA_SIGN, GAIA_WALL_SIGN));
	public static final Item GAIA_HANGING_SIGN_ITEM = get("gaia_hanging_sign",
		new HangingSignItem(GAIA_HANGING_SIGN, GAIA_WALL_HANGING_SIGN, new Item.Settings().maxCount(16)));
	public static final Item GAIA_DOOR_ITEM = get("gaia_door", new TallBlockItem(GAIA_DOOR, new Item.Settings()));

	public static void init() {
        TheCorners.LOGGER.debug("Registering custom blocks");
		DispenserBlock.registerBehavior(GAIA_BOAT, new CornerBoatDispensorBehavior(CornerBoat.GAIA, false));
		DispenserBlock.registerBehavior(GAIA_CHEST_BOAT, new CornerBoatDispensorBehavior(CornerBoat.GAIA, true));

        FlammableBlockRegistry.getDefaultInstance().add(NYLON_FIBER_BLOCK, 30, 60);
		FlammableBlockRegistry.getDefaultInstance().add(NYLON_FIBER_STAIRS, 30, 60);
		FlammableBlockRegistry.getDefaultInstance().add(NYLON_FIBER_SLAB, 30, 60);
		FlammableBlockRegistry.getDefaultInstance().add(DRYWALL, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(WOODEN_RADIO, 10, 20);
		FlammableBlockRegistry.getDefaultInstance().add(TUNED_RADIO, 10, 20);
		FlammableBlockRegistry.getDefaultInstance().add(BROKEN_RADIO, 10, 20);
		FlammableBlockRegistry.getDefaultInstance().add(GROWN_RADIO, 10, 20);
		FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_GAIA_LOG, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_GAIA_WOOD, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(GAIA_LOG, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(GAIA_WOOD, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(GAIA_STAIRS, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(GAIA_SLAB, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(GAIA_PLANKS, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(CARVED_GAIA, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(GAIA_FENCE, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(GAIA_FENCE_GATE, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(GAIA_LEAVES, 30, 60);

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(new ModifyEntries() {

			@Override
			public void modifyEntries(FabricItemGroupEntries entries) {
				entries.add(STONE_PILLAR);
				entries.add(DARK_RAILING);
				entries.add(DRYWALL);
				entries.add(NYLON_FIBER_BLOCK);
				entries.add(NYLON_FIBER_STAIRS);
				entries.add(NYLON_FIBER_SLAB);
				entries.add(CARVED_GAIA);
				entries
					.addAfter(Items.CHERRY_BUTTON, GAIA_LOG, GAIA_WOOD, STRIPPED_GAIA_LOG, STRIPPED_GAIA_WOOD, GAIA_PLANKS,
						GAIA_STAIRS, GAIA_SLAB, GAIA_FENCE, GAIA_FENCE_GATE, GAIA_DOOR_ITEM, GAIA_TRAPDOOR,
						GAIA_PRESSURE_PLATE, GAIA_BUTTON);
			}

		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register(new ModifyEntries() {

			@Override
			public void modifyEntries(FabricItemGroupEntries entries) {
				entries.addAfter(Items.PINK_STAINED_GLASS_PANE, SNOWY_GLASS, SNOWY_GLASS_PANE, SNOWY_GLASS_SLAB);
			}

		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(new ModifyEntries() {

			@Override
			public void modifyEntries(FabricItemGroupEntries entries) {
				entries.addAfter(Items.CHISELED_BOOKSHELF, DEEP_BOOKSHELF);
				entries.add(WOODEN_RADIO);
				entries.add(TUNED_RADIO);
				entries.add(BROKEN_RADIO);
				entries.add(GROWN_RADIO);
			}

		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(new ModifyEntries() {

			@Override
			public void modifyEntries(FabricItemGroupEntries entries) {
				entries.addAfter(Items.CHERRY_LOG, GAIA_LOG);
				entries.addAfter(Items.CHERRY_LEAVES, GAIA_LEAVES);
				entries.addAfter(Items.CHERRY_SAPLING, GAIA_SAPLING);
			}

		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(new ModifyEntries() {

			@Override
			public void modifyEntries(FabricItemGroupEntries entries) {
				entries.addAfter(Items.CHERRY_HANGING_SIGN, GAIA_SIGN_ITEM, GAIA_HANGING_SIGN_ITEM);
				entries.addAfter(Items.CHISELED_BOOKSHELF, DEEP_BOOKSHELF);
			}

		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(new ModifyEntries() {

			@Override
			public void modifyEntries(FabricItemGroupEntries entries) {
				entries.addAfter(Items.CHERRY_CHEST_BOAT, GAIA_BOAT, GAIA_CHEST_BOAT);
			}

		});
	}

	private static <B extends Block> B getSingle(String id, B block) {
		return Registry.register(Registries.BLOCK, TheCorners.id(id), block);
	}

	private static <B extends Block> B get(String id, B block) {
		Registry.register(Registries.ITEM, TheCorners.id(id), new BlockItem(block, new FabricItemSettings()));
		return Registry.register(Registries.BLOCK, TheCorners.id(id), block);
	}

	private static <I extends Item> I get(String id, I item) {
		return Registry.register(Registries.ITEM, TheCorners.id(id), item);
	}

}
