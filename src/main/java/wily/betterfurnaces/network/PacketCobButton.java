package wily.betterfurnaces.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import wily.betterfurnaces.tileentity.BlockCobblestoneGeneratorTile;

import java.util.function.Supplier;

public class PacketCobButton {

	private int x;
	private int y;
	private int z;
	private int index;
	private int set;

	public PacketCobButton(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		index = buf.readInt();
		set = buf.readInt();
	}

	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(index);
		buf.writeInt(set);
	}

	public PacketCobButton(BlockPos pos, int index) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.index = index;
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			BlockPos pos = new BlockPos(x, y, z);
			BlockCobblestoneGeneratorTile te = (BlockCobblestoneGeneratorTile) player.getLevel().getBlockEntity(pos);
			if (player.level.isLoaded(pos)) {
				te.resultType = index;
				te.getLevel().markAndNotifyBlock(pos, player.getLevel().getChunkAt(pos), te.getLevel().getBlockState(pos).getBlock().defaultBlockState(), te.getLevel().getBlockState(pos), 2, 3);
				te.setChanged();
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
