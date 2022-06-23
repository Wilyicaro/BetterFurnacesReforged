package wily.ultimatefurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.blocks.BlockFurnaceBase;
import wily.ultimatefurnaces.tileentity.BlockCopperFurnaceTile;
import wily.ultimatefurnaces.tileentity.BlockPlatinumFurnaceTile;

import javax.annotation.Nullable;

public class BlockPlatinumFurnace extends BlockFurnaceBase {

    public static final String PLATINUM_FURNACE = "platinum_furnace";

    public BlockPlatinumFurnace(Properties properties) {
        super(properties);
    }
    @Override
    public int getHarvestLevel(BlockState state) {
        return 1;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BlockPlatinumFurnaceTile();
    }
}
