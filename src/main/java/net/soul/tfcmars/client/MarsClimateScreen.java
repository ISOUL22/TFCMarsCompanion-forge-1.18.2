package net.soul.tfcmars.client;

import java.util.Objects;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import net.dries007.tfc.client.ClimateRenderCache;
import net.dries007.tfc.client.screen.ClimateScreen;
import net.dries007.tfc.common.container.Container;
import net.dries007.tfc.util.Helpers;

public class MarsClimateScreen extends ClimateScreen
{

    public MarsClimateScreen(ClimateScreen internal)
    {
        this(internal.getMenu(), Objects.requireNonNull(Minecraft.getInstance().player).getInventory(), Helpers.translatable("tfc.screen.climate"));
    }

    public MarsClimateScreen(Container container, Inventory playerInv, Component name)
    {
        super(container, playerInv, name);
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY)
    {
        font.draw(stack, title, titleLabelX, titleLabelY, 4210752);
        font.draw(stack, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 4210752);
        // Climate at the current player
        final float averageTemp = ClimateRenderCache.INSTANCE.getAverageTemperature();
        final float rainfall = ClimateRenderCache.INSTANCE.getRainfall();
        final float currentTemp = ClimateRenderCache.INSTANCE.getTemperature();

        drawCenteredLine(stack, Helpers.translatable("tfc.tooltip.climate_average_temperature", String.format("%.1f", averageTemp)), 25);
        drawCenteredLine(stack, Helpers.translatable("tfc.tooltip.climate_annual_rainfall", String.format("%.1f", rainfall)), 34);
        drawCenteredLine(stack, Helpers.translatable("tfc.tooltip.climate_current_temp", String.format("%.1f", currentTemp)), 43);

    }
}
