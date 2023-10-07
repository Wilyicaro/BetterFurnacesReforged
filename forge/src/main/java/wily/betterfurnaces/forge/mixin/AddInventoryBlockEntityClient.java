package wily.betterfurnaces.forge.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;

@Mixin(InventoryBlockEntity.class)
public class AddInventoryBlockEntityClient extends BlockEntity {
    public AddInventoryBlockEntityClient(BlockEntityType<?> arg) {
        super(arg);

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
        if (hasLevel())
            level.markAndNotifyBlock(worldPosition, level.getChunkAt(worldPosition), level.getBlockState(worldPosition).getBlock().defaultBlockState(), level.getBlockState(worldPosition), 2, 3);
    }
}
