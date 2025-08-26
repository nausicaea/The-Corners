package net.ludocrypt.corners.world.feature;

import net.ludocrypt.corners.init.CornerBiomes;
import net.minecraft.block.BlockState;
import net.minecraft.block.sapling.LargeTreeSaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class GaiaSaplingGenerator extends LargeTreeSaplingGenerator {

	@Override
	protected RegistryKey<ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
		return null;
	}

	@Override
	protected RegistryKey<ConfiguredFeature<?, ?>> getLargeTreeFeature(Random random) {
		return CornerBiomes.CONFIGURED_SAPLING_GAIA_TREE_FEATURE;
	}

	public boolean generateRadio(ServerWorld world, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state,
			Random random) {
		RegistryEntry<ConfiguredFeature<?, ?>> holder = world
			.getRegistryManager()
			.get(RegistryKeys.CONFIGURED_FEATURE)
			.getEntry(CornerBiomes.CONFIGURED_GAIA_TREE_FEATURE)
			.orElse(null);

		if (holder == null) {
			return false;
		} else {
			return holder.value().generate(world, chunkGenerator, random, pos);
		}

	}

}
