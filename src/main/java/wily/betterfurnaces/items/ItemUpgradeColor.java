package wily.betterfurnaces.items;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.handler.GuiColor;

public class ItemUpgradeColor extends ItemUpgrade {

	final String tooltipKey;

	public ItemUpgradeColor(String name, String tooltipKey) {
		super(name, tooltipKey,4);
		this.tooltipKey = tooltipKey;

	}
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entity, EnumHand hand) {
		ActionResult<ItemStack> ar = super.onItemRightClick(world, entity, hand);
		ItemStack itemstack = ar.getResult();
		int x = (int) entity.posX;
		int y = (int) entity.posY;
		int z = (int) entity.posZ;
		if (world.isRemote && !entity.isSneaking())
			(entity).openGui(BetterFurnacesReforged.INSTANCE, GuiColor.GUIID, world, x, y, z);
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
	}


}
