package wily.betterfurnaces.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.network.NetworkEvent;
import wily.betterfurnaces.items.ColorUpgradeItem;

import java.util.function.Supplier;

public class PacketColorSlider {

    private final int iden;
    private final int index;

    public PacketColorSlider(ByteBuf buf) {
        index = buf.readInt();
        iden = buf.readInt();
    }

    public PacketColorSlider(int index, int iden) {
        this.index = index;
        this.iden = iden;
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(index);
        buf.writeInt(iden);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof ColorUpgradeItem) {
                CompoundNBT nbt = stack.getOrCreateTag();
                if (iden == 1) nbt.putInt("red", index);
                if (iden == 2) nbt.putInt("green", index);
                if (iden == 3) nbt.putInt("blue", index);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
