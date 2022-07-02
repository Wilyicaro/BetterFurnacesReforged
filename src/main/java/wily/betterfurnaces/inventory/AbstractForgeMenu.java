package wily.betterfurnaces.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
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
        int i = 20;
        int y1 = 62 - i;
        int y2 = 100 - i;
        int y3 = 80 - i;
        int y4 = 5 - i;
        this.addSlot(new SlotInput(te, 0, 27, y1));
        this.addSlot(new SlotInput(te, 1, 45, y1));
        this.addSlot(new SlotInput(te, 2, 63, y1));
        this.addSlot(new SlotFuel(this.te, 3, 8, y2));
        this.addSlot(new SlotOutput(playerEntity, te, 4, 108, y3));
        this.addSlot(new SlotOutput(playerEntity, te, 5, 126, y3));
        this.addSlot(new SlotOutput(playerEntity, te, 6, 144, y3));
        this.addSlot(new SlotUpgrade(te, 7, 7, y4));
        this.addSlot(new SlotUpgrade(te, 8, 25, y4));
        this.addSlot(new SlotUpgrade(te, 9, 43, y4));
        this.addSlot(new SlotHeater(te, 10, 79, y4));
        this.addSlot(new SlotUpgrade(te, 11, 115, y4));
        this.addSlot(new SlotUpgrade(te, 12, 133, y4));
        this.addSlot(new SlotUpgrade(te, 13, 151, y4));
        layoutPlayerInventorySlots(8, 106);
    }
}
