package wily.betterfurnaces.items;

import java.util.List;

import net.minecraft.util.text.TextFormatting;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blocks.BlockIronFurnace;
import wily.betterfurnaces.init.ModObjects;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Special case kit to convert from vanilla furnace to Iron Furnace.
 * @author Shadows
 *
 */
public final class ItemIronKit extends ItemKit {

	public ItemIronKit() {
		super("iron_upgrade", null, null);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format("info." + BetterFurnacesReforged.MODID+ ".upgrade_shift_right_click", TextFormatting.GOLD,TextFormatting.ITALIC));
		tooltip.add(I18n.format("info.betterfurnacesreforged.kit", Blocks.FURNACE.getLocalizedName(), ModObjects.IRON_FURNACE.getLocalizedName()));
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return world.getBlockState(pos).getBlock() instanceof BlockFurnace ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
		else {
			IBlockState state = world.getBlockState(pos);
			if (!(state.getBlock() instanceof BlockFurnace)) return EnumActionResult.FAIL;
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof TileEntityFurnace) {
				NBTTagCompound tag = new NBTTagCompound();
				te.writeToNBT(tag);
				tag.removeTag("id");
				tag = convert(tag);
				((TileEntityFurnace) te).clear();
				boolean burning = state.getBlock() == Blocks.LIT_FURNACE;
				EnumFacing face = state.getValue(BlockIronFurnace.FACING);
				world.setBlockState(pos, ModObjects.IRON_FURNACE.getDefaultState().withProperty(BlockIronFurnace.FACING, face).withProperty(BlockIronFurnace.BURNING, burning));
				world.getTileEntity(pos).readFromNBT(tag);
				player.getHeldItem(hand).shrink(1);
				world.updateComparatorOutputLevel(pos, ModObjects.IRON_FURNACE);
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.FAIL;
		}
	}

	private static NBTTagCompound convert(NBTTagCompound tag) {
		NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(tag, items);
		tag.setInteger("burn_time", tag.getInteger("BurnTime"));
		tag.setInteger("fuel_length", TileEntityFurnace.getItemBurnTime(items.get(1)));
		tag.setInteger("current_cook_time", tag.getInteger("CookTime"));
		ItemStackHandler handler = new ItemStackHandler(6);
		for (int i = 0; i < 3; i++)
			handler.setStackInSlot(i, items.get(i));
		tag.setTag("inv", handler.serializeNBT());
		return tag;
	}

}
