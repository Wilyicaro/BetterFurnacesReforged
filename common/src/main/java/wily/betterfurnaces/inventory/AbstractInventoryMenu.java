package wily.betterfurnaces.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;


public abstract class AbstractInventoryMenu<T extends InventoryBlockEntity> extends AbstractContainerMenu {

    public T be;
    protected ContainerData fields;
    public Player player;
    protected final Level level;
    protected int TOP_ROW = 84;



    public AbstractInventoryMenu(MenuType<?> containerType, int windowId, Level level, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
        super(containerType, windowId);
        this.be = (T) level.getBlockEntity(pos);

        this.player = player;
        this.level = playerInventory.player.level();
        this.fields = fields;
        this.addInventorySlots();
        layoutPlayerInventorySlots(8, TOP_ROW);
        this.addDataSlots(this.fields);
        checkContainerSize(this.be.inventory, be.getInventorySize());
    }

    public void addInventorySlots(){
        be.getSlots(player).forEach((this::addSlot));
    }
    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        updateChanges();
    }
    @Override
    public void sendAllDataToRemote() {
        super.sendAllDataToRemote();
        updateChanges();

    }
    protected void updateChanges() {
        be.syncAdditionalMenuData(this, player);
    }
    @Override
    protected boolean moveItemStackTo(ItemStack itemStack, int i, int j, boolean bl) {
        boolean bl2 = false;
        int k = i;
        if (bl) {
            k = j - 1;
        }

        Slot slot;
        ItemStack itemStack2;
        if (itemStack.isStackable()) {
            while(!itemStack.isEmpty()) {
                if (bl) {
                    if (k < i) {
                        break;
                    }
                } else if (k >= j) {
                    break;
                }

                slot = this.slots.get(k);
                itemStack2 = slot.getItem();
                if (!itemStack2.isEmpty() && ItemStack.isSameItemSameTags(itemStack, itemStack2) && slot.mayPlace(itemStack)) {
                    int l = itemStack2.getCount() + itemStack.getCount();
                    if (l <= itemStack.getMaxStackSize()) {
                        itemStack.setCount(0);
                        itemStack2.setCount(l);
                        slot.setChanged();
                        bl2 = true;
                    } else if (itemStack2.getCount() < itemStack.getMaxStackSize()) {
                        itemStack.shrink(itemStack.getMaxStackSize() - itemStack2.getCount());
                        itemStack2.setCount(itemStack.getMaxStackSize());
                        slot.setChanged();
                        bl2 = true;
                    }
                }

                if (bl) {
                    --k;
                } else {
                    ++k;
                }
            }
        }

        if (!itemStack.isEmpty()) {
            if (bl) {
                k = j - 1;
            } else {
                k = i;
            }

            while(true) {
                if (bl) {
                    if (k < i) {
                        break;
                    }
                } else if (k >= j) {
                    break;
                }

                slot = this.slots.get(k);
                itemStack2 = slot.getItem();
                if (itemStack2.isEmpty() && slot.mayPlace(itemStack)) {
                    if (itemStack.getCount() > slot.getMaxStackSize()) {
                        slot.set(itemStack.split(slot.getMaxStackSize()));
                    } else {
                        slot.set(itemStack.split(itemStack.getCount()));
                    }

                    slot.setChanged();
                    bl2 = true;
                    break;
                }

                if (bl) {
                    --k;
                } else {
                    ++k;
                }
            }
        }
        return bl2;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            int inventorySize = be.getInventorySize();
            if (index < inventorySize) {
                if (!this.moveItemStackTo(stack, inventorySize, inventorySize + 36, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemstack);
            } else if (!this.moveItemStackTo(stack, 0, inventorySize, false)){
                if (index < inventorySize + 27) {
                    if (!this.moveItemStackTo(stack, inventorySize + 27, inventorySize + 36, true)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < inventorySize + 36) {
                    if (!this.moveItemStackTo(stack, inventorySize, inventorySize + 27, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }
        return itemstack;
    }



    private int addSlotRange(Container container, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new Slot(container, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(Container container, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(container, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    protected void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(player.getInventory(), 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(player.getInventory(), 0, leftCol, topRow, 9, 18);
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return be.inventory.stillValid(p_38874_);
    }
}
