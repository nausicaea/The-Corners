package net.ludocrypt.corners.client.init;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Lifecycle;
import net.ludocrypt.corners.TheCorners;
import net.ludocrypt.corners.init.CornerSoundEvents;
import net.ludocrypt.limlib.api.LimlibRegistrar;
import net.ludocrypt.limlib.api.LimlibRegistryHooks;
import net.ludocrypt.limlib.client.api.effects.sky.DimensionEffects;
import net.ludocrypt.limlib.client.api.effects.sky.StaticDimensionEffects;
import net.ludocrypt.limlib.client.api.effects.sound.SoundEffects;
import net.ludocrypt.limlib.client.api.effects.sound.reverb.StaticReverbEffect;
import net.ludocrypt.limlib.client.api.skybox.Skybox;
import net.ludocrypt.limlib.client.api.skybox.TexturedSkybox;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.MusicSound;

import java.util.List;
import java.util.Optional;

import static net.ludocrypt.corners.init.CornerWorlds.*;

public class CornerWorldsClient implements LimlibRegistrar {
    private static final List<Pair<RegistryKey<SoundEffects>, SoundEffects>> SOUND_EFFECTS = Lists.newArrayList();
    private static final List<Pair<RegistryKey<Skybox>, Skybox>> SKYBOXES = Lists.newArrayList();
    private static final List<Pair<RegistryKey<DimensionEffects>, DimensionEffects>> DIMENSION_EFFECTS = Lists
            .newArrayList();

    @Override
    public void registerHooks() {
        // Sound Effects
        get(YEARNING_CANAL, new SoundEffects(Optional.of(new StaticReverbEffect.Builder().setDecayTime(20.0F).build()),
                Optional.empty(), Optional.of(new MusicSound(CornerSoundEvents.MUSIC_YEARNING_CANAL, 3000, 8000, true))));
        get(COMMUNAL_CORRIDORS,
                new SoundEffects(Optional.of(new StaticReverbEffect.Builder().setDecayTime(2.15F).setDensity(0.0725F).build()),
                        Optional.empty(), Optional.empty()));
        get(HOARY_CROSSROADS,
                new SoundEffects(Optional.of(new StaticReverbEffect.Builder().setDecayTime(15.0F).setDensity(1.0F).build()),
                        Optional.empty(), Optional.of(new MusicSound(CornerSoundEvents.MUSIC_HOARY_CROSSROADS, 3000, 8000, true))));

        // Skyboxes
        get(YEARNING_CANAL, new TexturedSkybox(TheCorners.id("textures/sky/yearning_canal")));
        get(COMMUNAL_CORRIDORS, new TexturedSkybox(TheCorners.id("textures/sky/snow")));
        get(HOARY_CROSSROADS, new TexturedSkybox(TheCorners.id("textures/sky/hoary_crossroads")));

        // Sky Effects
        get(YEARNING_CANAL, new StaticDimensionEffects(Optional.empty(), false, "NONE", true, false, false, 1.0F));
        get(COMMUNAL_CORRIDORS, new StaticDimensionEffects(Optional.empty(), false, "NONE", true, false, false, 1.0F));
        get(HOARY_CROSSROADS, new StaticDimensionEffects(Optional.empty(), false, "NONE", true, false, true, 1.0F));

        // Registries
        LimlibRegistryHooks
                .hook(SoundEffects.SOUND_EFFECTS_KEY, (infoLookup, registryKey, registry) -> SOUND_EFFECTS
                        .forEach((pair) -> registry.add(pair.getFirst(), pair.getSecond(), Lifecycle.stable())));
        LimlibRegistryHooks
                .hook(Skybox.SKYBOX_KEY, (infoLookup, registryKey, registry) -> SKYBOXES
                        .forEach((pair) -> registry.add(pair.getFirst(), pair.getSecond(), Lifecycle.stable())));
        LimlibRegistryHooks
                .hook(DimensionEffects.DIMENSION_EFFECTS_KEY, (infoLookup, registryKey, registry) -> DIMENSION_EFFECTS
                        .forEach((pair) -> registry.add(pair.getFirst(), pair.getSecond(), Lifecycle.stable())));
    }

    private static <S extends SoundEffects> S get(String id, S soundEffects) {
        SOUND_EFFECTS.add(Pair.of(RegistryKey.of(SoundEffects.SOUND_EFFECTS_KEY, TheCorners.id(id)), soundEffects));
        return soundEffects;
    }

    private static <S extends Skybox> S get(String id, S skybox) {
        SKYBOXES.add(Pair.of(RegistryKey.of(Skybox.SKYBOX_KEY, TheCorners.id(id)), skybox));
        return skybox;
    }

    private static <D extends DimensionEffects> D get(String id, D dimensionEffects) {
        DIMENSION_EFFECTS
                .add(Pair.of(RegistryKey.of(DimensionEffects.DIMENSION_EFFECTS_KEY, TheCorners.id(id)), dimensionEffects));
        return dimensionEffects;
    }

}
