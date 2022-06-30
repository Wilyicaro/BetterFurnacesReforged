package wily.betterfurnaces.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import wily.betterfurnaces.blockentity.ExtremeFurnaceBlockEntity;
import wily.betterfurnaces.init.Registration;

import javax.annotation.Nullable;

public class ExtremeFurnaceBlock extends BlockFurnaceBase {

    public static final String EXTREME_FURNACE = "extreme_furnace";

    public ExtremeFurnaceBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new ExtremeFurnaceBlockEntity(p_153215_, p_153216_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createFurnaceTicker(level, type, Registration.EXTREME_FURNACE_TILE.get());
    }
}
