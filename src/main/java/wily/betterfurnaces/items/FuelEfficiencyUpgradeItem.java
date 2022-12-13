package wily.betterfurnaces.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.BetterFurnacesReforged;

import java.util.List;

public class FuelEfficiencyUpgradeItem extends UpgradeItem {

    public int getMultiplier;

    public FuelEfficiencyUpgradeItem(Properties properties, int Multiplier) {
        super(properties, 2);
        this.getMultiplier = Multiplier;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        String s;
        String i;
        if (!stack.isDamageableItem())
            i = new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.infinitely").getString();
        else i = "";
        if (getMultiplier == 2)
            s = new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.double").getString();
        else if (getMultiplier == 4)
            s = new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.quadruple").getString();
        else if (getMultiplier > 4)
            s = new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.multiple").getString();
        else s = "";
        tooltip.add(new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.fuel", s, i).setStyle(Style.EMPTY.applyFormat(TextFormatting.GRAY)));
    }

    public void inventoryTick(ItemStack stack, World world, Entity player, int slot, boolean selected) {
        super.inventoryTick(stack, world, player, slot, selected);
        if (stack.isDamageableItem() && stack.getDamageValue() >= 256) {
            stack.shrink(1);
        }
    }
}
