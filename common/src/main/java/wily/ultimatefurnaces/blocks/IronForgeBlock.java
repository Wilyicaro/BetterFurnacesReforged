package wily.ultimatefurnaces.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import wily.betterfurnaces.blocks.AbstractForgeBlock;
import wily.ultimatefurnaces.blockentity.IronForgeBlockEntity;
import wily.ultimatefurnaces.init.RegistrationUF;

import org.jetbrains.annotations.Nullable;

public class IronForgeBlock extends AbstractForgeBlock {

    public static final String IRON_FORGE = "iron_forge";

    public IronForgeBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new IronForgeBlockEntity(p_153215_, p_153216_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createFurnaceTicker(level, type, RegistrationUF.IRON_FORGE_TILE.get());
    }
}
