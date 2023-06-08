package net.soul.tfcmars.capability.player;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import net.soul.tfcmars.network.MarsPacketHandler;
import net.soul.tfcmars.network.PlayerUpdatePacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MarsPlayerData implements ICapabilitySerializable<CompoundTag>
{
    private final Player player;
    private final LazyOptional<MarsPlayerData> cap;
    private int oxygenTicks = 0;
    private boolean indoors = false;

    public MarsPlayerData(Player player)
    {
        this.player = player;
        this.cap = LazyOptional.of(() -> this);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        if (cap == MarsPlayerDataCapability.CAPABILITY)
        {
            return this.cap.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT()
    {
        final CompoundTag nbt = new CompoundTag();
        nbt.putInt("oxygen", oxygenTicks);
        nbt.putBoolean("indoors", indoors);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        oxygenTicks = nbt.getInt("oxygen");
        indoors = nbt.getBoolean("indoors");
    }

    public void updateFromPacket(int oxygen, boolean indoors)
    {
        this.oxygenTicks = oxygen;
        this.indoors = indoors;
    }

    public void setOxygenTicks(int oxygenTicks)
    {
        this.oxygenTicks = oxygenTicks;
        sync();
    }

    public void setIndoors(boolean indoors)
    {
        this.indoors = indoors;
        sync();
    }

    public int getOxygenTicks()
    {
        return oxygenTicks;
    }

    public boolean isIndoors()
    {
        return indoors;
    }

    public void sync()
    {
        if (player instanceof final ServerPlayer serverPlayer)
        {
            MarsPacketHandler.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PlayerUpdatePacket(oxygenTicks, indoors));
        }
    }
}
