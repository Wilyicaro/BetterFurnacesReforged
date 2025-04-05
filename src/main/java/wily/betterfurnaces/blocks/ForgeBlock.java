package wily.betterfurnaces.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.init.BlockEntityTypes;
import wily.betterfurnaces.items.UpgradeItem;
import wily.factoryapi.util.VoxelShapeUtil;

import java.util.function.Supplier;

public class ForgeBlock extends SmeltingBlock implements SimpleWaterloggedBlock {

    public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public ForgeBlock(Properties properties, Supplier<Integer> defaultCookTime) {
        super(properties.noOcclusion(),defaultCookTime);
        this.registerDefaultState( this.defaultBlockState().setValue(FACING, Direction.SOUTH).setValue(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState p_48735_, BlockGetter p_48736_, BlockPos p_48737_, CollisionContext p_48738_) {
        return VoxelShapeUtil.rotate(FORGE_SHAPE, p_48735_.getValue(FACING));
    }

    public static final VoxelShape FORGE_SHAPE = Shapes.join(Shapes.block(), Shapes.or(box(0, 0,0, 16,1,16), box(15,1,0,16,15,1), box(0,1,0,1,15,1), box(0,1,15,1,15,16), box(15,1,15,16,15,16), box(1.75,15,1.75,14.5,15,14.5), box(0,15,0,16,16,16) ,box(1, 0.5,1,15,15,15)), BooleanOp.AND);


    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction facing = ctx.getNearestLookingDirection().getOpposite();
        boolean flag = ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER;;
        return this.defaultBlockState().setValue(FACING, facing).setValue(WATERLOGGED, flag);
    }

    @Override
    protected void interactUpgrade(SmeltingBlockEntity be, Level level, BlockPos pos, Player player, InteractionHand handIn, ItemStack handItem) {
        Item hand = player.getItemInHand(handIn).getItem();

        if (((UpgradeItem) hand).upgradeType == UpgradeItem.Type.ALTERNATIVE_FUEL) {
            if ((!(be.inventory.getItem(10).isEmpty())) && (!player.isCreative())) {
                Containers.dropItemStack(level, pos.getX(), pos.getY() + 1, pos.getZ(), ((Container) be).getItem(10));
            }
            be.inventory.setItem(10, handItem.copyWithCount(1));
            level.playSound(null, be.getBlockPos(), SoundEvents.ARMOR_EQUIP_IRON/*? if >=1.20.5 {*/.value()/*?}*/, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!player.isCreative()) {
                player.getItemInHand(handIn).shrink(1);
            }
            be.onUpdateSent();
        }else {
            super.interactUpgrade(be, level, pos, player, handIn, handItem);
        }
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
        if (state.getValue(BlockStateProperties.LIT)) {
            if (world.getBlockEntity(pos) == null)
            {
                return;
            }
            if (!(world.getBlockEntity(pos) instanceof SmeltingBlockEntity))
            {
                return;
            }

            if (state.getValue(BlockStateProperties.FACING) == Direction.UP){
                double d0 = (double) pos.getX() + 0.5D;
                double d1 = (double) pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
                double d2 = (double) pos.getZ() + 0.5D;
                double d4 = rand.nextDouble() * 0.6D - 0.3D;
                if (rand.nextDouble() < 0.1D) {
                    world.playLocalSound(d0, pos.getY(), d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                }
                world.addParticle(ParticleTypes.SMOKE, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SMOKE, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SMOKE, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SMOKE, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
            }else{
                for (int l = 0; l < 3; ++l) {
                    double d0 = (pos.getX() + rand.nextFloat());
                    double d1 = (pos.getY() + rand.nextFloat());
                    double d2 = (pos.getZ() + rand.nextFloat());
                    double d3 = (rand.nextFloat() - 0.5D) * 0.2D;
                    double d4 = (rand.nextFloat() - 0.5D) * 0.2D;
                    double d5 = (rand.nextFloat() - 0.5D) * 0.2D;
                    world.addParticle(ParticleTypes.DRIPPING_LAVA, d0, d1, d2, d3, d4, d5);
                }
            }
        }
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return BlockEntityTypes.FORGE_TILE.get().create(blockPos,blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createFurnaceTicker(level, type, BlockEntityTypes.FORGE_TILE.get());
    }

    public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
        if (p_185499_2_ == Rotation.CLOCKWISE_90 || p_185499_2_ == Rotation.COUNTERCLOCKWISE_90) {
            if ((Direction) p_185499_1_.getValue(FACING) == Direction.WEST || (Direction) p_185499_1_.getValue(FACING) == Direction.EAST) {
                return p_185499_1_.setValue(FACING, Direction.UP);
            } else if ((Direction) p_185499_1_.getValue(FACING) == Direction.UP || (Direction)p_185499_1_.getValue(FACING) == Direction.DOWN) {
                return p_185499_1_.setValue(FACING, Direction.WEST);
            }
        }
        return p_185499_1_;
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, WATERLOGGED, BlockStateProperties.LIT, COLORED);
    }

}
