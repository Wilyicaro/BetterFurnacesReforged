package wily.betterfurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.tileentity.DiamondFurnaceTileEntity;

import javax.annotation.Nullable;

public class DiamondFurnaceBlock extends AbstractFurnaceBlock {

    public static final String DIAMOND_FURNACE = "diamond_furnace";

    public DiamondFurnaceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 2;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DiamondFurnaceTileEntity();
    }
}
