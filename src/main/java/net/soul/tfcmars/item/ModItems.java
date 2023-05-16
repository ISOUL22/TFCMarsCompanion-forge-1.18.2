package net.soul.tfcmars.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soul.tfcmars.TFCMars;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TFCMars.MOD_ID);
        public static final RegistryObject<Item> MARS_TOKEN = ITEMS.register("mars_token",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.TFCMARS_TAB)));
        public static final RegistryObject<Item> MARS_BERRIES_ITEM = ITEMS.register("mars_berries_item",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.TFCMARS_TAB)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}