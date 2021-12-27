package wily.betterfurnaces.blocks;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.util.*;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.inventory.ContainerBF;
import wily.betterfurnaces.tile.TileEntitySmeltingBase;
import net.minecraft.block.Block;
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
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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

public class BlockIronFurnace extends Block {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool BURNING = PropertyBool.create("burning");
	public static final PropertyBool COLORED = PropertyBool.create("colored");


	protected final double moreFast;
	protected final Supplier<TileEntity> teFunc;

	/**
	 * Make a new Iron Furnace.
	 * @param name The registry name.
	 * @param moreFast The default cook time of this furnace.
	 * @param teFunc A supplier for the TE of this furnace.
	 */
	public BlockIronFurnace(String name, double moreFast, Supplier<TileEntity> teFunc) {
		super(Material.IRON);
		this.setUnlocalizedName(BetterFurnacesReforged.MODID + "." + name);
		this.setRegistryName(BetterFurnacesReforged.MODID, name);
		this.setCreativeTab(BetterFurnacesReforged.BF_TAB);
		this.setHardness(2.0F);
		this.setResistance(9.0F);
		this.setHarvestLevel("pickaxe", 1);
		this.setLightOpacity(0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(BURNING, false).withProperty(COLORED, false));
		this.moreFast = moreFast;
		this.teFunc = teFunc;
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(BURNING) ? 14 : 0;
	}
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format("info.betterfurnacesreforged.morefast", moreFast));
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

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.HORIZONTALS[(meta & 0b1100) >> 2]).withProperty(BURNING, (meta & 1) == 1).withProperty(COLORED, (meta & 2) == 2);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(COLORED) ? 2 : 0) | (state.getValue(BURNING) ? 1 : 0) | (state.getValue(FACING).getHorizontalIndex() << 2);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
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
		if (te instanceof TileEntitySmeltingBase) {
			ItemStackHandler inv = ((TileEntitySmeltingBase) te).getInventory();
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

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getTileEntity(pos);
		if (!world.isRemote && te instanceof TileEntitySmeltingBase && ((TileEntitySmeltingBase) te).isFluid()) {
			ItemStack stack = player.getHeldItem(hand);
			FluidStack fs = FluidUtil.getFluidContained(stack);
			if (fs != null && TileEntitySmeltingBase.getFluidBurnTime(fs) > 0) {
				FluidActionResult res = FluidUtil.tryEmptyContainer(stack, FluidUtil.getFluidHandler(world, pos, null), 1000, player, true);
				if (res.isSuccess()) {
					world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.PLAYERS, 0.6F, 0.8F);
					if (!player.capabilities.isCreativeMode) player.setHeldItem(hand, res.result);
					return true;
				}
			}
		}

		if (!player.isSneaking() && !world.isRemote) {
			player.openGui(BetterFurnacesReforged.INSTANCE, ContainerBF.GUIID, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntitySmeltingBase && world.getBlockState(pos).getBlock() != state.getBlock()) {
			ItemStackHandler inv = ((TileEntitySmeltingBase) te).getInventory();
			for (int i = 0; i < inv.getSlots(); i++)
				Block.spawnAsEntity(world, pos, inv.getStackInSlot(i));
			world.updateComparatorOutputLevel(pos, this);
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.getValue(BURNING)) {
			EnumFacing enumfacing = (EnumFacing) stateIn.getValue(FACING);
			double d0 = (double) pos.getX() + 0.5D;
			double d1 = (double) pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
			double d2 = (double) pos.getZ() + 0.5D;
			double d4 = rand.nextDouble() * 0.6D - 0.3D;

			if (rand.nextDouble() < 0.1D) {
				worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}

			switch (enumfacing) {
				case WEST:
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					break;
				case EAST:
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					break;
				case NORTH:
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
					worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
					break;
				case SOUTH:
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
					worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

}
