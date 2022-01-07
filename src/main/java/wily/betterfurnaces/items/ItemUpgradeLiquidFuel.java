package wily.betterfurnaces.items;

import net.minecraft.client.util.ITooltipFlag;
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

import javax.annotation.Nullable;
import java.util.List;

public class ItemUpgradeLiquidFuel extends ItemUpgrade {


    public ItemUpgradeLiquidFuel(Properties properties) {
        super(properties,1);
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.liquid").setStyle(Style.EMPTY.applyFormat((TextFormatting.GRAY))));
    }
}
