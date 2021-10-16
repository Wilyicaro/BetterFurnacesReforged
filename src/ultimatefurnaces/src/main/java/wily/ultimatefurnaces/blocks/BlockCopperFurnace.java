package wily.ultimatefurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.blocks.BlockFurnaceBase;
import wily.ultimatefurnaces.tileentity.BlockCopperFurnaceTile;

import javax.annotation.Nullable;

public class BlockCopperFurnace extends BlockFurnaceBase {

    public static final String COPPER_FURNACE = "copper_furnace";

    public BlockCopperFurnace(Properties properties) {
        super(properties);
    }
    @Override
    public int getHarvestLevel(BlockState state) {
        return 1;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BlockCopperFurnaceTile();
    }
}
