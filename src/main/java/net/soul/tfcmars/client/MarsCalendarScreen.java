package net.soul.tfcmars.client;

import java.util.Objects;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.soul.tfcmars.TFCMars;

import net.dries007.tfc.client.screen.CalendarScreen;
import net.dries007.tfc.common.container.Container;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.Month;

public class MarsCalendarScreen extends CalendarScreen
{
    public MarsCalendarScreen(CalendarScreen internal)
    {
        this(internal.getMenu(), Objects.requireNonNull(Minecraft.getInstance().player).getInventory(), Helpers.translatable("tfc.screen.calendar"));
    }

    public MarsCalendarScreen(Container container, Inventory playerInv, Component name)
    {
        super(container, playerInv, name);
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY)
    {
        font.draw(stack, title, titleLabelX, titleLabelY, 4210752);
        font.draw(stack, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 4210752);
        String season = I18n.get("tfc.tooltip.calendar_season", I18n.get(Calendars.CLIENT.getCalendarMonthOfYear().getTranslationKey(Month.Style.SEASON)));
        String date = TFCMars.getTimeAndDate(true).getString();
        String message = Helpers.translatable("tfcmars.tooltip.very_dramatic_tooltip").getString();

        font.draw(stack, season, (imageWidth - font.width(season)) / 2f, 25, 0x404040);
        font.draw(stack, date, (imageWidth - font.width(date)) / 2f, 34, 0x404040);
        font.draw(stack, message, (imageWidth - font.width(message)) / 2f, 43, 0x404040);
    }

}
