package net.soul.tfcmars.network;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.soul.tfcmars.TFCMars;
import org.apache.commons.lang3.mutable.MutableInt;

public final class MarsPacketHandler
{
    private static final String VERSION = "0.0.1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(TFCMars.identifier("network"), () -> VERSION, VERSION::equals, VERSION::equals);
    private static final MutableInt ID = new MutableInt(0);

    public static void init()
    {
        // S -> C
        register(PlayerUpdatePacket.class, PlayerUpdatePacket::encode, PlayerUpdatePacket::new, PlayerUpdatePacket::handle);
    }

    public static void send(PacketDistributor.PacketTarget target, Object message)
    {
        CHANNEL.send(target, message);
    }

    private static <T> void register(Class<T> cls, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, NetworkEvent.Context> handler)
    {
        CHANNEL.registerMessage(ID.getAndIncrement(), cls, encoder, decoder, (packet, context) -> {
            context.get().setPacketHandled(true);
            handler.accept(packet, context.get());
        });
    }

    private static <T> void register(Class<T> cls, Supplier<T> factory, BiConsumer<T, NetworkEvent.Context> handler)
    {
        CHANNEL.registerMessage(ID.getAndIncrement(), cls, (packet, buffer) -> {}, buffer -> factory.get(), (packet, context) -> {
            context.get().setPacketHandled(true);
            handler.accept(packet, context.get());
        });
    }
}
