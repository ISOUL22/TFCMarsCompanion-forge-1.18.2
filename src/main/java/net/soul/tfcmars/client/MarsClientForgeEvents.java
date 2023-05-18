package net.soul.tfcmars.client;

import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

import net.dries007.tfc.client.screen.CalendarScreen;
import net.dries007.tfc.client.screen.ClimateScreen;

public class MarsClientForgeEvents
{
    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(MarsClientForgeEvents::onScreenOpen);

    }

    public static void onScreenOpen(ScreenOpenEvent event)
    {
        if (event.getScreen() instanceof ClimateScreen screen)
        {
            event.setScreen(new MarsClimateScreen(screen));
        }
        else if (event.getScreen() instanceof CalendarScreen screen)
        {
            event.setScreen(new MarsCalendarScreen(screen));
        }
    }
}
