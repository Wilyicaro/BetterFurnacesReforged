package wily.betterfurnaces.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.factoryapi.base.*;
import wily.factoryapi.util.CompoundTagUtil;

public abstract class InventoryBlockEntity extends BaseContainerBlockEntity implements IInventoryBlockEntity, MenuProvider, Nameable, IFactoryStorage {

    public FactoryItemHandler inventory;


    public InventoryBlockEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
        inventory = new FactoryItemHandler(getInventorySize(),this, TransportState.EXTRACT_INSERT){
            @Override
            public boolean canTakeItem(Container container, int i, ItemStack itemStack) {
                return super.canTakeItem(container, i, itemStack) && IcanExtractItem(i,itemStack);
            }
        };
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void updateBlockState(){
        level.sendBlockUpdated(getBlockPos(), level.getBlockState(getBlockPos()), getBlockState(), 2);
    }

    @Override
    public CompoundTag getUpdateTag(/*? if >=1.20.5 {*/HolderLookup.Provider provider/*?}*/) {
        return saveWithoutMetadata(/*? if >=1.20.5 {*/provider/*?}*/);
    }

    public void breakDurabilityItem(ItemStack stack){
        if (!stack.isEmpty() && stack.isDamageableItem()) {
            //? if <1.20.5 {
            /*stack.hurt(1, level.random, null);
            *///?} else {
            if (level instanceof ServerLevel serverLevel) stack.hurtAndBreak(1, serverLevel, null, i-> {});
            //?}
            if (stack.getDamageValue() >= stack.getMaxDamage()) {
                stack.shrink(1);
                this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.ITEM_BREAK/*? if >=1.21.5 {*/.value()/*?}*/, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    public void syncAdditionalMenuData(AbstractContainerMenu menu, Player player){
    }

    @Override
    public void /*? if <1.20.5 {*//*load(CompoundTag tag)*//*?} else {*/loadAdditional(CompoundTag tag, HolderLookup.Provider provider)/*?}*/ {
        super./*? if <1.20.5 {*//*load(tag)*//*?} else {*/loadAdditional(tag, provider)/*?}*/;
        inventory.deserializeTag(CompoundTagUtil.getCompoundTagOrEmpty(tag, "inventory"));
    }

    @Override
    public void saveAdditional(CompoundTag tag/*? if >=1.20.5 {*/, HolderLookup.Provider provider/*?}*/) {
        super.saveAdditional(tag/*? if >=1.20.5 {*/, provider/*?}*/);
        tag.put("inventory", inventory.serializeTag());
    }

    //? if >=1.20.5 {
    @Override
    protected NonNullList<ItemStack> getItems() {
        return inventory.getItems();
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        inventory.getItems().clear();
        inventory.getItems().addAll(nonNullList);
    }
    //?} else {
    /*@Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        return inventory.getItem(i);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        return inventory.removeItem(i, j);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return inventory.removeItemNoUpdate(i);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        inventory.setItem(i, itemStack);
    }

    @Override
    public boolean stillValid(Player player) {
        return inventory.stillValid(player);
    }

    @Override
    public void clearContent() {
        inventory.clearContent();
    }
    *///?}

    @Override
    public int getContainerSize() {
        return inventory.getContainerSize();
    }

    @Override
    protected Component getDefaultName() {
        return getBlockState().getBlock().getName();
    }

    public @NotNull IPlatformItemHandler getInv() {
        return inventory;
    }

    public void onRemoved(boolean withContents){
        if (withContents) Containers.dropContents(level, getBlockPos(), inventory);
    }
    //? if >=1.21.5 {
    @Override
    public void preRemoveSideEffects(BlockPos blockPos, BlockState blockState) {
    }
    //?}


    public abstract boolean IcanExtractItem(int index, ItemStack stack);
}
