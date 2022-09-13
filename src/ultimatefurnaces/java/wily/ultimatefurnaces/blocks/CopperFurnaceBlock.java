package wily.ultimatefurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.blocks.AbstractFurnaceBlock;
import wily.ultimatefurnaces.tileentity.CopperFurnaceTileEntity;

import javax.annotation.Nullable;

public class CopperFurnaceBlock extends AbstractFurnaceBlock {

    public static final String COPPER_FURNACE = "copper_furnace";

    public CopperFurnaceBlock(Properties properties) {
        super(properties);
    }
    @Override
    public int getHarvestLevel(BlockState state) {
        return 1;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CopperFurnaceTileEntity();
    }
}
