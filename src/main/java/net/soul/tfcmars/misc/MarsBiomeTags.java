package net.soul.tfcmars.misc;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.soul.tfcmars.TFCMars;

public class MarsBiomeTags
{
    public static final TagKey<Biome> CHERT_DUST = create("chert_dust");
    public static final TagKey<Biome> SANDSTONE_DUST = create("sandstone_dust");
    public static final TagKey<Biome> CLAYSTONE_DUST = create("claystone_dust");

    private static TagKey<Biome> create(String name)
    {
        return TagKey.create(Registry.BIOME_REGISTRY, TFCMars.identifier(name));
    }

}
