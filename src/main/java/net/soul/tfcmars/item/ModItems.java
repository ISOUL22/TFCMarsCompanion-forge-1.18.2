package net.soul.tfcmars.item;

import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soul.tfcmars.TFCMars;

import static net.soul.tfcmars.item.ModCreativeModeTab.*;

public class ModItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TFCMars.MOD_ID);

    public static final RegistryObject<Item> MARS_TOKEN = register("mars_token");
    public static final RegistryObject<Item> MARS_BERRIES_ITEM = register("mars_berries_item");

    public static <T extends Item> RegistryObject<T> register(String name, Supplier<T> supplier)
    {
        return ITEMS.register(name, supplier);
    }

    public static RegistryObject<Item> register(String name)
    {
        return ITEMS.register(name, () -> new Item(new Item.Properties().tab(TFCMARS_TAB)));
    }

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
