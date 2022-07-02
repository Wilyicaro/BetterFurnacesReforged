package wily.betterfurnaces.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public abstract class AbstractForgeBlockEntity extends AbstractSmeltingBlockEntity {
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

    public AbstractForgeBlockEntity(BlockEntityType<?> tileentitytypeIn, BlockPos pos, BlockState state) {
        super(tileentitytypeIn, pos, state, 14);
    }

    public int getIndexBottom() {
        return facing().getOpposite().ordinal();
    }
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