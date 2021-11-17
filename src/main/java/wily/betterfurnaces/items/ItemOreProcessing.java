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

public class ItemOreProcessing extends Item {


    public ItemOreProcessing(Properties properties) {
        super(properties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade_right_click").setStyle(Style.EMPTY.applyFormat(TextFormatting.GOLD).withItalic(true)));
        if (!stack.getItem().getRegistryName().toString().equals("ultimatefurnaces_bfr:ultimate_ore_processing_upgrade"))
        tooltip.add(new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.ores").setStyle(Style.EMPTY.applyFormat(TextFormatting.GRAY)));
        else tooltip.add(new TranslationTextComponent("tooltip.ultimate_furnaces_bfr.upgrade.ultore").setStyle(Style.EMPTY.applyFormat(TextFormatting.GRAY)));
        if (stack.getItem() != Registration.ORE_PROCESSING.get())
            tooltip.add(new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.unbreakable").setStyle(Style.EMPTY.applyFormat(TextFormatting.GRAY)));

    }
    public void inventoryTick(ItemStack stack, World world, Entity player, int slot, boolean selected) {
        super.inventoryTick(stack, world, player, slot, selected);
        if (stack.isDamageableItem() && stack.getDamageValue() >= 128){
            stack.shrink(1);
        }
    }
}
