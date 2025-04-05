package wily.betterfurnaces.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import wily.betterfurnaces.BetterFurnacesReforged;

public class BFRComponents {
    public static final Component UPGRADE_RIGHT_CLICK = Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade_right_click").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GOLD).withItalic(true));
    public static final Component UPGRADE_DISABLED_MESSAGE = Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.disabled").setStyle(Style.EMPTY.applyFormat((ChatFormatting.RED)));
    public static final Component UPGRADE_SHIFT_RIGHT_CLICK = Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade_shift_right_click").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GOLD).withItalic(true));


    public static Component optionsName(String key){
        return Component.translatable("options." + BetterFurnacesReforged.MOD_ID + "." + key);
    }
}
