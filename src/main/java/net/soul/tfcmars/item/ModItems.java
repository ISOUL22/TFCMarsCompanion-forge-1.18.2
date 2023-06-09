package net.soul.tfcmars.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soul.tfcmars.TFCMars;
import net.soul.tfcmars.item.custom.T3SuitItem;

import java.util.function.Supplier;

import static net.soul.tfcmars.item.ModCreativeModeTab.TFCMARS_TAB;

public class ModItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TFCMars.MOD_ID);

    public static final RegistryObject<Item> MARS_TOKEN = register("mars_token");
    public static final RegistryObject<Item> MARS_BERRY = register("mars_berry");
    public static final RegistryObject<Item> METEORITE_CHUNK = register("meteorite_chunk");

    public static final RegistryObject<Item> TIER3_HELMET = ITEMS.register("tier3_helmet",
            () -> new T3SuitItem(MarsArmorMaterials.TIER3SUIT, EquipmentSlot.HEAD,
                    new Item.Properties().tab(TFCMARS_TAB)));
    public static final RegistryObject<Item> TIER3_SUIT = ITEMS.register("tier3_suit",
            () -> new T3SuitItem(MarsArmorMaterials.TIER3SUIT, EquipmentSlot.CHEST,
                    new Item.Properties().tab(TFCMARS_TAB)));
    public static final RegistryObject<Item> TIER3_LEGGING = ITEMS.register("tier3_leggings",
            () -> new T3SuitItem(MarsArmorMaterials.TIER3SUIT, EquipmentSlot.LEGS,
                    new Item.Properties().tab(TFCMARS_TAB)));
    public static final RegistryObject<Item> TIER3_BOOTS = ITEMS.register("tier3_boots",
            () -> new T3SuitItem(MarsArmorMaterials.TIER3SUIT, EquipmentSlot.FEET,
                    new Item.Properties().tab(TFCMARS_TAB)));
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
