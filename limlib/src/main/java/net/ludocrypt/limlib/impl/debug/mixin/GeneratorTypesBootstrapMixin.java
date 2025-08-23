package net.ludocrypt.limlib.impl.debug.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.ludocrypt.limlib.impl.debug.DebugNbtChunkGenerator;
import net.ludocrypt.limlib.impl.debug.DebugWorld;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.WorldPreset;

@Mixin(net.minecraft.world.gen.WorldPresets.Registrar.class)
public abstract class GeneratorTypesBootstrapMixin {

	@Final
	@Shadow
	private RegistryEntryLookup<Biome> biomeLookup;

	@Final
	@Shadow
	private RegistryEntry<DimensionType> overworldDimensionType;

	@Inject(method = "bootstrap()V", at = @At("TAIL"))
	public void limlib$addDimensionOpions(CallbackInfo ci) {
		this
			.addDimensionGenerator(DebugWorld.DEBUG_KEY, new DimensionOptions(this.overworldDimensionType,
				new DebugNbtChunkGenerator(this.biomeLookup.getOrThrow(BiomeKeys.THE_VOID))));
	}

	@Unique
	abstract void addDimensionGenerator(RegistryKey<WorldPreset> generator, DimensionOptions dimension);

}
