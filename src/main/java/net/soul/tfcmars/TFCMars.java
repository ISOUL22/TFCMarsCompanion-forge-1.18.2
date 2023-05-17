package net.soul.tfcmars;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.soul.tfcmars.block.ModBlocks;
import net.soul.tfcmars.client.MarsClientEvents;
import net.soul.tfcmars.client.MarsClientForgeEvents;
import net.soul.tfcmars.item.ModItems;
import org.slf4j.Logger;

@Mod(TFCMars.MOD_ID)
public class TFCMars
{
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "tfcmars";

    public TFCMars()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(bus);
        ModBlocks.register(bus);

        bus.addListener(this::setup);

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            MarsClientEvents.init();
            MarsClientForgeEvents.init();
        }
    }

    public void setup(FMLCommonSetupEvent event)
    {

    }

    public static ResourceLocation identifier(String path)
    {
        return new ResourceLocation(MOD_ID, path);
    }

}
