package wily.ultimatefurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.blocks.BlockFurnaceBase;
import wily.ultimatefurnaces.tileentity.BlockUltimateFurnaceTile;

import javax.annotation.Nullable;

public class BlockUltimateFurnace extends BlockFurnaceBase {

    public static final String ULTIMATE_FURNACE = "ultimate_furnace";

    public BlockUltimateFurnace(Properties properties) {
        super(properties);
    }
    @Override
    public int getHarvestLevel(BlockState state) {
        return 3;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BlockUltimateFurnaceTile();
    }
}
