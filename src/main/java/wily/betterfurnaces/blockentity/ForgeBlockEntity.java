package wily.betterfurnaces.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.init.BlockEntityTypes;
import wily.betterfurnaces.inventory.*;
import wily.factoryapi.base.BlockSide;
import wily.factoryapi.base.FactoryItemSlot;

import java.util.function.Consumer;

import static wily.factoryapi.base.BlockSide.*;

public class ForgeBlockEntity extends SmeltingBlockEntity {
    public ForgeBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypes.FORGE_TILE.get(),pos, state);
    }
    @Override
    public int[] getFuelIndexes() {
        return new int[]{3};
    }

    @Override
    public int getHeaterIndex() {
        return 10;
    }

    @Override
    public int[] getUpgradeIndexes() {
        return new int[]{7,8,9,10,11,12,13};
    }

    @Override
    public int[] getInputs(){
        return new int[]{0,1,2};
    }

    @Override
    public int[] getOutputs(){
        return new int[]{4,5,6};
    }

    @Override
    public int getLiquidCapacity() {
        return 2 * super.getLiquidCapacity();
    }

    @Override
    public int getEnergyCapacity() {
        return 64000;
    }

    public static final BlockSide[] FORGE_TOP_FACE_SIDES = new BlockSide[]{TOP,BOTTOM,BACK,FRONT,RIGHT,LEFT};

    @Override
    protected BlockSide[] getSidesOrder() {
        return FORGE_TOP_FACE_SIDES;
    }

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory) {
        return new ForgeMenu(i,level,getBlockPos(),playerInventory,fields);
    }

    @Override
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    public void addSlots(Consumer<FactoryItemSlot> slots, @Nullable Player player) {
        int y1 = 62;
        int y2 = 100;
        int y3 = 80;
        int y4 = 5;
        slots.accept(new SlotInput(this, 0, 27, y1));
        slots.accept(new SlotInput(this, 1, 45, y1));
        slots.accept(new SlotInput(this, 2, 63, y1));
        slots.accept(new SlotFuel(this, 3, 8, y2));
        slots.accept(new SlotOutput(player, this, 4, 108, y3));
        slots.accept(new SlotOutput(player, this, 5, 126, y3));
        slots.accept(new SlotOutput(player, this, 6, 144, y3));
        slots.accept(new SlotUpgrade(this, 7, 7, y4));
        slots.accept(new SlotUpgrade(this, 8, 25, y4));
        slots.accept(new SlotUpgrade(this, 9, 43, y4));
        slots.accept(new SlotHeater(this, 10, 79, y4));
        slots.accept(new SlotUpgrade(this, 11, 115, y4));
        slots.accept(new SlotUpgrade(this, 12, 133, y4));
        slots.accept(new SlotUpgrade(this, 13, 151, y4));
    }
}