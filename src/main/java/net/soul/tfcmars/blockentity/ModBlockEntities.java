package net.soul.tfcmars.blockentity;

import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soul.tfcmars.TFCMars;
import net.soul.tfcmars.block.ModBlocks;

import net.dries007.tfc.util.registry.RegistrationHelpers;

public final class ModBlockEntities
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, TFCMars.MOD_ID);

    public static final RegistryObject<BlockEntityType<PressurizerBlockEntity>> PRESSURIZER = register("pressurizer", PressurizerBlockEntity::new, ModBlocks.PRESSURIZER);
    public static final RegistryObject<BlockEntityType<VentBlockEntity>> VENT = register("vent", VentBlockEntity::new, ModBlocks.VENT);

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, Supplier<? extends Block> block)
    {
        return RegistrationHelpers.register(BLOCK_ENTITIES, name, factory, block);
    }

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, Stream<? extends Supplier<? extends Block>> blocks)
    {
        return RegistrationHelpers.register(BLOCK_ENTITIES, name, factory, blocks);
    }
}
