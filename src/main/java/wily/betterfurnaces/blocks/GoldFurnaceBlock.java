package wily.betterfurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.tileentity.GoldFurnaceTileEntity;

import javax.annotation.Nullable;

public class GoldFurnaceBlock extends AbstractFurnaceBlock {

    public static final String GOLD_FURNACE = "gold_furnace";

    public GoldFurnaceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 2;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new GoldFurnaceTileEntity();
    }
}
