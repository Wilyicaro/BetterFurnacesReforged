package wily.betterfurnaces.items;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
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
		this.setUnlocalizedName(BetterFurnacesReforged.MODID + "." + name);
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

	public void onUpdate(ItemStack stack, World world, Entity player, int slot, boolean selected) {
		super.onUpdate(stack, world, player, slot, selected);
		ItemStack itemStack = stack;
		NBTTagCompound nbt;
		if (itemStack.hasTagCompound()) {
			nbt = itemStack.getTagCompound();
		} else {
			nbt = new NBTTagCompound();
		}
		nbt.getInteger("red");
		if ((Minecraft.getMinecraft().currentScreen) instanceof GuiColor && player instanceof EntityPlayer && ((EntityPlayer) player).getHeldItemMainhand() == stack) {
			int red = ((GuiColor) Minecraft.getMinecraft().currentScreen).red.getValueInt();
			int green = ((GuiColor) Minecraft.getMinecraft().currentScreen).green.getValueInt();
			int blue = ((GuiColor) Minecraft.getMinecraft().currentScreen).blue.getValueInt();
			if (red != nbt.getInteger("red")) {
				nbt.setInteger("red", red);
			}
			if (red != nbt.getInteger("green")) {
				nbt.setInteger("green", green);
			}
			if (red != nbt.getInteger("blue")) {
				nbt.setInteger("blue", blue);
			}
			itemStack.setTagCompound(nbt);
		}
	}


}
