package net.soul.tfcmars.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.soul.tfcmars.block.ModBlocks;
import org.jetbrains.annotations.Nullable;

public interface PressurizerReceiver
{
    @Nullable
    static PressurizerReceiver get(Level level, BlockPos pos)
    {
        if (level.getBlockEntity(pos) instanceof PressurizerReceiver rec)
        {
            return rec;
        }
        if (level.getBlockState(pos) instanceof PressurizerReceiver rec)
        {
            return rec;
        }
        return null;
    }

    default boolean checkForPressurizer(Level level, BlockPos pos)
    {
        if (!level.isLoaded(pos))
        {
            return true;
        }
        final BlockState state = level.getBlockState(pos);
        return state.getBlock() == ModBlocks.PRESSURIZER.get() && state.getValue(PressurizerBlock.STASIS);
    }

    void setValid(Level level, BlockPos pressurizerPos, boolean valid);

}
