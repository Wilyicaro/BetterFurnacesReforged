package wily.betterfurnaces.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import wily.betterfurnaces.tileentity.AbstractSmeltingTileEntity;

import java.util.function.Supplier;

public class PacketShowSettingsButton {

    private final int x;
    private final int y;
    private final int z;
    private final int set;

    public PacketShowSettingsButton(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        set = buf.readInt();
    }

    public PacketShowSettingsButton(BlockPos pos, int set) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.set = set;
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(set);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            BlockPos pos = new BlockPos(x, y, z);
            AbstractSmeltingTileEntity te = (AbstractSmeltingTileEntity) player.getLevel().getBlockEntity(pos);
            if (player.level.isLoaded(pos)) {
                te.show_inventory_settings = set;
                te.getLevel().markAndNotifyBlock(pos, player.getLevel().getChunkAt(pos), te.getLevel().getBlockState(pos).getBlock().defaultBlockState(), te.getLevel().getBlockState(pos), 2, 3);
                te.setChanged();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
