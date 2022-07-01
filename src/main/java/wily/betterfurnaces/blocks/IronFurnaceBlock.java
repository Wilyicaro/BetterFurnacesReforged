package wily.betterfurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.tileentity.IronFurnaceTileEntity;

import javax.annotation.Nullable;

public class IronFurnaceBlock extends AbstractFurnaceBlock {

    public static final String IRON_FURNACE = "iron_furnace";

    public IronFurnaceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 1;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new IronFurnaceTileEntity();
    }
}
