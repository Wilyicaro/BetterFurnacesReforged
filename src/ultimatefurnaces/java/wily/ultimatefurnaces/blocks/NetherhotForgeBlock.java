package wily.ultimatefurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.blocks.AbstractForgeBlock;
import wily.ultimatefurnaces.tileentity.NetherhotForgeTileEntity;

import javax.annotation.Nullable;

public class NetherhotForgeBlock extends AbstractForgeBlock {

    public static final String NETHERHOT_FORGE = "netherhot_forge";

    public NetherhotForgeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 2;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new NetherhotForgeTileEntity();
    }
}
