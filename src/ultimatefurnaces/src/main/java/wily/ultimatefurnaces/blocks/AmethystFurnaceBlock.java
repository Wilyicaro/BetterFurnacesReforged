package wily.ultimatefurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.blocks.AbstractFurnaceBlock;
import wily.ultimatefurnaces.tileentity.AmethystFurnaceTileEntity;

import javax.annotation.Nullable;

public class AmethystFurnaceBlock extends AbstractFurnaceBlock {

    public static final String AMETHYST_FURNACE = "amethyst_furnace";

    public AmethystFurnaceBlock(Properties properties) {
        super(properties);
    }
    @Override
    public int getHarvestLevel(BlockState state) {
        return 2;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AmethystFurnaceTileEntity();
    }
}
