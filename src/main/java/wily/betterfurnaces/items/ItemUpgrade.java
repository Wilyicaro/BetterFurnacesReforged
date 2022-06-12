package wily.betterfurnaces.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.BetterFurnacesReforged;

import javax.annotation.Nullable;
import java.util.List;

public class ItemUpgrade extends Item {
private String tooltip;
public int upgradeType;
// Use a different int for each type of upgrade
    public ItemUpgrade(Properties properties, int Type, String tooltip ) {
        super(properties);
        upgradeType = Type;
        this.tooltip = tooltip;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade_right_click").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GOLD).withItalic(true)));
        if (this.tooltip != null)
        tooltip.add(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade." + this.tooltip ).setStyle(Style.EMPTY.applyFormat((ChatFormatting.GRAY))));
    }
}
