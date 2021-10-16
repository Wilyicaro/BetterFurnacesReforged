package wily.ultimatefurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.blocks.BlockForgeBase;
import wily.ultimatefurnaces.tileentity.BlockIronForgeTile;

import javax.annotation.Nullable;

public class BlockIronForge extends BlockForgeBase {

    public static final String IRON_FORGE = "iron_forge";

    public BlockIronForge(Properties properties) {
        super(properties);
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 1;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BlockIronForgeTile();
    }
}
