package wily.betterfurnaces.fabric.mixin;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;

@Mixin(InventoryBlockEntity.class)
public class AddInventoryBlockEntityClient extends BlockEntity implements BlockEntityClientSerializable {
    public AddInventoryBlockEntityClient(BlockEntityType<?> blockEntityType) {
        super(blockEntityType);
    }
    InventoryBlockEntity be = (InventoryBlockEntity)(Object)this;
    @Override
    public void fromClientTag(CompoundTag tag) {
        be.handleUpdateTag(tag);
        level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), getBlockState(), 3);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if(hasLevel() && !level.isClientSide) sync();
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return super.getUpdatePacket();
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        this.setChanged();
        return save(tag);
    }
}
