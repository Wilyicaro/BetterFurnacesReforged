package wily.betterfurnaces.items;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
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
		if (!(nbt.hasKey("red") && nbt.hasKey("green") && nbt.hasKey("blue"))) {
			nbt.setInteger("red", 255);
			nbt.setInteger("green", 255);
			nbt.setInteger("blue", 255);
			itemStack.setTagCompound(nbt);
		}

		if ((Minecraft.getMinecraft().currentScreen) instanceof GuiColor && player instanceof EntityPlayer && ((EntityPlayer) player).getHeldItemMainhand() == stack) {
			GuiColor color =  (GuiColor) Minecraft.getMinecraft().currentScreen;
			int red = color.red.getValueInt();
			int green = color.green.getValueInt();
			int blue = color.blue.getValueInt();
			if (color.red != null && red != nbt.getInteger("red") && color.red.dragging) {
				nbt.setInteger("red", red);
			}
			if (color.green != null && green != nbt.getInteger("green") && color.green.dragging) {
				nbt.setInteger("green", green);
			}
			if (color.blue != null && blue != nbt.getInteger("blue") && color.blue.dragging) {
				nbt.setInteger("blue", blue);
			}
			itemStack.setTagCompound(nbt);
		}
	}


}
