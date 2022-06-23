package wily.ultimatefurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.blocks.BlockFurnaceBase;
import wily.ultimatefurnaces.tileentity.BlockCopperFurnaceTile;
import wily.ultimatefurnaces.tileentity.BlockSteelFurnaceTile;

import javax.annotation.Nullable;

public class BlockSteelFurnace extends BlockFurnaceBase {

    public static final String STEEL_FURNACE = "steel_furnace";

    public BlockSteelFurnace(Properties properties) {
        super(properties);
    }
    @Override
    public int getHarvestLevel(BlockState state) {
        return 1;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BlockSteelFurnaceTile();
    }
}
