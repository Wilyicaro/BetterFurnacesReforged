package wily.ultimatefurnaces.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import wily.betterfurnaces.blocks.BlockFurnaceBase;
import wily.ultimatefurnaces.init.Registration;
import wily.ultimatefurnaces.tileentity.BlockUltimateFurnaceTile;

import javax.annotation.Nullable;

public class BlockUltimateFurnace extends BlockFurnaceBase {

    public static final String ULTIMATE_FURNACE = "ultimate_furnace";

    public BlockUltimateFurnace(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new BlockUltimateFurnaceTile(p_153215_, p_153216_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createFurnaceTicker(level, type, Registration.ULTIMATE_FURNACE_TILE.get());
    }
}
