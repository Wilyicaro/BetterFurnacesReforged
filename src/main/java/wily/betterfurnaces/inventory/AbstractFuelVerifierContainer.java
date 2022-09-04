package wily.betterfurnaces.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.tileentity.AbstractFuelVerifierTileEntity;


public abstract class AbstractFuelVerifierContainer extends AbstractInventoryContainer<AbstractFuelVerifierTileEntity.FuelVerifierTileEntity> {



    public AbstractFuelVerifierContainer(ContainerType<?> containerType, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        this(containerType, windowId, world, pos, playerInventory, player, new IntArray(5));
    }

    public AbstractFuelVerifierContainer(ContainerType<?> containerType, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, IIntArray fields) {
        super(containerType, windowId, world, pos, playerInventory, player, fields);

        checkContainerDataCount(this.fields, 1);
    }

    @Override
    public void addInventorySlots() {
        this.addSlot(new SlotFuel(te, 0, 80, 48));
    }


    @OnlyIn(Dist.CLIENT)
    public int getBurnTimeScaled(int pixels) {
        int i = 20000;

        return this.fields.get(0) * pixels / i;
    }

    public float getBurnTime() {
        return (float) this.fields.get(0) / 200;
    }


    public static class FuelVerifierContainer extends AbstractFuelVerifierContainer {
        public FuelVerifierContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
            super(Registration.FUEL_VERIFIER_CONTAINER.get(), windowId, world, pos, playerInventory, player);
        }

        public FuelVerifierContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, IIntArray fields) {
            super(Registration.FUEL_VERIFIER_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
        }
    }
}
