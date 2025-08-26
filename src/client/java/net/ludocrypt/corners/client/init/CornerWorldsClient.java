package net.ludocrypt.corners.client.init;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Lifecycle;
import net.ludocrypt.corners.TheCorners;
import net.ludocrypt.corners.init.CornerSoundEvents;
import net.ludocrypt.limlib.api.LimlibRegistrar;
import net.ludocrypt.limlib.api.LimlibRegistryHooks;
import net.ludocrypt.limlib.api.effects.sky.DimensionEffectsDto;
import net.ludocrypt.limlib.api.effects.sky.SkyTypeDto;
import net.ludocrypt.limlib.api.effects.sky.StaticDimensionEffectsDto;
import net.ludocrypt.limlib.api.effects.sound.reverb.StaticReverbEffectDto;
import net.ludocrypt.limlib.api.skybox.SkyboxDto;
import net.ludocrypt.limlib.api.skybox.TexturedSkyboxDto;
import net.ludocrypt.limlib.impl.LimlibRegistries;
import net.ludocrypt.limlib.impl.effects.sound.SoundEffectsDto;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.MusicSound;

import java.util.List;
import java.util.Optional;

import static net.ludocrypt.corners.init.CornerWorlds.*;

public class CornerWorldsClient implements LimlibRegistrar {
    private static final List<Pair<RegistryKey<SoundEffectsDto>, SoundEffectsDto>> SOUND_EFFECTS = Lists.newArrayList();
    private static final List<Pair<RegistryKey<SkyboxDto>, SkyboxDto>> SKYBOXES = Lists.newArrayList();
    private static final List<Pair<RegistryKey<DimensionEffectsDto>, DimensionEffectsDto>> DIMENSION_EFFECTS = Lists
            .newArrayList();

    @Override
    public void registerHooks() {
        // Sound Effects
        get(YEARNING_CANAL, new SoundEffectsDto(Optional.of(new StaticReverbEffectDto.Builder().setDecayTime(20.0F).build()),
                Optional.empty(), Optional.of(new MusicSound(CornerSoundEvents.MUSIC_YEARNING_CANAL, 3000, 8000, true))));
        get(COMMUNAL_CORRIDORS,
                new SoundEffectsDto(Optional.of(new StaticReverbEffectDto.Builder().setDecayTime(2.15F).setDensity(0.0725F).build()),
                        Optional.empty(), Optional.empty()));
        get(HOARY_CROSSROADS,
                new SoundEffectsDto(Optional.of(new StaticReverbEffectDto.Builder().setDecayTime(15.0F).setDensity(1.0F).build()),
                        Optional.empty(), Optional.of(new MusicSound(CornerSoundEvents.MUSIC_HOARY_CROSSROADS, 3000, 8000, true))));

        // Skyboxes
        get(YEARNING_CANAL, new TexturedSkyboxDto(TheCorners.id("textures/sky/yearning_canal")));
        get(COMMUNAL_CORRIDORS, new TexturedSkyboxDto(TheCorners.id("textures/sky/snow")));
        get(HOARY_CROSSROADS, new TexturedSkyboxDto(TheCorners.id("textures/sky/hoary_crossroads")));

        // Sky Effects
        get(YEARNING_CANAL, new StaticDimensionEffectsDto(Float.NaN, false, SkyTypeDto.NONE, true, false, false, 1.0F));
        get(COMMUNAL_CORRIDORS, new StaticDimensionEffectsDto(Float.NaN, false, SkyTypeDto.NONE, true, false, false, 1.0F));
        get(HOARY_CROSSROADS, new StaticDimensionEffectsDto(Float.NaN, false, SkyTypeDto.NONE, true, false, true, 1.0F));

        // Registries
        LimlibRegistryHooks
                .hook(LimlibRegistries.SndFx.REGISTRY_KEY, (infoLookup, registryKey, registry) -> SOUND_EFFECTS
                        .forEach((pair) -> registry.add(pair.getFirst(), pair.getSecond(), Lifecycle.stable())));
        LimlibRegistryHooks
                .hook(LimlibRegistries.Skyboxes.REGISTRY_KEY, (infoLookup, registryKey, registry) -> SKYBOXES
                        .forEach((pair) -> registry.add(pair.getFirst(), pair.getSecond(), Lifecycle.stable())));
        LimlibRegistryHooks
                .hook(LimlibRegistries.DimFx.REGISTRY_KEY, (infoLookup, registryKey, registry) -> DIMENSION_EFFECTS
                        .forEach((pair) -> registry.add(pair.getFirst(), pair.getSecond(), Lifecycle.stable())));
    }

    private static <S extends SoundEffectsDto> S get(String id, S soundEffects) {
        SOUND_EFFECTS.add(Pair.of(RegistryKey.of(LimlibRegistries.SndFx.REGISTRY_KEY, TheCorners.id(id)), soundEffects));
        return soundEffects;
    }

    private static <S extends SkyboxDto> S get(String id, S skybox) {
        SKYBOXES.add(Pair.of(RegistryKey.of(LimlibRegistries.Skyboxes.REGISTRY_KEY, TheCorners.id(id)), skybox));
        return skybox;
    }

    private static <D extends DimensionEffectsDto> D get(String id, D dimensionEffects) {
        DIMENSION_EFFECTS
                .add(Pair.of(RegistryKey.of(LimlibRegistries.DimFx.REGISTRY_KEY, TheCorners.id(id)), dimensionEffects));
        return dimensionEffects;
    }

}
