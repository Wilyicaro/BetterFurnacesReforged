package wily.betterfurnaces.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import java.util.function.Supplier;

public class BlockBetterFurnace extends BlockSmelting{
    /**
     * Make a new Iron Furnace.
     *
     * @param name     The registry name.
     * @param moreFast The default cook time of this furnace.
     * @param teFunc   A supplier for the TE of this furnace.
     */
    public BlockBetterFurnace(String name, double moreFast, Supplier<TileEntity> teFunc) {
        super(name, moreFast, teFunc);
        this.setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH));
    }
}
