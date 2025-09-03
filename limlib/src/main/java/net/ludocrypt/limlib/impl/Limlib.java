package net.ludocrypt.limlib.impl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ludocrypt.limlib.api.LimlibRegistrar;
import net.ludocrypt.limlib.api.LimlibWorld;
import net.ludocrypt.limlib.impl.debug.DebugNbtChunkGenerator;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Limlib implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("Limlib");

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing liminal library");
		SharedMutableState.init();
		LimlibRegistries.init();

		LOGGER.debug("Registering the debug chunk generator");
		Registry.register(Registries.CHUNK_GENERATOR,
			new Identifier("limlib", "debug_nbt_chunk_generator"),
				DebugNbtChunkGenerator.CODEC);

		LOGGER.debug("Registering the custom mod entrypoint");
		FabricLoader.getInstance()
			.getEntrypoints(LimlibRegistrar.ENTRYPOINT_KEY, LimlibRegistrar.class)
			.forEach(LimlibRegistrar::registerHooks);
	}

}
