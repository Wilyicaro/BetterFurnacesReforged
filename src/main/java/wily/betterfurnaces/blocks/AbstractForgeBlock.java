package wily.betterfurnaces.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.items.EnergyFuelUpgradeItem;
import wily.betterfurnaces.items.LiquidFuelUpgradeItem;
import wily.betterfurnaces.items.UpgradeItem;
import wily.betterfurnaces.tileentity.AbstractForgeTileEntity;

import java.util.Random;

public abstract class AbstractForgeBlock extends AbstractSmeltingBlock implements IWaterLoggable {

    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty SHOW_ORIENTATION = BooleanProperty.create("show_orientation");
    public static final VoxelShape FORGE_SHAPE = VoxelShapes.join(VoxelShapes.block(), VoxelShapes.or(box(0, 0, 0, 16, 1, 16), box(15, 1, 0, 16, 15, 1), box(0, 1, 0, 1, 15, 1), box(0, 1, 15, 1, 15, 16), box(15, 1, 15, 16, 15, 16), box(1.75, 15, 1.75, 14.5, 15, 14.5), box(0, 15, 0, 16, 16, 16), box(1, 0.5, 1, 15, 15, 15)), IBooleanFunction.AND);

    public AbstractForgeBlock(Properties properties) {
        super(properties.noOcclusion().emissiveRendering(AbstractForgeBlock::getOrientation));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.SOUTH).setValue(WATERLOGGED, false).setValue(SHOW_ORIENTATION, false));
    }

    public static boolean getOrientation(BlockState state, IBlockReader world, BlockPos pos) {
        return state.getValue(SHOW_ORIENTATION);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return FORGE_SHAPE;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return state.getValue(BlockStateProperties.LIT) ? 14 : 0;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        Direction facing = ctx.getNearestLookingDirection().getOpposite();
        boolean flag = ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(FACING, facing).setValue(WATERLOGGED, flag);
    }


    protected ActionResultType interactUpgrade(World world, BlockPos pos, PlayerEntity player, Hand handIn, ItemStack stack) {
        Item hand = player.getItemInHand(handIn).getItem();
        if (!(hand instanceof UpgradeItem)) {
            return ActionResultType.SUCCESS;
        }
        if (!(world.getBlockEntity(pos) instanceof AbstractForgeTileEntity)) {
            return ActionResultType.SUCCESS;
        }
        AbstractForgeTileEntity te = (AbstractForgeTileEntity) world.getBlockEntity(pos);
        ItemStack newStack = new ItemStack(stack.getItem(), 1);
        newStack.setTag(stack.getTag());

        if (hand instanceof LiquidFuelUpgradeItem || hand instanceof EnergyFuelUpgradeItem) {
            if ((!(te.getItem(10).isEmpty())) && (!player.isCreative())) {
                InventoryHelper.dropItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), te.getItem(10));
            }
            te.setItem(10, newStack);
            world.playSound(null, te.getBlockPos(), SoundEvents.ARMOR_EQUIP_IRON, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!player.isCreative()) {
                player.getItemInHand(handIn).shrink(1);
            }
        } else {
            super.interactUpgrade(world, pos, player, handIn, stack);
        }
        te.onUpdateSent();
        return ActionResultType.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
        if (state.getValue(BlockStateProperties.LIT)) {
            if (world.getBlockEntity(pos) == null) {
                return;
            }
            if (!(world.getBlockEntity(pos) instanceof AbstractForgeTileEntity)) {
                return;
            }
            AbstractForgeTileEntity tile = ((AbstractForgeTileEntity) world.getBlockEntity(pos));

            if (state.getValue(BlockStateProperties.FACING) == Direction.UP) {
                double d0 = (double) pos.getX() + 0.5D;
                double d1 = (double) pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
                double d2 = (double) pos.getZ() + 0.5D;
                double d4 = rand.nextDouble() * 0.6D - 0.3D;
                if (rand.nextDouble() < 0.1D) {
                    world.playLocalSound(d0, pos.getY(), d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                }
                world.addParticle(ParticleTypes.SMOKE, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SMOKE, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SMOKE, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SMOKE, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
            } else {
                for (int l = 0; l < 3; ++l) {
                    double d0 = (pos.getX() + rand.nextFloat());
                    double d1 = (pos.getY() + rand.nextFloat());
                    double d2 = (pos.getZ() + rand.nextFloat());
                    int i1 = rand.nextInt(2) * 2 - 1;
                    double d3 = (rand.nextFloat() - 0.5D) * 0.2D;
                    double d4 = (rand.nextFloat() - 0.5D) * 0.2D;
                    double d5 = (rand.nextFloat() - 0.5D) * 0.2D;
                    world.addParticle(ParticleTypes.DRIPPING_LAVA, d0, d1, d2, d3, d4, d5);

                }
            }
        }
    }

    public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
        if (p_185499_2_ == Rotation.CLOCKWISE_90 || p_185499_2_ == Rotation.COUNTERCLOCKWISE_90) {
            if (p_185499_1_.getValue(FACING) == Direction.WEST || p_185499_1_.getValue(FACING) == Direction.EAST) {
                return p_185499_1_.setValue(FACING, Direction.UP);
            } else if (p_185499_1_.getValue(FACING) == Direction.UP || p_185499_1_.getValue(FACING) == Direction.DOWN) {
                return p_185499_1_.setValue(FACING, Direction.WEST);
            }
        }
        return p_185499_1_;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, WATERLOGGED, BlockStateProperties.LIT, COLORED, SHOW_ORIENTATION);
    }

}
