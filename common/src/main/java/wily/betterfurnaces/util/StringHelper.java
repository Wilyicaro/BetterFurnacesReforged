package wily.betterfurnaces.util;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import wily.betterfurnaces.BetterFurnacesReforged;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class StringHelper {


    public static List<Component> getShiftInfoGui()
    {
        List<Component> list = Lists.newArrayList();
        list.add(new TranslatableComponent(("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_close")));
        MutableComponent tooltip1 = new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_hold_shift");
        MutableComponent shift = new TextComponent("[Shift]");
        MutableComponent tooltip2 = new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_shift_more_options");
        tooltip1.withStyle(ChatFormatting.GRAY);
        shift.withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC);
        tooltip2.withStyle(ChatFormatting.GRAY);
        list.add(tooltip1.append(shift).append(tooltip2));
        return list;
    }

}
