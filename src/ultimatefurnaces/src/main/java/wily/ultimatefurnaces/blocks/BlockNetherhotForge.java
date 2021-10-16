package wily.ultimatefurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.blocks.BlockForgeBase;
import wily.ultimatefurnaces.tileentity.BlockDiamondForgeTile;
import wily.ultimatefurnaces.tileentity.BlockNetherhotForgeTile;

import javax.annotation.Nullable;

public class BlockNetherhotForge extends BlockForgeBase {

    public static final String NETHERHOT_FORGE = "netherhot_forge";

    public BlockNetherhotForge(Properties properties) {
        super(properties);
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 2;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BlockNetherhotForgeTile();
    }
}
