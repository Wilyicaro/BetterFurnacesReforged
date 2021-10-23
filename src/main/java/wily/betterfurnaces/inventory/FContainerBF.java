package wily.betterfurnaces.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidUtil;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.items.ItemUpgrade;
import wily.betterfurnaces.net.MessageSync;
import wily.betterfurnaces.net.MessageSyncTF;
import wily.betterfurnaces.tile.TileEntityForge;

import javax.annotation.Nullable;

public class FContainerBF extends Container {
	public static int GUIID = 2;
	public static final int SLOTS_TE = 0;
	public static final int SLOTS_TE_SIZE = 14;
	public static final int SLOTS_INVENTORY = SLOTS_TE_SIZE;
	public static final int SLOTS_HOTBAR = SLOTS_INVENTORY + 3 * 9;

	private final TileEntityForge tf;
	private final EntityPlayerMP player;

	public FContainerBF(InventoryPlayer playerInv, TileEntityForge tf) {
		this.tf = tf;
		this.addSlotToContainer(new SlotFurnaceInput(tf.getInventory(), 0, 27, 62));
		this.addSlotToContainer(new SlotFurnaceInput(tf.getInventory(), 1, 45, 62));
		this.addSlotToContainer(new SlotFurnaceInput(tf.getInventory(), 2, 63, 62));
		this.addSlotToContainer(new SlotFurnaceHeater(tf.getInventory(), 3, 8, 100){
		@Override
		public boolean isItemValid (ItemStack stack) {
			return ((FluidUtil.getFluidContained(stack) != null && TileEntityForge.getFluidBurnTime(FluidUtil.getFluidContained(stack)) > 0) || TileEntityFurnace.isItemFuel(stack));
		}
			});
		this.addSlotToContainer(new SlotFurnaceOutput(playerInv.player, tf.getInventory(), 4, 108, 80));
		this.addSlotToContainer(new SlotFurnaceOutput(playerInv.player, tf.getInventory(), 5, 126, 80));
		this.addSlotToContainer(new SlotFurnaceOutput(playerInv.player, tf.getInventory(), 6, 144, 80));
		this.addSlotToContainer(new SlotUpgrade(tf.getInventory(), 11, 115, 5){
			@Override
			public boolean isItemValid (ItemStack stack){
				return false; }});
		this.addSlotToContainer(new SlotUpgrade(tf.getInventory(), 12, 133, 5){
			@Override
			public boolean isItemValid (ItemStack stack){
				return stack.getItem() == ModObjects.COLOR_UPGRADE; }});
		this.addSlotToContainer(new SlotUpgrade(tf.getInventory(), 13, 151, 5){
			@Override
			public boolean isItemValid (ItemStack stack){
				return stack.getItem() instanceof ItemUpgrade; }});
		this.addSlotToContainer(new SlotUpgrade(tf.getInventory(), 7, 7, 5){
			@Override
			public boolean isItemValid (ItemStack stack){
				return (new ItemStack(ModObjects.ORE_PROCESSING_UPGRADE, (int) (1)).getItem() == stack.getItem() || new ItemStack(ModObjects.ADVANCED_ORE_PROCESSING_UPGRADE, (int) (1)).getItem() == stack.getItem()); }});
		this.addSlotToContainer(new SlotUpgrade(tf.getInventory(), 8, 25, 5){
			@Override
			public boolean isItemValid (ItemStack stack){
				return (new ItemStack(ModObjects.FUEL_EFFICIENCY_UPGRADE, (int) (1)).getItem() == stack.getItem() || new ItemStack(ModObjects.ADVANCED_FUEL_EFFICIENCY_UPGRADE, (int) (1)).getItem() == stack.getItem()); }});
		this.addSlotToContainer(new SlotUpgrade(tf.getInventory(), 9, 43, 5){
			@Override
			public boolean isItemValid (ItemStack stack){
				return false; }});
		this.addSlotToContainer(new SlotUpgrade(tf.getInventory(), 10, 79, 5){
			@Override
			public boolean isItemValid (ItemStack stack){
				return (new ItemStack(ModObjects.LIQUID_FUEL_UPGRADE, (int) (1)).getItem() == stack.getItem() || new ItemStack(ModObjects.ENERGY_UPGRADE, (int) (1)).getItem() == stack.getItem()); }});
		int si;
		int sj;
		for (si = 0; si < 3; ++si)
			for (sj = 0; sj < 9; ++sj)
				this.addSlotToContainer(new Slot(playerInv, sj + (si + 1) * 9, 0 + 8 + sj * 18, 42 + 84 + si * 18));
		for (si = 0; si < 9; ++si)
			this.addSlotToContainer(new Slot(playerInv, si, 0 + 8 + si * 18, 42 + 142));


		if (playerInv.player instanceof EntityPlayerMP) player = (EntityPlayerMP) playerInv.player;
		else player = null;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (player != null) BetterFurnacesReforged.NETWORK.sendTo(new MessageSyncTF(tf), player);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.tf.isUsableByPlayer(player);
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
					int s = TileEntityForge.SLOT_FUEL;
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
