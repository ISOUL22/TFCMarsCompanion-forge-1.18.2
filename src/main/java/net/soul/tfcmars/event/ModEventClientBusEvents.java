package net.soul.tfcmars.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.soul.tfcmars.TFCMars;
import net.soul.tfcmars.entity.client.armor.T3SuitRenderer;
import net.soul.tfcmars.item.custom.T3SuitItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@SuppressWarnings("ALL")
@Mod.EventBusSubscriber(modid = TFCMars.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventClientBusEvents {
    @SubscribeEvent
    public static void registerArmorRenderers(final EntityRenderersEvent.AddLayers event) {
        GeoArmorRenderer.registerArmorRenderer(T3SuitItem.class, new T3SuitRenderer());
    }
}
