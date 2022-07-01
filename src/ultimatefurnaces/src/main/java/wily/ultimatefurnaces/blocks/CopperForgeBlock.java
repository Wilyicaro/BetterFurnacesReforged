package wily.ultimatefurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.blocks.AbstractForgeBlock;
import wily.ultimatefurnaces.tileentity.CopperForgeTileEntity;

import javax.annotation.Nullable;

public class CopperForgeBlock extends AbstractForgeBlock {

    public static final String COPPER_FORGE = "copper_forge";

    public CopperForgeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 1;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CopperForgeTileEntity();
    }
}
