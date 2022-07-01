package wily.ultimatefurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.blocks.AbstractForgeBlock;
import wily.ultimatefurnaces.tileentity.UltimateForgeTileEntity;

import javax.annotation.Nullable;

public class UltimateForgeBlock extends AbstractForgeBlock {

    public static final String ULTIMATE_FORGE = "ultimate_forge";

    public UltimateForgeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 3;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new UltimateForgeTileEntity();
    }
}
