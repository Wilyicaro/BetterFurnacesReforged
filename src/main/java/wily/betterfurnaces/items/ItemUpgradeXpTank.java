package wily.betterfurnaces.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.Config;

import javax.annotation.Nullable;
import java.util.List;

public class ItemUpgradeXpTank extends ItemUpgrade {


    public ItemUpgradeXpTank(Properties properties,String tooltip) {
        super(properties,6, tooltip);
    }
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (!isWorking())
            tooltip.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.isworking").setStyle(Style.EMPTY.applyFormat((ChatFormatting.RED))));
    }
    public static boolean  isWorking(){
        return ModList.get().isLoaded(Config.getLiquidXPMod());
    }

}
