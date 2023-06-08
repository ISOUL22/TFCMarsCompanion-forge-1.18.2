package net.soul.tfcmars.blockentity;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.soul.tfcmars.block.custom.PressurizerReceiver;

import net.dries007.tfc.common.blockentities.TFCBlockEntity;

public class PressurizerBlockEntity extends TFCBlockEntity
{
    private Set<BlockPos> positions = new HashSet<>();

    public PressurizerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    public PressurizerBlockEntity(BlockPos pos, BlockState state)
    {
        this(ModBlockEntities.PRESSURIZER.get(), pos, state);
    }

    @Override
    public void loadAdditional(CompoundTag nbt)
    {
        super.loadAdditional(nbt);
        long[] array = nbt.getLongArray("positions");
        positions.clear();
        positions = new HashSet<>(array.length);
        for (long pos : array)
        {
            positions.add(BlockPos.of(pos));
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt)
    {
        super.saveAdditional(nbt);
        final long[] array = new long[positions.size()];
        int i = 0;
        for (BlockPos pos : positions)
        {
            array[i] = pos.asLong();
            i++;
        }
        nbt.putLongArray("positions", array);
    }

    public void updateValidity(boolean valid)
    {
        assert level != null;
        positions.forEach(pos -> {
            final PressurizerReceiver receiver = PressurizerReceiver.get(level, pos);
            if (receiver != null)
            {
                receiver.setValid(level, worldPosition, valid);
            }
        });
    }

    public void setPositions(Set<BlockPos> positions)
    {
        this.positions = positions;
    }

    public Set<BlockPos> getPositions()
    {
        return positions;
    }
}
