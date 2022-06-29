package wily.betterfurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.tileentity.ExtremeForgeTileEntity;

import javax.annotation.Nullable;

public class ExtremeForgeBlock extends BlockForgeBase {

    public static final String EXTREME_FORGE = "extreme_forge";

    public ExtremeForgeBlock(Properties properties) {
        super(properties);
    }
    @Override
    public int getHarvestLevel(BlockState state) {
        return 4;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ExtremeForgeTileEntity();
    }
}
