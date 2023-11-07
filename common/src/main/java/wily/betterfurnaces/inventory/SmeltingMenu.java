package wily.betterfurnaces.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.network.Messages;
import wily.betterfurnaces.network.PacketSyncEnergy;
import wily.betterfurnaces.network.PacketSyncFluid;
import wily.factoryapi.base.Storages;


public class SmeltingMenu extends AbstractInventoryMenu<SmeltingBlockEntity> {



    public SmeltingMenu(MenuType<?> containerType, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        this(containerType, windowId, world, pos, playerInventory, player, new SimpleContainerData(5));
    }
    public SmeltingMenu(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        this(ModObjects.FURNACE_CONTAINER.get(), windowId, world, pos, playerInventory, player, new SimpleContainerData(5));
    }

    public SmeltingMenu(MenuType<?> containerType, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
        super(containerType, windowId, world, pos, playerInventory, player, fields);
        checkContainerDataCount(this.fields, 5);
    }


    public boolean showInventoryButtons() {
        return this.fields.get(4) == 1;
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
        int i = this.fields.get(2);
        int j = this.fields.get(3);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    public int getBurnLeftScaled(int pixels) {
        int i = this.fields.get(1);
        if (i == 0) {
            i = 200;
        }

        return this.fields.get(0) * pixels / i;
    }

    public int getEnergyStored() {
        return be.energyStorage.getEnergyStored();
    }
    public int getMaxEnergyStored() {
        return be.energyStorage.getMaxEnergyStored();
    }
    public int BurnTimeGet(){
        return this.fields.get(0);
    }
    protected void updateChanges() {
        super.updateChanges();
        if (player instanceof ServerPlayer) {
            for (Direction d : Direction.values()) {
                be.getStorage(Storages.FLUID, d).ifPresent(t-> Messages.INSTANCE.sendToPlayer((ServerPlayer) player, new PacketSyncFluid(be.getBlockPos(), d, t.getFluidStack())));
            }
            Messages.INSTANCE.sendToPlayer((ServerPlayer) player, new PacketSyncEnergy(be.getBlockPos(),  be.energyStorage.getEnergyStored()));
        }
    }
}
