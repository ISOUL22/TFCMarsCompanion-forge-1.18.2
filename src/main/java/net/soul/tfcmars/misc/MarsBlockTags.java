package net.soul.tfcmars.misc;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.soul.tfcmars.TFCMars;

public class MarsBlockTags
{
    public static final TagKey<Block> BASE_INSULATION = create("base_insulation");

    private static TagKey<Block> create(String name)
    {
        return TagKey.create(Registry.BLOCK_REGISTRY, TFCMars.identifier(name));
    }
}
