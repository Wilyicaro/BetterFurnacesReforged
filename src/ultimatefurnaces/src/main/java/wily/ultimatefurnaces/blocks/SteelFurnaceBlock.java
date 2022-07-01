package wily.ultimatefurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.blocks.AbstractFurnaceBlock;
import wily.ultimatefurnaces.tileentity.SteelFurnaceTileEntity;

import javax.annotation.Nullable;

public class SteelFurnaceBlock extends AbstractFurnaceBlock {

    public static final String STEEL_FURNACE = "steel_furnace";

    public SteelFurnaceBlock(Properties properties) {
        super(properties);
    }
    @Override
    public int getHarvestLevel(BlockState state) {
        return 1;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SteelFurnaceTileEntity();
    }
}
