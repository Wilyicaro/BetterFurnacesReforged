package wily.betterfurnaces.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;


public abstract class AbstractForgeMenu extends AbstractSmeltingMenu {

    public AbstractForgeMenu(MenuType<?> containerType, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        super(containerType, windowId, world, pos, playerInventory, player);
    }

    public AbstractForgeMenu(MenuType<?> containerType, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
        super(containerType, windowId, world, pos, playerInventory, player, fields);
    }
    @Override
    public void addInventorySlots(){
        TOP_ROW = 126;
        int y1 = 62;
        int y2 = 100 ;
        int y3 = 80;
        int y4 = 5;
        this.addSlot(new SlotInput(be, 0, 27, y1));
        this.addSlot(new SlotInput(be, 1, 45, y1));
        this.addSlot(new SlotInput(be, 2, 63, y1));
        this.addSlot(new SlotFuel(this.be, 3, 8, y2));
        this.addSlot(new SlotOutput(playerEntity, be.inventory, 4, 108, y3));
        this.addSlot(new SlotOutput(playerEntity, be.inventory, 5, 126, y3));
        this.addSlot(new SlotOutput(playerEntity, be.inventory, 6, 144, y3));
        this.addSlot(new SlotUpgrade(be, 7, 7, y4));
        this.addSlot(new SlotUpgrade(be, 8, 25, y4));
        this.addSlot(new SlotUpgrade(be, 9, 43, y4));
        this.addSlot(new SlotHeater(be, 10, 79, y4));
        this.addSlot(new SlotUpgrade(be, 11, 115, y4));
        this.addSlot(new SlotUpgrade(be, 12, 133, y4));
        this.addSlot(new SlotUpgrade(be, 13, 151, y4));
    }
}
