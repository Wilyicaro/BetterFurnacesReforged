package wily.betterfurnaces.items;

import java.util.List;

import wily.betterfurnaces.BetterFurnacesReforged;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemUpDamage extends Item {

    final String tooltipKey;

    public ItemUpDamage(String name, String tooltipKey) {
        this.setMaxDamage(128);
        this.setTranslationKey(BetterFurnacesReforged.MODID + "." + name);
        this.setRegistryName(new ResourceLocation(BetterFurnacesReforged.MODID, name));
        this.setMaxStackSize(1);
        this.setCreativeTab(BetterFurnacesReforged.BF_TAB);
        this.tooltipKey = tooltipKey;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (tooltipKey != null) tooltip.add(I18n.format(tooltipKey));
    }

}
