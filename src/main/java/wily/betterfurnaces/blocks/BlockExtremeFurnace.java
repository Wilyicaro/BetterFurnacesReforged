package wily.betterfurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.tileentity.BlockExtremeFurnaceTile;

import javax.annotation.Nullable;

public class BlockExtremeFurnace extends BlockFurnaceBase {

    public static final String EXTREME_FURNACE = "extreme_furnace";

    public BlockExtremeFurnace(Properties properties) {
        super(properties);
    }
    @Override
    public int getHarvestLevel(BlockState state) {
        return 3;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BlockExtremeFurnaceTile();
    }
}
