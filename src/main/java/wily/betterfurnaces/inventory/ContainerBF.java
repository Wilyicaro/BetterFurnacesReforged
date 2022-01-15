package wily.betterfurnaces.inventory;

import javax.annotation.Nullable;

import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.net.MessageSync;
import wily.betterfurnaces.tile.TileEntitySmeltingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerBF extends Container {
	public static int GUIID = 1;
	public static final int SLOTS_TE = 0;
	public static final int SLOTS_TE_SIZE = 6;
	public static final int SLOTS_INVENTORY = SLOTS_TE_SIZE;
	public static final int SLOTS_HOTBAR = SLOTS_INVENTORY + 3 * 9;

	private final TileEntitySmeltingBase te;
	private final EntityPlayerMP player;

	public ContainerBF(InventoryPlayer playerInv, TileEntitySmeltingBase te) {
		this.te = te;
		this.addSlotToContainer(new SlotFurnaceInput(te.getInventory(), 0, 54, 18));
		this.addSlotToContainer(new SlotFurnaceFuel(te.getInventory(), 1, 54, 54));
		this.addSlotToContainer(new SlotFurnaceOutput(playerInv.player, te.getInventory(), 2, 116, 35));
		this.addSlotToContainer(new SlotUpgrade(te, 3, 8, 18));
		this.addSlotToContainer(new SlotUpgrade(te, 4, 8, 36));
		this.addSlotToContainer(new SlotUpgrade(te, 5, 8, 54));

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				int x = 8 + j * 18;
				int y = i * 18 + 84;
				this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, x, y));
			}
		}

		for (int k = 0; k < 9; ++k) {
			this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
		}

		if (playerInv.player instanceof EntityPlayerMP) player = (EntityPlayerMP) playerInv.player;
		else player = null;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (player != null) BetterFurnacesReforged.NETWORK.sendTo(new MessageSync(te), player);
	}


	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.te.isUsableByPlayer(player);
	}
	
	@Override
	@Nullable
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack slotStack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			slotStack = stack.copy();

			if (index >= SLOTS_INVENTORY && index <= SLOTS_HOTBAR + 9) {
				if (TileEntityFurnace.isItemFuel(stack)) {
					int s = te.FUEL();
					if (!mergeItemStack(stack, s, s + 1, false)) { return ItemStack.EMPTY; }
				}
				if (!mergeItemStack(stack, SLOTS_TE, SLOTS_TE + SLOTS_TE_SIZE, false)) { return ItemStack.EMPTY; }
			} else if (index >= SLOTS_HOTBAR && index < SLOTS_HOTBAR + 9) {
				if (!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_INVENTORY + 3 * 9, false)) { return ItemStack.EMPTY; }
			} else if (!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_HOTBAR + 9, true)) { return ItemStack.EMPTY; }

			slot.onSlotChanged();
			if (stack.getCount() == slotStack.getCount()) { return ItemStack.EMPTY; }
			slot.onTake(player, stack);
		}
		return slotStack;
	}
}
