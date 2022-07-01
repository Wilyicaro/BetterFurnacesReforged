package wily.ultimatefurnaces.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import wily.betterfurnaces.blocks.AbstractFurnaceBlock;
import wily.ultimatefurnaces.blockentity.UltimateFurnaceBlockEntity;
import wily.ultimatefurnaces.init.RegistrationUF;

import javax.annotation.Nullable;

public class UltimateFurnaceBlock extends AbstractFurnaceBlock {

    public static final String ULTIMATE_FURNACE = "ultimate_furnace";

    public UltimateFurnaceBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new UltimateFurnaceBlockEntity(p_153215_, p_153216_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createFurnaceTicker(level, type, RegistrationUF.ULTIMATE_FURNACE_TILE.get());
    }
}
