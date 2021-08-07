package wily.betterfurnaces.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import wily.betterfurnaces.BetterFurnacesReforged;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wily.betterfurnaces.handler.GuiColor;
import wily.betterfurnaces.upgrade.Upgrades;

public class ItemUpgrade extends Item {

	final String tooltipKey;

	public ItemUpgrade(String name, String tooltipKey) {
		this.setTranslationKey(BetterFurnacesReforged.MODID + "." + name);
		this.setRegistryName(new ResourceLocation(BetterFurnacesReforged.MODID, name));
		this.setMaxStackSize(1);
		this.setCreativeTab(BetterFurnacesReforged.BF_TAB);
		this.tooltipKey = tooltipKey;
	}
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entity, EnumHand hand) {
		ActionResult<ItemStack> ar = super.onItemRightClick(world, entity, hand);
		ItemStack itemstack = ar.getResult();
		int x = (int) entity.posX;
		int y = (int) entity.posY;
		int z = (int) entity.posZ;
		if (Upgrades.COLOR.matches(itemstack)) {
			(entity).openGui(BetterFurnacesReforged.INSTANCE, GuiColor.GUIID, world, x, y, z);
		}
			return ar;
	}


	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (tooltipKey != null) tooltip.add(I18n.format(tooltipKey));
	}

}
