package wily.betterfurnaces.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import wily.betterfurnaces.blockentity.BlockEntityForgeBase;

import java.util.function.Supplier;

public class PacketForgeSettingsButton {

	private int x;
	private int y;
	private int z;
	private int index;
	private int set;

	public PacketForgeSettingsButton(ByteBuf buf) {
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

	public PacketForgeSettingsButton(BlockPos pos, int index, int set) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.index = index;
		this.set = set;
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			BlockPos pos = new BlockPos(x, y, z);
			BlockEntityForgeBase te = (BlockEntityForgeBase) player.getLevel().getBlockEntity(pos);
			if (player.level.isLoaded(pos)) {
				te.furnaceSettings.set(index, set);
				te.getLevel().markAndNotifyBlock(pos, player.getLevel().getChunkAt(pos), te.getLevel().getBlockState(pos).getBlock().defaultBlockState(), te.getLevel().getBlockState(pos), 2, 3);
				te.setChanged();
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
