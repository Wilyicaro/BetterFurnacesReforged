package wily.betterfurnaces.items;

import java.util.List;

import net.minecraft.util.text.TextFormatting;
import wily.betterfurnaces.BetterFurnacesReforged;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemUpgrade extends Item {

	final String tooltipKey;
	public int upgradeType;
	public ItemUpgrade(String name, String tooltipKey, int Type) {
		this.setUnlocalizedName(BetterFurnacesReforged.MODID + "." + name);
		this.setRegistryName(new ResourceLocation(BetterFurnacesReforged.MODID, name));
		this.setMaxStackSize(1);
		this.setCreativeTab(BetterFurnacesReforged.BF_TAB);
		this.tooltipKey = tooltipKey;
		upgradeType = Type;

	}
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format("info." + BetterFurnacesReforged.MODID+ ".upgrade_right_click",TextFormatting.GOLD,TextFormatting.ITALIC));
		tooltip.add(I18n.format(tooltipKey));
	}


}
