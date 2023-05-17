package net.soul.tfcmars.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soul.tfcmars.TFCMars;
import net.soul.tfcmars.block.custom.MarsBerriesBlock;
import net.soul.tfcmars.item.ModItems;

import java.util.function.Supplier;

import static net.soul.tfcmars.item.ModCreativeModeTab.*;

public class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TFCMars.MOD_ID);

    public static final RegistryObject<Block> BLOCK_OF_MARS_TOKEN = registerBlock("block_of_mars_token",
        () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
            .strength(9f).requiresCorrectToolForDrops()), TFCMARS_TAB);
    public static final RegistryObject<Block> MARS_BERRIES_BLOCK = registerBlock("mars_berries_block",
        () -> new MarsBerriesBlock(BlockBehaviour.Properties.of(Material.STONE)
            .strength(1f).requiresCorrectToolForDrops()), TFCMARS_TAB);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab)
    {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab)
    {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }
}
