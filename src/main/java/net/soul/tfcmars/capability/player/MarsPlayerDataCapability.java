package net.soul.tfcmars.capability.player;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.soul.tfcmars.TFCMars;

public class MarsPlayerDataCapability
{
    public static final Capability<MarsPlayerData> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final ResourceLocation KEY = TFCMars.identifier("player_data");
}
