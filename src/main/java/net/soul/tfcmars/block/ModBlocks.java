package net.soul.tfcmars.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soul.tfcmars.TFCMars;
import net.soul.tfcmars.block.custom.Dust;
import net.soul.tfcmars.block.custom.MarsBerriesBlock;
import net.soul.tfcmars.block.custom.PressurizerBlock;
import net.soul.tfcmars.block.custom.VentBlock;
import net.soul.tfcmars.blockentity.ModBlockEntities;
import net.soul.tfcmars.item.ModItems;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.Helpers;

import static net.soul.tfcmars.item.ModCreativeModeTab.*;

public class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TFCMars.MOD_ID);

    public static final RegistryObject<Block> BLOCK_OF_MARS_TOKEN = register("block_of_mars_token",
        () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
            .strength(9f).requiresCorrectToolForDrops().sound(SoundType.METAL)), TFCMARS_TAB);
    public static final RegistryObject<Block> MARS_BERRIES = register("mars_berries",
        () -> new MarsBerriesBlock(BlockBehaviour.Properties.of(Material.STONE)
            .strength(1f).requiresCorrectToolForDrops().noOcclusion().sound(SoundType.STONE)), TFCMARS_TAB);
    public static final RegistryObject<Block> METEORITE = register("meteorite", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(10f).requiresCorrectToolForDrops().sound(SoundType.STONE)), TFCMARS_TAB);
    public static final RegistryObject<Block> STANDARD_CASING = register("standard_casing", () -> new Block(BlockBehaviour.Properties.of(Material.WOOL).strength(5f).sound(SoundType.WOOL)), TFCMARS_TAB);
    public static final RegistryObject<Block> PRESSURIZER = register("pressurizer", () -> new PressurizerBlock(ExtendedProperties.of(Material.METAL).strength(5f).randomTicks().sound(SoundType.METAL).blockEntity(ModBlockEntities.PRESSURIZER)), TFCMARS_TAB);
    public static final RegistryObject<Block> VENT = register("vent", () -> new VentBlock(ExtendedProperties.of(Material.METAL).strength(5f).randomTicks().sound(SoundType.METAL).blockEntity(ModBlockEntities.VENT)), TFCMARS_TAB);

    public static final Map<Dust, Map<Dust.Variant, RegistryObject<Block>>> DUSTS = Helpers.mapOfKeys(Dust.class, dust ->
        Helpers.mapOfKeys(Dust.Variant.class, variant ->
            register("soil/" + dust.name().toLowerCase(Locale.ROOT) + "_" + variant.name().toLowerCase(Locale.ROOT), () -> new Block(BlockBehaviour.Properties.of(Material.SAND).strength(5f).sound(SoundType.SAND)), TFCMARS_TAB)
        )
    );

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block)
    {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, CreativeModeTab tab)
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
