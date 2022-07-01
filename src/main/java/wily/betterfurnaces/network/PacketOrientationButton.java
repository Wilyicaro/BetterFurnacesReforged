package wily.betterfurnaces.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import wily.betterfurnaces.blocks.AbstractForgeBlock;
import wily.betterfurnaces.tileentity.AbstractSmeltingTileEntity;

import java.util.function.Supplier;

public class PacketOrientationButton {

    private final int x;
    private final int y;
    private final int z;
    private final boolean state;

    public PacketOrientationButton(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        state = buf.readBoolean();
    }

    public PacketOrientationButton(BlockPos pos, boolean state) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.state = state;
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeBoolean(state);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            BlockPos pos = new BlockPos(x, y, z);
            AbstractSmeltingTileEntity te = (AbstractSmeltingTileEntity) player.getLevel().getBlockEntity(pos);
            if (player.level.isLoaded(pos)) {
                player.level.setBlock(pos, player.level.getBlockState(pos).setValue(AbstractForgeBlock.SHOW_ORIENTATION, state), 3);
                te.getLevel().markAndNotifyBlock(pos, player.getLevel().getChunkAt(pos), te.getLevel().getBlockState(pos).getBlock().defaultBlockState(), te.getLevel().getBlockState(pos), 2, 3);
                te.setChanged();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
