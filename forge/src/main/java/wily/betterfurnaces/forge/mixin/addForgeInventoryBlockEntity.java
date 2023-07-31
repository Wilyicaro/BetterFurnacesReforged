package wily.betterfurnaces.forge.mixin;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;
import wily.factoryapi.base.IPlatformHandlerApi;
import wily.factoryapi.forge.base.CapabilityUtil;

import java.util.Optional;

@Mixin(InventoryBlockEntity.class)
public class addForgeInventoryBlockEntity extends BlockEntity {
    public addForgeInventoryBlockEntity(BlockEntityType<?> arg) {
        super(arg);

    }
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction direction) {

        InventoryBlockEntity be = ((InventoryBlockEntity)(Object) this);
        Optional<? extends IPlatformHandlerApi> storage =  be.getStorage(CapabilityUtil.capabilityToStorage(cap),direction);
        if (!this.isRemoved() && CapabilityUtil.capabilityToStorage(cap) != null && storage.isPresent())
            return LazyOptional.of(()->storage.get().getHandler()).cast();
        return super.getCapability(cap,direction);
    }
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt){
        CompoundTag tag = pkt.getTag();
        this.load(level.getBlockState(worldPosition),tag);
        this.setChanged();
        if (hasLevel()) level.markAndNotifyBlock(worldPosition, level.getChunkAt(worldPosition), level.getBlockState(worldPosition).getBlock().defaultBlockState(), level.getBlockState(worldPosition), 2, 3);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        level.markAndNotifyBlock(worldPosition, level.getChunkAt(worldPosition), level.getBlockState(worldPosition).getBlock().defaultBlockState(), level.getBlockState(worldPosition), 2, 3);
    }
}
