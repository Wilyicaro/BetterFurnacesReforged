package wily.betterfurnaces.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.inventory.FContainerBF;
import wily.betterfurnaces.items.ItemUpgrade;
import wily.betterfurnaces.tile.TileEntityForge;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class BlockForge extends Block {

	public static final PropertyDirection FACING = BlockDirectional.FACING;
	public static final PropertyBool BURNING = PropertyBool.create("burning");
	public static final PropertyBool COLORED = PropertyBool.create("colored");

	protected final int moreFast;
	protected final Supplier<TileEntity> teFunc;

	/**
	 * Make a new Iron Furnace.
	 * @param name The registry name.
	 * @param moreFast The default cook time of this furnace.
	 * @param teFunc A supplier for the TE of this furnace.
	 */
	public BlockForge(String name, int moreFast, Supplier<TileEntity> teFunc) {
		super(Material.IRON);
		this.setUnlocalizedName(BetterFurnacesReforged.MODID + "." + name);
		this.setRegistryName(BetterFurnacesReforged.MODID, name);
		this.setCreativeTab(BetterFurnacesReforged.BF_TAB);
		this.setHardness(2.0F);
		this.setResistance(9.0F);
		this.setLightOpacity(0);
		this.setHarvestLevel("pickaxe", 1);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP).withProperty(BURNING, false).withProperty(COLORED, false));
		this.moreFast = moreFast;
		this.teFunc = teFunc;
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(BURNING) ? 14 : 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format("info.betterfurnacesreforged.forgemorefast", moreFast));
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return teFunc.get();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, BURNING, COLORED);
	}
	private EnumFacing ForgeFacing(int index){
		if (index > 3) index = index - 4;
		if (index == 0) return EnumFacing.DOWN;
		else if (index == 1) return EnumFacing.UP;
		else if (index == 2) return EnumFacing.NORTH;
		else return EnumFacing.WEST;
	}
	private int getForgeIndex(EnumFacing facing){
		if (facing == EnumFacing.DOWN) return 0;
		else if (facing == EnumFacing.UP) return 1;
		else if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH ) return 2;
		else  return 3;
	}
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, ForgeFacing(meta >> 2)).withProperty(BURNING, (meta & 1) == 1).withProperty(COLORED, (meta & 2) == 2);
	}

	@Override
	public int getMetaFromState(IBlockState state) {

		return getForgeIndex(state.getValue(FACING)) << 2 | (state.getValue(BURNING) ? 1 : 0) | (state.getValue(COLORED) ? 2 : 0);
	}
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty( FACING, ForgeFacing(getForgeIndex(EnumFacing.getDirectionFromEntityLiving(pos, placer))));
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirror) {
		return state.withRotation(mirror.toRotation(state.getValue(FACING)));
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityForge) {
			ItemStackHandler inv = ((TileEntityForge) te).getInventory();
			int i = 0;
			float f = 0.0F;
			for (int j = 0; j < 3; ++j) {
				ItemStack itemstack = inv.getStackInSlot(j);

				if (!itemstack.isEmpty()) {
					f += (float) itemstack.getCount() / (float) Math.min(64, itemstack.getMaxStackSize());
					++i;
				}
			}
			f = f / 3;
			return MathHelper.floor(f * 14.0F) + (i > 0 ? 1 : 0);
		}
		return 0;
	}

	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getTileEntity(pos);
		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote && player.getHeldItem(hand).getItem() instanceof ItemUpgrade  && !(player.isSneaking())) {
			return this.interactUpgrade(world, pos, player, hand, stack);
		}
		if (!world.isRemote && te instanceof TileEntityForge && ((TileEntityForge) te).isFluid()) {
			FluidStack fs = FluidUtil.getFluidContained(stack);
			if (fs != null && TileEntityForge.getFluidBurnTime(fs) > 0) {
				FluidActionResult res = FluidUtil.tryEmptyContainer(stack, FluidUtil.getFluidHandler(world, pos, null), 1000, player, true);
				if (res.isSuccess()) {
					world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.PLAYERS, 0.6F, 0.8F);
					if (!player.capabilities.isCreativeMode) player.setHeldItem(hand, res.result);
					return true;
				}
			}
		}

		if (!world.isRemote) {
			player.openGui(BetterFurnacesReforged.INSTANCE, FContainerBF.GUIID, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
	private boolean interactUpgrade(World world, BlockPos pos, EntityPlayer player, EnumHand handIn, ItemStack stack) {
		Item hand = player.getHeldItem(handIn).getItem();
		if (!(hand instanceof ItemUpgrade)){
			return false;
		}
		if (!(world.getTileEntity(pos) instanceof TileEntityForge)) {
			return false;
		}
		TileEntityForge te = (TileEntityForge) world.getTileEntity(pos);
		ItemStack newStack = new ItemStack(stack.getItem(), 1);
		newStack.setTagCompound(stack.getTagCompound());

		if (((ItemUpgrade)hand).upgradeType == 1) {
			if ((!(te.getInventory().getStackInSlot(10).isEmpty())) && (!player.isCreative())) {
				InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), te.getInventory().getStackInSlot(10));
			}
			te.getInventory().setStackInSlot(10, newStack);
			world.playSound(player, te.getPos(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.BLOCKS, 1.0F, 1.0F);
			if (!player.isCreative()) {
				player.getHeldItem(handIn).shrink(1);
			}
		}else {
			if (te.hasUpgradeType((ItemUpgrade) stack.getItem())) {
				if (!player.isCreative())
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), te.getUpgradeTypeSlotItem((ItemUpgrade) stack.getItem()));
				else  te.getUpgradeTypeSlotItem((ItemUpgrade) stack.getItem()).shrink(1);
			}
			for (int upg : te.UPGRADES()) {
				if (te.getInventory().isItemValid(upg, stack) && !stack.isEmpty() && upg != 10) {
					if (!(te.getInventory().getStackInSlot(upg).isEmpty()) && upg == te.UPGRADES()[te.UPGRADES().length - 1]) {
						if (!player.isCreative())
							InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), te.getInventory().getStackInSlot(upg));
						else te.getInventory().getStackInSlot(upg).shrink(1);
					}
					if (te.getInventory().getStackInSlot(upg).isEmpty()) {
						te.getInventory().setStackInSlot(upg, newStack);
						if (!player.isCreative()) {
							player.getHeldItem(handIn).shrink(1);
						}
						world.playSound(player, te.getPos(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
				}
			}
		}
		te.update();
		return true;
	}
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityForge && world.getBlockState(pos).getBlock() != state.getBlock()) {
			ItemStackHandler inv = ((TileEntityForge) te).getInventory();
			for (int i = 0; i < inv.getSlots(); i++)
				Block.spawnAsEntity(world, pos, inv.getStackInSlot(i));
			world.updateComparatorOutputLevel(pos, this);
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.getValue(BURNING)) {


			if (rand.nextDouble() < 0.1D) {
				worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}
			if (stateIn.getValue(FACING) == EnumFacing.UP) {
				double d0 = (double) pos.getX() + 0.5D;
				double d1 = (double) pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
				double d2 = (double) pos.getZ() + 0.5D;
				double d4 = rand.nextDouble() * 0.6D - 0.3D;
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
				worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
				worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
				worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
				worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
			}{
				for (int l = 0; l < 3; ++l) {
					double d0 = (pos.getX() + rand.nextFloat());
					double d1 = (pos.getY() + rand.nextFloat());
					double d2 = (pos.getZ() + rand.nextFloat());
					int i1 = rand.nextInt(2) * 2 - 1;
					double d3 = (rand.nextFloat() - 0.5D) * 0.2D;
					double d4 = (rand.nextFloat() - 0.5D) * 0.2D;
					double d5 = (rand.nextFloat() - 0.5D) * 0.2D;
					worldIn.spawnParticle(EnumParticleTypes.DRIP_LAVA, d0, d1, d2, d3, d4, d5);

				}
			}
			}

	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

}
