package wily.betterfurnaces.tileentity;

import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public abstract class BlockForgeTileBase extends BlockSmeltingTileBase {
    public int FUEL() {return 3;}
    public int HEATER() {return 10;}
    public int UPGRADES()[]{ return new int[]{7,8,9,10,11,12,13};}
    public int[] INPUTS(){ return new int[]{0,1,2};}
    public int[] OUTPUTS(){ return new int[]{4,5,6};}
    public int EnergyUse() {return 1800;}
    public int LiquidCapacity() {return 8000;}
    public int EnergyCapacity() {return 64000;}
    public boolean isForge(){ return true;}
    @Override
    public Direction facing(){
        return this.getBlockState().getValue(BlockStateProperties.FACING);
    }

    public BlockForgeTileBase(TileEntityType<?> tileentitytypeIn) {
        super(tileentitytypeIn, 14);

    }

    @Override
    public boolean inputSlotsEmpty(){
        return (!this.inventory.get(FINPUT()).isEmpty() || !this.inventory.get(FINPUT() +1).isEmpty() || !this.inventory.get(FINPUT() + 2).isEmpty());
    }
    @Override
    public boolean smeltValid(){
        return (this.canSmelt(irecipeSlot(FINPUT()).orElse(null), FINPUT(), FOUTPUT()) || this.canSmelt(irecipeSlot(FINPUT() + 1).orElse(null), FINPUT() + 1, FOUTPUT() + 1) || this.canSmelt(irecipeSlot(FINPUT() + 2).orElse(null), FINPUT() + 2, FOUTPUT() + 2));
    }
    @Override
    public void trySmelt(){
        this.smeltItem(irecipeSlot(FINPUT()).orElse(null), FINPUT(), correspondentOutputSlot(FINPUT()));
            this.smeltItem(irecipeSlot(FINPUT() + 1).orElse(null), FINPUT() + 1, correspondentOutputSlot(FINPUT() + 1));
            this.smeltItem(irecipeSlot(FINPUT() + 2).orElse(null), FINPUT() + 2, correspondentOutputSlot(FINPUT() + 2));
    }
    @Override
    public int getIndexBottom() {
        return facing().getOpposite().ordinal();
    }
    @Override
    public int getIndexTop() {
        return facing().ordinal();
    }
    @Override
    public int getIndexFront() {
        if (facing() == Direction.NORTH || facing() == Direction.EAST)  {
            return Direction.DOWN.ordinal();
        } else if ((facing() == Direction.SOUTH) || (facing() == Direction.WEST)) {
            return Direction.UP.ordinal();
        }else if (facing() == Direction.UP){
            return Direction.NORTH.ordinal();
        }else {
            return Direction.SOUTH.ordinal();
        }
    }
    @Override
    public int getIndexBack() {
        if (facing() == Direction.NORTH || facing() == Direction.EAST)  {
            return Direction.UP.ordinal();
        } else if ((facing() == Direction.SOUTH) || (facing() == Direction.WEST)) {
            return Direction.DOWN.ordinal();
        }else if (facing() == Direction.UP){
            return Direction.SOUTH.ordinal();
        }else {
            return Direction.NORTH.ordinal();
        }
    }
    @Override
    public int getIndexLeft() {
        if (facing() == Direction.EAST || facing() == Direction.WEST) {
            return Direction.SOUTH.ordinal();
        } else {
            return Direction.EAST.ordinal();
        }
    }
    @Override
    public int getIndexRight() {
        if (facing() == Direction.EAST || facing() == Direction.WEST) {
            return Direction.NORTH.ordinal();
        } else {
            return Direction.WEST.ordinal();
        }
    }
}