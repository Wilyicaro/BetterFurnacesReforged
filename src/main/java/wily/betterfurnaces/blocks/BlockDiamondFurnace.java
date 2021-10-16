package wily.betterfurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.tileentity.BlockDiamondFurnaceTile;

import javax.annotation.Nullable;

public class BlockDiamondFurnace extends BlockFurnaceBase {

    public static final String DIAMOND_FURNACE = "diamond_furnace";

    public BlockDiamondFurnace(Properties properties) {
        super(properties);
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 2;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BlockDiamondFurnaceTile();
    }
}
