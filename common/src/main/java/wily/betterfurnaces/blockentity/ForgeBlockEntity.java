package wily.betterfurnaces.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.inventory.*;

import java.util.function.Supplier;

public class ForgeBlockEntity extends SmeltingBlockEntity {
    public int[] FUEL() {return new int[]{3};}
    public int HEATER() {return 10;}
    public int UPGRADES()[]{ return new int[]{7,8,9,10,11,12,13};}
    public int[] INPUTS(){ return new int[]{0,1,2};}
    public int[] OUTPUTS(){ return new int[]{4,5,6};}
    public long LiquidCapacity() {return 2 * super.LiquidCapacity();}
    public int EnergyCapacity() {return 64000;}
    public boolean isForge(){ return true;}
    @Override
    public Direction facing(){
        return this.getBlockState().getValue(BlockStateProperties.FACING);
    }

    public ForgeBlockEntity(BlockPos pos, BlockState state, Supplier<Integer> cookTime) {
        super(pos, state, cookTime);
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
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new ForgeMenu(Registration.FORGE_CONTAINER.get(),i,level,getBlockPos(),playerInventory,playerEntity,fields);
    }

    @Override
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    public void addSlots(NonNullList<Slot> slots, @Nullable Player player) {
        int y1 = 62;
        int y2 = 100;
        int y3 = 80;
        int y4 = 5;
        slots.add(new SlotInput(this, 0, 27, y1));
        slots.add(new SlotInput(this, 1, 45, y1));
        slots.add(new SlotInput(this, 2, 63, y1));
        slots.add(new SlotFuel(this, 3, 8, y2));
        slots.add(new SlotOutput(player, this, 4, 108, y3));
        slots.add(new SlotOutput(player, this, 5, 126, y3));
        slots.add(new SlotOutput(player, this, 6, 144, y3));
        slots.add(new SlotUpgrade(this, 7, 7, y4));
        slots.add(new SlotUpgrade(this, 8, 25, y4));
        slots.add(new SlotUpgrade(this, 9, 43, y4));
        slots.add(new SlotHeater(this, 10, 79, y4));
        slots.add(new SlotUpgrade(this, 11, 115, y4));
        slots.add(new SlotUpgrade(this, 12, 133, y4));
        slots.add(new SlotUpgrade(this, 13, 151, y4));
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