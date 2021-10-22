package wily.betterfurnaces.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.tileentity.BlockDiamondFurnaceTile;
import wily.betterfurnaces.tileentity.BlockExtremeFurnaceTile;

import javax.annotation.Nullable;

public class BlockExtremeFurnace extends BlockFurnaceBase {

    public static final String EXTREME_FURNACE = "extreme_furnace";

    public BlockExtremeFurnace(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new BlockExtremeFurnaceTile(p_153215_, p_153216_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createFurnaceTicker(level, type, Registration.EXTREME_FURNACE_TILE.get());
    }
}
