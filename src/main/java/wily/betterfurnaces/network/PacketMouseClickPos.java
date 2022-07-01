package wily.betterfurnaces.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.tileentity.AbstractSmeltingTileEntity;

import java.util.function.Supplier;

public class PacketMouseClickPos {

    private final int x;
    private final int y;
    private final int z;
    private final int mouseX;
    private final int mouseY;

    public PacketMouseClickPos(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        mouseX = buf.readInt();
        mouseY = buf.readInt();
    }

    public PacketMouseClickPos(BlockPos pos, int mouseX, int mouseY) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(mouseX);
        buf.writeInt(mouseY);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            BlockPos pos = new BlockPos(x, y, z);
            AbstractSmeltingTileEntity te = (AbstractSmeltingTileEntity) player.getLevel().getBlockEntity(pos);
            if (player.level.isLoaded(pos)) {
                if (te.hasUpgradeType(Registration.FACTORY.get())) {
                    ItemStack stack = te.getUpgradeTypeSlotItem(Registration.FACTORY.get());
                    CompoundNBT nbt = stack.getOrCreateTag();
                    int[] mousePos = new int[]{mouseX, mouseY};
                    nbt.putIntArray("mousePos", mousePos);
                    stack.setTag(nbt);
                    te.getLevel().markAndNotifyBlock(pos, player.getLevel().getChunkAt(pos), te.getLevel().getBlockState(pos).getBlock().defaultBlockState(), te.getLevel().getBlockState(pos), 2, 3);
                    te.setChanged();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
