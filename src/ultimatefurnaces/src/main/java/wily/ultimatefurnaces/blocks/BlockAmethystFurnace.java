package wily.ultimatefurnaces.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import wily.betterfurnaces.blocks.BlockFurnaceBase;
import wily.ultimatefurnaces.blockentity.BlockEntityAmethystFurnace;
import wily.ultimatefurnaces.init.RegistrationUF;

import javax.annotation.Nullable;

public class BlockAmethystFurnace extends BlockFurnaceBase {

    public static final String AMETHYST_FURNACE = "amethyst_furnace";

    public BlockAmethystFurnace(Properties properties) {
        super(properties);
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new BlockEntityAmethystFurnace(p_153215_, p_153216_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createFurnaceTicker(level, type, RegistrationUF.AMETHYST_FURNACE_TILE.get());
    }
}
