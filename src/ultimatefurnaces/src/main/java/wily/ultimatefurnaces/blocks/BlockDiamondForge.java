package wily.ultimatefurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.blocks.BlockForgeBase;
import wily.ultimatefurnaces.tileentity.BlockCopperForgeTile;
import wily.ultimatefurnaces.tileentity.BlockDiamondForgeTile;

import javax.annotation.Nullable;

public class BlockDiamondForge extends BlockForgeBase {

    public static final String DIAMOND_FORGE = "diamond_forge";

    public BlockDiamondForge(Properties properties) {
        super(properties);
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 3;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BlockDiamondForgeTile();
    }
}
