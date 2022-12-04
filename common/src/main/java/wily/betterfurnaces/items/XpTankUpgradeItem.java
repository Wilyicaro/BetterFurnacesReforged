package wily.betterfurnaces.items;

import dev.architectury.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.Config;

import javax.annotation.Nullable;
import java.util.List;

public class XpTankUpgradeItem extends UpgradeItem {


    public XpTankUpgradeItem(Properties properties, String tooltip) {
        super(properties,6, tooltip);
    }

    @Override
    public boolean isEnabled() {
        return Platform.isModLoaded(Config.getLiquidXPMod());
    }
}
