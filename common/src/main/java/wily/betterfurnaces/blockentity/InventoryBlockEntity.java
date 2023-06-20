package wily.betterfurnaces.blockentity;

import me.shedaniel.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.base.IPlatformHandlerApi;
import wily.factoryapi.base.IPlatformItemHandler;
import wily.factoryapi.base.Storages;

import java.util.Optional;

public abstract class InventoryBlockEntity extends BlockEntity implements TickableBlockEntity,IInventoryBlockEntity, ExtendedMenuProvider, Nameable {

    protected Component name;

    public IPlatformItemHandler inventory;

    public InventoryBlockEntity(BlockEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        inventory = FactoryAPIPlatform.getItemHandlerApi(getInventorySize(),this);
        inventory.setExtractableSlots(this::IcanExtractItem);
        inventory.setInsertableSlots(this::IisItemValidForSlot);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        this.setChanged();
        return new ClientboundBlockEntityDataPacket(getBlockPos(), -1, getUpdateTag());
    }
    public void handleUpdateTag(CompoundTag tag){
        if (tag != null)
            load(getBlockState(),tag);
        setChanged();


    }
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt){
        CompoundTag tag = pkt.getTag();
        this.load(getBlockState(),tag);
        this.setChanged();
        level.setBlock(worldPosition, level.getBlockState(worldPosition), 2, 3);
    }
    public void updateBlockState(){
        level.sendBlockUpdated(getBlockPos(), level.getBlockState(getBlockPos()), getBlockState(), 2);
    }
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        save(tag);
        return tag;
    }
    public <T extends IPlatformHandlerApi> Optional<T> getStorage(Storages.Storage<T> storage, Direction facing){
        return Optional.empty();
    }
    @Override
    public void saveExtraData(FriendlyByteBuf buf) {
        buf.writeBlockPos(getBlockPos());
    }

    public void setCustomName(Component name) {
        this.name = name;
    }

    @Override
    public Component getName() {
        return (this.name != null ? this.name : getDisplayName());
    }



    public void breakDurabilityItem(ItemStack stack){
        if (stack.isDamageableItem()) {
            stack.hurt(1, null, null);
            if (stack.getDamageValue() >= stack.getMaxDamage()) {
                stack.shrink(1);
                this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }

    }
    public void syncAdditionalMenuData(AbstractContainerMenu menu, Player player){

    }
    @Override
    public void load(BlockState blockState,CompoundTag tag) {
        super.load(blockState,tag);
        inventory.deserializeTag(tag.getCompound("inventory"));
        if (tag.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(tag.getString("CustomName"));
        }
    }


    @Override
    public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        tag.put("inventory", inventory.serializeTag());
        if (this.name != null) {
            tag.putString("CustomName", Component.Serializer.toJson(this.name));
        }
        return tag;
    }

    @Override
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
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


    public @NotNull IPlatformItemHandler getInv() {
        return inventory;
    }


    public abstract boolean IcanExtractItem(int index, ItemStack stack);
}
