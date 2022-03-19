package wily.betterfurnaces.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.BetterFurnacesReforged;

import java.util.List;

public class ItemUpgradeOreProcessing extends ItemUpgrade {

    public int getMultiplier;
    public boolean acceptOre;
    public boolean acceptRaw;
    public ItemUpgradeOreProcessing(Properties properties, int Multiplier, boolean acceptOre, boolean acceptRaw ) {
        super(properties,3, null);
        this.getMultiplier = Multiplier;
        this.acceptOre = acceptOre;
        this.acceptRaw = acceptRaw;
    }
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        String s;
        String i;
        String o;
        if (!stack.isDamageableItem())
            i = new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.infinitely").getString();
        else i = "";
        if (acceptOre && !acceptRaw) o = new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.ore").getString();
        else o = new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.raw").getString();
        if (acceptRaw && acceptOre) o = new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.allore").getString();
        if (getMultiplier == 2) s = new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.double").getString();
        else if (getMultiplier == 4) s = new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.quadruple").getString();
        else if (getMultiplier > 4) s = new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.multiple").getString();
        else s = "";
        tooltip.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.ores",s,i,o).setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
    }
    public void inventoryTick(ItemStack stack, Level world, Entity player, int slot, boolean selected) {
        super.inventoryTick(stack, world, player, slot, selected);
        if (stack.isDamageableItem() && stack.getDamageValue() >= stack.getMaxDamage()){
            stack.shrink(1);
        }
    }
}
