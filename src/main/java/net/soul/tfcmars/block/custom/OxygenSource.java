package net.soul.tfcmars.block.custom;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface OxygenSource
{
    default BlockEntity getBlockEntity()
    {
        return (BlockEntity) this;
    }

    boolean isSupplyingOxygen();
}
