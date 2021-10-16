package wily.ultimatefurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.blocks.BlockForgeBase;
import wily.ultimatefurnaces.tileentity.BlockGoldForgeTile;

import javax.annotation.Nullable;

public class BlockGoldForge extends BlockForgeBase {

    public static final String GOLD_FORGE = "gold_forge";

    public BlockGoldForge(Properties properties) {
        super(properties);
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 2;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BlockGoldForgeTile();
    }
}
