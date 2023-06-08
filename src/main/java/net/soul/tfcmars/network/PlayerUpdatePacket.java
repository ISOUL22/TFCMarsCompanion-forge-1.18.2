package net.soul.tfcmars.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.soul.tfcmars.capability.player.MarsPlayerDataCapability;

import net.dries007.tfc.client.ClientHelpers;

public class PlayerUpdatePacket
{
    private final int oxygenTicks;
    private final boolean indoors;

    public PlayerUpdatePacket(int oxygenTicks, boolean indoors)
    {
        this.oxygenTicks = oxygenTicks;
        this.indoors = indoors;
    }

    PlayerUpdatePacket(FriendlyByteBuf buffer)
    {
        oxygenTicks = buffer.readInt();
        indoors = buffer.readBoolean();
    }

    void encode(FriendlyByteBuf buffer)
    {
        buffer.writeVarInt(oxygenTicks);
        buffer.writeBoolean(indoors);
    }

    void handle(NetworkEvent.Context context)
    {
        context.enqueueWork(() -> {
            final Player player = ClientHelpers.getPlayer();
            if (player != null)
            {
                player.getCapability(MarsPlayerDataCapability.CAPABILITY).ifPresent(p -> p.updateFromPacket(oxygenTicks, indoors));
            }
        });
    }
}
