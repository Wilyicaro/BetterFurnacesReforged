package wily.ultimatefurnaces.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import wily.betterfurnaces.blocks.BlockForgeBase;
import wily.ultimatefurnaces.init.RegistrationUF;
import wily.ultimatefurnaces.blockentity.BlockCopperForgeTile;

import javax.annotation.Nullable;

public class BlockCopperForge extends BlockForgeBase {

    public static final String COPPER_FORGE = "copper_forge";

    public BlockCopperForge(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new BlockCopperForgeTile(p_153215_, p_153216_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createFurnaceTicker(level, type, RegistrationUF.COPPER_FORGE_TILE.get());
    }
}
