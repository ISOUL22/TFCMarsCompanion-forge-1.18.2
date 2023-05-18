package net.soul.tfcmars.world.feature;

import java.util.function.Function;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soul.tfcmars.TFCMars;

@SuppressWarnings("unused")
public class ModFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, TFCMars.MOD_ID);

    public static final RegistryObject<MarsErosionFeature> EROSION = register("erosion", MarsErosionFeature::new, NoneFeatureConfiguration.CODEC);
    public static final RegistryObject<MeteorFeature> METEOR = register("meteor", MeteorFeature::new, NoneFeatureConfiguration.CODEC);
    public static final RegistryObject<MarsBoulderFeature> BOULDERS = register("boulders", MarsBoulderFeature::new, WeightedStateConfig.CODEC);
    public static final RegistryObject<MarsLooseRockFeature> LOOSE_ROCKS = register("loose_rocks", MarsLooseRockFeature::new, NoneFeatureConfiguration.CODEC);
    public static final RegistryObject<RockLayersFeature> ROCK_LAYERS = register("rock_layers", RockLayersFeature::new, NoneFeatureConfiguration.CODEC);
    public static final RegistryObject<CraterFeature> CRATER = register("crater", CraterFeature::new, NoneFeatureConfiguration.CODEC);

    private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryObject<F> register(String name, Function<Codec<C>, F> factory, Codec<C> codec)
    {
        return FEATURES.register(name, () -> factory.apply(codec));
    }

}
