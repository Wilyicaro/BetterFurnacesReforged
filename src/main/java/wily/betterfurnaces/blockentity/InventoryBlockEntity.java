package wily.betterfurnaces.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class InventoryBlockEntity extends BlockEntity implements IInventoryBlockEntity, WorldlyContainer, MenuProvider, Nameable {

    public ItemStackHandler inventory;
    public NonNullList<ItemStack> inventoryList;
    public boolean loadEmpty = false;
    protected Component name;
    public int getInvSize;

    public InventoryBlockEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state, int sizeInventory) {
        super(tileEntityTypeIn, pos, state);
        this.inventory = new ItemStackHandler(sizeInventory) {
            @Nonnull
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return IisItemValidForSlot(slot, stack);
            }

            @Override
            protected void onContentsChanged(final int slot) {
                super.onContentsChanged(slot);
                    if (!loadEmpty)
                    inventoryList.set(slot, getStackInSlot(slot));
                setChanged();
            }
        };
        getInvSize = sizeInventory;
        inventoryList = NonNullList.withSize(inventory.getSlots(), ItemStack.EMPTY);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        this.setChanged();
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt){
        CompoundTag tag = pkt.getTag();
        this.load(tag);
        this.setChanged();
        level.markAndNotifyBlock(worldPosition, level.getChunkAt(worldPosition), level.getBlockState(worldPosition).getBlock().defaultBlockState(), level.getBlockState(worldPosition), 2, 3);
    }
    public void updateBlockState(){
        level.sendBlockUpdated(getBlockPos(), level.getBlockState(getBlockPos()), getBlockState(), 2);
    }
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }


    public void setCustomName(Component name) {
        this.name = name;
    }

    @Override
    public Component getName() {
        return (this.name != null ? this.name : new TextComponent(IgetName()));
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return this.IgetSlotsForFace(side);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return IisItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return IisItemValidForSlot(i, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return IcanExtractItem(i, itemStack, direction);
    }

    @Override
    public int getContainerSize() {
        return this.inventoryList.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : inventoryList) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void breakDurabilityItem(ItemStack stack){
        if (stack.isDamageableItem()) {
            stack.hurt(1, null, null);
        }
            if (stack.isDamageableItem() && stack.getDamageValue() >= stack.getMaxDamage()) {
                stack.shrink(1);
                this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int i, int i1) {
        return inventory.extractItem(i, i1,false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return inventory.extractItem(i, 1,false);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack itemstack = this.inventory.getStackInSlot(index);
        boolean flag = !stack.isEmpty() && stack.sameItem(itemstack) && ItemStack.tagMatches(stack, itemstack);
        this.inventory.setStackInSlot(index, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    public int getMaxStackSize() {
        return WorldlyContainer.super.getMaxStackSize();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        for (int slot : new int[inventory.getSlots()])
            inventoryList.set(slot, inventory.getStackInSlot(slot));
        inventory.deserializeNBT(tag.getCompound("inventory"));
        if (tag.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(tag.getString("CustomName"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inventory", inventory.serializeNBT());
        if (this.name != null) {
            tag.putString("CustomName", Component.Serializer.toJson(this.name));
        }
    }

    @Override
    public boolean stillValid(Player playerEntity) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return !(playerEntity.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public String IgetName() {
        return getBlockState().getBlock().getName().getString();
    }

    @Override
    public boolean hasCustomName() {
        return this.name != null;
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return this.name;
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    public @NotNull ItemStackHandler getInv() {
        return inventory;
    }
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return IcreateMenu(i, playerInventory, playerEntity);
    }

    @Override
    public void clearContent() {
        for (int slot : new int[inventory.getSlots()])
        this.inventory.setStackInSlot(slot, ItemStack.EMPTY);
    }
}
