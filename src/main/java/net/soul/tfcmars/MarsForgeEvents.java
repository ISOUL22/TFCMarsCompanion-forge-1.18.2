package net.soul.tfcmars;

import java.util.UUID;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.soul.tfcmars.misc.MarsClimateModel;

import net.dries007.tfc.util.events.SelectClimateModelEvent;

public final class MarsForgeEvents
{
    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(MarsForgeEvents::onClimateModel);
        bus.addListener(MarsForgeEvents::onLivingFall);
        bus.addListener(MarsForgeEvents::onEntityJoinLevel);
    }

    public static final UUID MARS_JUMP_ID = UUID.fromString("ce80c1ce-3fde-4af6-b2b7-2d7681756f31");
    public static final AttributeModifier MARS_JUMP = new AttributeModifier(MARS_JUMP_ID, "mars_jump", 1.8, AttributeModifier.Operation.MULTIPLY_BASE);
    public static final UUID MARS_FALL_ID = UUID.fromString("0fdba2ed-3c75-48e5-bc6e-30f27dbc08c3");
    public static final AttributeModifier MARS_FALL = new AttributeModifier(MARS_FALL_ID, "mars_fall", -0.05, AttributeModifier.Operation.ADDITION);

    public static void onClimateModel(SelectClimateModelEvent event)
    {
        if (event.level().dimension().equals(Level.OVERWORLD))
        {
            event.setModel(new MarsClimateModel());
        }
    }

    public static void onLivingFall(LivingFallEvent event)
    {
        final LivingEntity entity = event.getEntityLiving();
        if (entity.getLevel().dimension().equals(Level.OVERWORLD))
        {
            if (event.getDistance() < 7)
            {
                event.setDamageMultiplier(0);
            }
            else
            {
                event.setDamageMultiplier(0.5f * event.getDamageMultiplier());
            }
        }
    }

    public static void onEntityJoinLevel(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof LivingEntity entity)
        {
            if (entity.getAttributes().hasAttribute(Attributes.JUMP_STRENGTH))
            {
                final AttributeInstance attr = entity.getAttribute(Attributes.JUMP_STRENGTH);
                if (attr != null)
                {
                    if (!attr.hasModifier(MARS_JUMP) && entity.getLevel().dimension().equals(Level.OVERWORLD))
                    {
                        attr.addTransientModifier(MARS_JUMP);
                    }
                    else
                    {
                        attr.removeModifier(MARS_JUMP);
                    }
                }
            }
            if (entity.getAttributes().hasAttribute(ForgeMod.ENTITY_GRAVITY.get()))
            {
                final AttributeInstance attr = entity.getAttribute(ForgeMod.ENTITY_GRAVITY.get());
                if (attr != null)
                {
                    if (!attr.hasModifier(MARS_FALL) && entity.getLevel().dimension().equals(Level.OVERWORLD))
                    {
                        attr.addTransientModifier(MARS_FALL);
                    }
                    else
                    {
                        attr.removeModifier(MARS_FALL);
                    }
                }
            }
        }
    }
}
