package wily.betterfurnaces.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import wily.betterfurnaces.tileentity.NetherhotFurnaceTileEntity;

import javax.annotation.Nullable;

public class NetherhotFurnaceBlock extends BlockFurnaceBase {

    public static final String NETHERHOT_FURNACE = "netherhot_furnace";

    public NetherhotFurnaceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 2;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new NetherhotFurnaceTileEntity();
    }
}
