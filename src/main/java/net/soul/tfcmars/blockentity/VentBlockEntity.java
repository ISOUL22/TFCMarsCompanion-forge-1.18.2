package net.soul.tfcmars.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.soul.tfcmars.block.custom.OxygenSource;
import net.soul.tfcmars.block.custom.PressurizerReceiver;
import net.soul.tfcmars.block.custom.VentBlock;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.blockentities.TFCBlockEntity;

public class VentBlockEntity extends TFCBlockEntity implements PressurizerReceiver, OxygenSource
{
    @Nullable private BlockPos pressurizerPos = null;

    public VentBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    public VentBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.VENT.get(), pos, state);
    }

    @Override
    public boolean isSupplyingOxygen()
    {
        return pressurizerPos != null;
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        if (pressurizerPos != null)
        {
            tag.putLong("pressurePos", pressurizerPos.asLong());
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag)
    {
        super.loadAdditional(tag);
        pressurizerPos = tag.contains("pressurePos", Tag.TAG_LONG) ? BlockPos.of(tag.getLong("pressurePos")) : null;
    }

    @Override
    public void onLoad()
    {
        super.onLoad();
        checkPressure();
    }

    @Override
    public void onChunkUnloaded()
    {
        super.onChunkUnloaded();
    }

    public void checkPressure()
    {
        assert level != null;
        if (pressurizerPos != null && !checkForPressurizer(level, pressurizerPos))
        {
            setValid(level, pressurizerPos, false);
        }
    }

    @Override
    public void setValid(Level level, BlockPos pressurizerPos, boolean valid)
    {
        this.pressurizerPos = valid ? pressurizerPos : null;
        updateState(valid);
        markForSync();
    }

    public void updateState(boolean valid)
    {
        assert level != null;
        if (getBlockState().getValue(VentBlock.STASIS) != valid)
        {
            level.setBlockAndUpdate(worldPosition, getBlockState().setValue(VentBlock.STASIS, valid));
        }
    }
}
