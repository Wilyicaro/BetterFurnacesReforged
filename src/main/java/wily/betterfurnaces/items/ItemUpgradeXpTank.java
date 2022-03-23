package wily.betterfurnaces.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.Config;

import javax.annotation.Nullable;
import java.util.List;

public class ItemUpgradeXpTank extends ItemUpgrade {


    public ItemUpgradeXpTank(Properties properties, String tooltip) {
        super(properties,6, tooltip);
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (!isWorking())
        tooltip.add(new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.isworking").setStyle(Style.EMPTY.applyFormat((TextFormatting.RED))));
    }
    public static boolean  isWorking(){
        return ModList.get().isLoaded(Config.getLiquidXPMod());
    }

}
