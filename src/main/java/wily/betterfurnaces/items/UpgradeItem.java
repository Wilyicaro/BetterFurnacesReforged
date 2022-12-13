package wily.betterfurnaces.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.BetterFurnacesReforged;

import javax.annotation.Nullable;
import java.util.List;

public class UpgradeItem extends Item {
    private String tooltipName;
    private ITextComponent tooltip;
    public int upgradeType;
    // Use a different int for each type of upgrade
    public UpgradeItem(Properties properties, int Type, String tooltipName ) {
        super(properties);
        upgradeType = Type;
        this.tooltipName = tooltipName;
    }
    public UpgradeItem(Properties properties, int Type) {
        super(properties);
        upgradeType = Type;
    }
    public UpgradeItem(Properties properties, int Type, ITextComponent tooltip ) {
        super(properties);
        upgradeType = Type;
        this.tooltip = tooltip;

    }
    public boolean isEnabled(){
        return true;
    }
    public ITextComponent getDisabledMessage(){
        return new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.disabled").setStyle(Style.EMPTY.applyFormat((TextFormatting.RED)));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (!isEnabled())
            tooltip.add(getDisabledMessage());
        tooltip.add(new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade_right_click").setStyle(Style.EMPTY.applyFormat(TextFormatting.GOLD).withItalic(true)));
        if (this.tooltip != null || tooltipName != null)
            tooltip.add( this.tooltip == null ? new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade." + this.tooltipName).setStyle(Style.EMPTY.applyFormat((TextFormatting.GRAY))) : this.tooltip);

    }
}
