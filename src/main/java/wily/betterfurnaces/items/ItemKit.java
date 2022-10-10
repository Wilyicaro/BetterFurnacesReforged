package wily.betterfurnaces.items;

import java.util.List;

import net.minecraft.util.text.TextFormatting;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blocks.BlockSmelting;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.tile.TileEntitySmeltingBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemKit extends Item {

	final BlockSmelting prev;
	final BlockSmelting next;

	public ItemKit(String name, BlockSmelting prev, BlockSmelting next) {
		this.setUnlocalizedName(BetterFurnacesReforged.MODID + "." + name);
		this.setRegistryName(new ResourceLocation(BetterFurnacesReforged.MODID, name));
		this.setMaxStackSize(1);
		this.setCreativeTab(BetterFurnacesReforged.BF_TAB);
		this.prev = prev;
		this.next = next;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format("info." + BetterFurnacesReforged.MODID+ ".upgrade_shift_right_click", TextFormatting.GOLD,TextFormatting.ITALIC));
		if (prev != null) tooltip.add(I18n.format("info.betterfurnacesreforged.kit", prev.getLocalizedName(), next.getLocalizedName()));
	}

	/**
	 * Handles conversion of the previous furnace into the next furnace.  Preserves tile data.
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (prev == null) return EnumActionResult.FAIL;
		if (world.isRemote) return world.getBlockState(pos).getBlock() == prev ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
		else {
			IBlockState state = world.getBlockState(pos);
			if (state.getBlock() != prev) return EnumActionResult.FAIL;
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof TileEntitySmeltingBase) {
				NBTTagCompound tag = new NBTTagCompound();
				te.writeToNBT(tag);
				tag.removeTag("id");
				((TileEntitySmeltingBase) te).clear();
				boolean burning = state == ((TileEntitySmeltingBase) te).getLitState();
				EnumFacing face = state.getValue(BlockSmelting.FACING);
				world.setBlockState(pos, next.getDefaultState().withProperty(BlockSmelting.FACING, face).withProperty(BlockSmelting.BURNING, burning));
				world.getTileEntity(pos).readFromNBT(tag);
				player.getHeldItem(hand).shrink(1);
				world.updateComparatorOutputLevel(pos, ModObjects.IRON_FURNACE);
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.FAIL;
		}
	}

}
