package net.soul.tfcmars;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.MutableComponent;
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
import net.soul.tfcmars.misc.TFCMarsClimateModels;
import net.soul.tfcmars.world.feature.ModFeatures;
import org.slf4j.Logger;

import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.util.calendar.Month;

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
        ModFeatures.FEATURES.register(bus);

        bus.addListener(this::setup);

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            MarsClientEvents.init();
            MarsClientForgeEvents.init();
        }
        MarsForgeEvents.init();
    }

    public void setup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            TFCMarsClimateModels.registerModels();
        });
    }

    public static ResourceLocation identifier(String path)
    {
        return new ResourceLocation(MOD_ID, path);
    }

    public static long getSol(boolean isClientSide)
    {
        return Calendars.get(isClientSide).getTotalCalendarDays();
    }

    public static MutableComponent getTimeAndDate(boolean isClientSide)
    {
        final ICalendar cal = Calendars.get(isClientSide);
        final long ticks = cal.getCalendarTicks();
        final int hour = ICalendar.getHourOfDay(ticks);
        final int minute = ICalendar.getMinuteOfHour(ticks);
        final long sol = getSol(isClientSide);
        return getTimeAndDate(hour, minute, sol);
    }

    private static MutableComponent getTimeAndDate(int hour, int minute, long sol)
    {
        return Helpers.translatable("tfcmars.tooltip.exact_time", String.format("%d:%02d", hour, minute), sol);
    }

}
