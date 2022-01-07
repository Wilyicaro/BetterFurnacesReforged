package wily.betterfurnaces.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.init.Registration;

import java.util.List;

public class ItemUpgradeFuelEfficiency extends ItemUpgrade {

    public int getMultiplier;
    public ItemUpgradeFuelEfficiency(Properties properties, int Multiplier) {
        super(properties,2);
        this.getMultiplier = Multiplier;
    }
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.fuel").setStyle(Style.EMPTY.applyFormat(TextFormatting.GRAY)));
        if (!stack.isDamageableItem())
            tooltip.add(new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.unbreakable").setStyle(Style.EMPTY.applyFormat(TextFormatting.GRAY)));
    }
    public void inventoryTick(ItemStack stack, World world, Entity player, int slot, boolean selected) {
        super.inventoryTick(stack, world, player, slot, selected);
        if (stack.isDamageableItem() && stack.getDamageValue() >= 256){
            stack.shrink(1);
        }
    }
}
