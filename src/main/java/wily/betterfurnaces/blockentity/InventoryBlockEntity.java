package wily.betterfurnaces.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
//? if >=1.20.5 {
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.item.component.ItemContainerContents;
//?}
//? if >=1.21.5 {
import net.minecraft.core.component.DataComponentGetter;
//?}
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.factoryapi.base.*;
import wily.factoryapi.util.CompoundTagUtil;

public abstract class InventoryBlockEntity extends BlockEntity implements IInventoryBlockEntity, MenuProvider, Nameable, IFactoryStorage {
    private final NonNullList<FactoryItemSlot> slots = createSlots(null);
    public final FactoryItemHandler inventory = new FactoryItemHandler(getInventorySize(),this, TransportState.EXTRACT_INSERT){
        @Override
        public boolean canTakeItem(Container container, int i, ItemStack itemStack) {
            return super.canTakeItem(container, i, itemStack) && IcanExtractItem(i,itemStack);
        }

        @Override
        public boolean canPlaceItem(int slot, @NotNull ItemStack stack) {
            return super.canPlaceItem(slot, stack) && InventoryBlockEntity.this.getSlots().get(slot).mayPlace(stack);
        }
    };
    private LockCode lockKey = LockCode.NO_LOCK;
    private Component name;

    public InventoryBlockEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Override
    public NonNullList<FactoryItemSlot> getSlots() {
        return slots;
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
            if (stack.getDamageValue() >= stack.getMaxDamage()) {
                stack.shrink(1);
                this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            *///?} else {
            if (level instanceof ServerLevel serverLevel) stack.hurtAndBreak(1, serverLevel, null, i-> this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.ITEM_BREAK/*? if >=1.21.5 {*/.value()/*?}*/, SoundSource.BLOCKS, 1.0F, 1.0F));
            //?}
        }
    }

    public void syncAdditionalMenuData(AbstractContainerMenu menu, Player player){
    }

    @Override
    public void /*? if <1.20.5 {*//*load(CompoundTag tag)*//*?} else {*/loadAdditional(CompoundTag tag, HolderLookup.Provider provider)/*?}*/ {
        super./*? if <1.20.5 {*//*load(tag)*//*?} else {*/loadAdditional(tag, provider)/*?}*/;
        inventory.deserializeTag(CompoundTagUtil.getCompoundTagOrEmpty(tag, "inventory"));
        this.lockKey = LockCode.fromTag(tag/*? if >=1.21.2 {*/, provider/*?}*/);
        CompoundTagUtil.getString(tag, "CustomName").ifPresent(nameJson-> this.name = Component.Serializer.fromJson(nameJson/*? if >=1.20.5 {*/,provider/*?}*/));
    }

    @Override
    public void saveAdditional(CompoundTag tag/*? if >=1.20.5 {*/, HolderLookup.Provider provider/*?}*/) {
        super.saveAdditional(tag/*? if >=1.20.5 {*/, provider/*?}*/);
        tag.put("inventory", inventory.serializeTag());
        this.lockKey.addToTag(tag/*? if >=1.21.2 {*/, provider/*?}*/);
        if (this.name != null) {
            tag.putString("CustomName", Component.Serializer.toJson(name/*? if >=1.20.5 {*/,provider/*?}*/));
        }
    }

    public boolean canOpen(Player arg) {
        return BaseContainerBlockEntity.canUnlock(arg, this.lockKey, this.getDisplayName());
    }

    protected abstract AbstractContainerMenu createMenu(int i, Inventory arg);


    public AbstractContainerMenu createMenu(int i, Inventory arg, Player arg2) {
        return this.canOpen(arg2) ? this.createMenu(i, arg) : null;
    }

    public void setCustomName(Component name) {
        this.name = name;
    }

    @Override
    public Component getName() {
        return (this.name != null ? this.name : getDisplayName());
    }

    @Override
    public Component getDisplayName() {
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

    //? if >=1.20.5 {
    protected void applyImplicitComponents(/*? if <1.21.5 {*//*BlockEntity.DataComponentInput*//*?} else {*/DataComponentGetter/*?}*/ data) {
        super.applyImplicitComponents(data);
        setCustomName(data.get(DataComponents.CUSTOM_NAME));
        this.lockKey = data.getOrDefault(DataComponents.LOCK, LockCode.NO_LOCK);
        data.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(inventory.getItems());
    }

    protected void collectImplicitComponents(DataComponentMap.Builder arg) {
        super.collectImplicitComponents(arg);
        arg.set(DataComponents.CUSTOM_NAME, this.name);
        if (!this.lockKey.equals(LockCode.NO_LOCK)) {
            arg.set(DataComponents.LOCK, this.lockKey);
        }

        arg.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(inventory.getItems()));
    }

    public void removeComponentsFromTag(CompoundTag arg) {
        arg.remove("CustomName");
        arg.remove("lock");
    }
    //?}
}
