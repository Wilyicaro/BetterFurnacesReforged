package wily.betterfurnaces.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import wily.betterfurnaces.blockentity.BlockEntityGoldFurnace;
import wily.betterfurnaces.init.Registration;

import javax.annotation.Nullable;

public class BlockGoldFurnace extends BlockFurnaceBase {

    public static final String GOLD_FURNACE = "gold_furnace";

    public BlockGoldFurnace(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new BlockEntityGoldFurnace(p_153215_, p_153216_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createFurnaceTicker(level, type, Registration.GOLD_FURNACE_TILE.get());
    }
}