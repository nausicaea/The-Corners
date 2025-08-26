package net.ludocrypt.limlib.impl.mixin;

import java.util.List;
import java.util.Map;

import com.llamalad7.mixinextras.sugar.Local;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Lifecycle;

import net.ludocrypt.limlib.api.LimlibRegistryHooks;
import net.ludocrypt.limlib.api.LimlibRegistryHooks.LimlibJsonRegistryHook;
import net.ludocrypt.limlib.api.LimlibRegistryHooks.LimlibRegistryHook;
import net.ludocrypt.limlib.api.LimlibRegistryProvider;
import net.ludocrypt.limlib.impl.SaveStorageSupplier;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.registry.RegistryOps;
import net.minecraft.resource.ResourceManager;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {

	@Shadow
	@Final
	@Mutable
	public static List<RegistryLoader.Entry<?>> DYNAMIC_REGISTRIES;
	static {
		List<RegistryLoader.Entry<?>> newRegistries = Lists.newArrayList();
		newRegistries.addAll(DYNAMIC_REGISTRIES);
		newRegistries.add(new RegistryLoader.Entry(LimlibRegistries.PostFx.REGISTRY_KEY, LimlibRegistries.PostFx.CODEC));
		newRegistries.add(new RegistryLoader.Entry(LimlibRegistries.DimFx.REGISTRY_KEY, LimlibRegistries.DimFx.CODEC));
		newRegistries.add(new RegistryLoader.Entry(LimlibRegistries.SndFx.REGISTRY_KEY, LimlibRegistries.SndFx.CODEC));
		newRegistries.add(new RegistryLoader.Entry(LimlibRegistries.Skyboxes.REGISTRY_KEY, LimlibRegistries.Skyboxes.CODEC));
		DYNAMIC_REGISTRIES = newRegistries;
	}

	@Inject(method = "load(Lnet/minecraft/registry/RegistryOps$RegistryInfoGetter;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Decoder;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;", shift = Shift.BEFORE, remap = false))
	private static <E> void limlib$loadRegistryContents(RegistryOps.RegistryInfoGetter infoLookup,
														ResourceManager resourceManager, RegistryKey<? extends Registry<E>> registryKey, MutableRegistry<E> registry,
														Decoder<E> decoder, Map<RegistryKey<?>, Exception> readFailures, CallbackInfo ci,
														@Local RegistryOps<JsonElement> registryOps,
														@Local(ordinal = 1) RegistryKey<E> registryKey2, @Local JsonElement jsonElement) {

		if (registryKey2.isOf(RegistryKeys.WORLD_PRESET)) {
			JsonObject presetType = jsonElement.getAsJsonObject();
			JsonObject dimensions = presetType.get("dimensions").getAsJsonObject();
			LimlibRegistries.Worlds.REGISTRY
				.getEntrySet()
				.forEach((world) -> dimensions
					.add(world.getKey().getValue().toString(),
						DimensionOptions.CODEC
							.encodeStart(registryOps,
								world.getValue().dimensionOptionsSupplier().apply(new LimlibRegistryProvider() {

									@Override
									public <T> RegistryEntryLookup<T> get(RegistryKey<Registry<T>> key) {
										return registryOps.getEntryLookup(key).get();
									}

								}))
							.result()
							.get()));
		}

		LimlibRegistryHooks.REGISTRY_JSON_HOOKS
			.getOrDefault(registryKey, Sets.newHashSet())
			.forEach((registrarhook -> ((LimlibJsonRegistryHook<E>) registrarhook)
				.register(infoLookup, registryKey, registryOps, jsonElement)));
	}

	@Inject(method = "load(Lnet/minecraft/registry/RegistryOps$RegistryInfoGetter;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V", at = @At("TAIL"))
	private static <E> void limlib$loadRegistryContents(RegistryOps.RegistryInfoGetter infoLookup,
			ResourceManager resourceManager, RegistryKey<? extends Registry<E>> registryKey, MutableRegistry<E> registry,
			Decoder<E> decoder, Map<RegistryKey<?>, Exception> readFailures, CallbackInfo ci) {

		if (registryKey.equals(RegistryKeys.DIMENSION_TYPE)) {
			LimlibRegistries.Worlds.REGISTRY
				.getEntrySet()
				.forEach((world) -> ((MutableRegistry<DimensionType>) registry)
					.add(RegistryKey.of(RegistryKeys.DIMENSION_TYPE, world.getKey().getValue()),
						world.getValue().dimensionTypeSupplier().get(), Lifecycle.stable()));
		}

		LimlibRegistryHooks.REGISTRY_HOOKS
			.getOrDefault(registryKey, Sets.newHashSet())
			.forEach((registrarhook -> ((LimlibRegistryHook<E>) registrarhook).register(infoLookup, registryKey, registry)));
	}

	@Inject(method = "load(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/DynamicRegistryManager;Ljava/util/List;)Lnet/minecraft/registry/DynamicRegistryManager$Immutable;", at = @At("TAIL"))
	private static void limlib$loadRegistriesIntoManager(ResourceManager resourceManager,
			DynamicRegistryManager registryManager, List<RegistryLoader.Entry<?>> decodingData,
			CallbackInfoReturnable<DynamicRegistryManager.Immutable> ci) {
		SaveStorageSupplier.LOADED_REGISTRY.set(registryManager.toImmutable());
	}

}
