package wily.ultimatefurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.blocks.BlockForgeBase;
import wily.ultimatefurnaces.tileentity.BlockNetherhotForgeTile;
import wily.ultimatefurnaces.tileentity.BlockUltimateForgeTile;

import javax.annotation.Nullable;

public class BlockUltimateForge extends BlockForgeBase {

    public static final String ULTIMATE_FORGE = "ultimate_forge";

    public BlockUltimateForge(Properties properties) {
        super(properties);
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 3;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BlockUltimateForgeTile();
    }
}
