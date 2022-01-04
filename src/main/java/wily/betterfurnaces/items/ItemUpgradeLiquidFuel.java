package wily.betterfurnaces.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
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
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.liquid").setStyle(Style.EMPTY.applyFormat((ChatFormatting.GRAY))));
    }
}
