package net.soul.tfcmars;

import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.soul.tfcmars.misc.MarsClimateModel;

import net.dries007.tfc.util.events.SelectClimateModelEvent;

public final class MarsForgeEvents
{
    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(MarsForgeEvents::onClimateModel);
    }

    public static void onClimateModel(SelectClimateModelEvent event)
    {
        if (event.level().dimension().equals(Level.OVERWORLD))
        {
            event.setModel(new MarsClimateModel());
        }
    }
}
