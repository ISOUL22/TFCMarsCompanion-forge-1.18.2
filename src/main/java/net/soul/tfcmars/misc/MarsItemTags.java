package net.soul.tfcmars.misc;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.soul.tfcmars.TFCMars;

public class MarsItemTags
{
    public static final TagKey<Item> SPACESUIT = create("spacesuit");

    public static TagKey<Item> create(String name)
    {
        return TagKey.create(Registry.ITEM_REGISTRY, TFCMars.identifier(name));
    }
}
