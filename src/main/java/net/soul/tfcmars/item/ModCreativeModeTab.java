package net.soul.tfcmars.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab
{
    public static final CreativeModeTab TFCMARS_TAB = new CreativeModeTab("tfcmarstab")
    {
        @Override
        public ItemStack makeIcon()
        {
            return new ItemStack(ModItems.MARS_TOKEN.get());
        }
    };
}
