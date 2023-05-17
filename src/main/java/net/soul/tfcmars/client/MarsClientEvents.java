package net.soul.tfcmars.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.soul.tfcmars.block.ModBlocks;

public final class MarsClientEvents
{
    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(MarsClientEvents::setup);
    }

    public static void setup(FMLClientSetupEvent event)
    {
        final RenderType cutout = RenderType.cutout();
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.MARS_BERRIES_BLOCK.get(), cutout);
    }
}
