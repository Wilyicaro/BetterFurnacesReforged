package wily.betterfurnaces.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.network.FluidSyncPayload;
import wily.factoryapi.base.FactoryStorage;
import wily.factoryapi.base.network.CommonNetwork;


public class SmeltingMenu extends AbstractInventoryMenu<SmeltingBlockEntity> {

    public SmeltingMenu(int windowId, Level world, BlockPos pos, Inventory playerInventory, ContainerData data) {
        this(ModObjects.FURNACE_CONTAINER.get(), windowId, world, pos, playerInventory, data);
    }

    public SmeltingMenu(int windowId, Level world, BlockPos pos, Inventory playerInventory) {
        this(ModObjects.FURNACE_CONTAINER.get(), windowId, world, pos, playerInventory);
    }

    public SmeltingMenu(MenuType<?> containerType, int windowId, Level world, BlockPos pos, Inventory playerInventory) {
        this(containerType, windowId, world, pos, playerInventory, new SimpleContainerData(7){
            @Override
            public void set(int i, int j) {
                if (i == 5 && world.getBlockEntity(pos) instanceof SmeltingBlockEntity smeltingBe) smeltingBe.energyStorage.setEnergyStored(j);
                super.set(i, j);
            }
        });
    }

    public SmeltingMenu(MenuType<?> containerType, int windowId, Level world, BlockPos pos, Inventory playerInventory, ContainerData data) {
        super(containerType, windowId, world, pos, playerInventory, data);
        checkContainerDataCount(this.data, 7);
    }

    public boolean showInventoryButtons() {
        return this.data.get(4) == 1;
    }

    public int getRedstoneMode() {
        return this.be.getRedstoneSetting();
    }

    public int getComSub() {
        return this.be.getRedstoneComSub();
    }

    public boolean getAutoInput() {
        return this.be.getAutoInput() == 1;
    }

    public boolean getAutoOutput() {
        return this.be.getAutoOutput() == 1;
    }

    public BlockPos getPos() {
        return this.be.getBlockPos();
    }

    public int getCookScaled(int pixels) {
        int i = this.data.get(2);
        int j = this.data.get(3);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    public int getBurnLeftScaled(int pixels) {
        int i = this.data.get(1);
        if (i == 0) {
            i = 200;
        }

        return this.data.get(0) * pixels / i;
    }

    public int getBurnTime(){
        return this.data.get(0);
    }

    public int getEnergyStored() {
        return this.data.get(5);
    }

    public int getMaxEnergyStored() {
        return this.data.get(6);
    }


    protected void updateChanges() {
        super.updateChanges();
        if (player instanceof ServerPlayer sp) {
            CommonNetwork.sendToPlayer(sp, new FluidSyncPayload(be.getBlockPos(), be.fluidTank.getFluidInstance()));
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(be.getLevel(), be.getBlockPos()), player, be.getBlockState().getBlock());
    }
}
