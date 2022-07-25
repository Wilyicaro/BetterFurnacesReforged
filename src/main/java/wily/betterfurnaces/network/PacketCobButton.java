package wily.betterfurnaces.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import wily.betterfurnaces.tileentity.AbstractCobblestoneGeneratorTileEntity;

import java.util.function.Supplier;

public class PacketCobButton {

    private final int x;
    private final int y;
    private final int z;
    private final boolean onlyUpdate;

    public PacketCobButton(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        onlyUpdate = buf.readBoolean();
    }

    public PacketCobButton(BlockPos pos, boolean onlyUpdate) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.onlyUpdate = onlyUpdate;
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeBoolean(onlyUpdate);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            BlockPos pos = new BlockPos(x, y, z);
            AbstractCobblestoneGeneratorTileEntity te = (AbstractCobblestoneGeneratorTileEntity) player.getLevel().getBlockEntity(pos);
            if (player.level.isLoaded(pos)) {
                te.changeRecipe(true);
                te.setChanged();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

