package wily.betterfurnaces.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import wily.betterfurnaces.tileentity.BlockForgeTileBase;

import java.util.function.Supplier;

public class PacketForgeShowSettingsButton {

	private int x;
	private int y;
	private int z;
	private int set;

	public PacketForgeShowSettingsButton(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		set = buf.readInt();
	}

	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(set);
	}

	public PacketForgeShowSettingsButton(BlockPos pos, int set) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.set = set;
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			BlockPos pos = new BlockPos(x, y, z);
			BlockForgeTileBase te = (BlockForgeTileBase) player.getLevel().getBlockEntity(pos);
			if (player.level.isLoaded(pos)) {
				te.show_inventory_settings = set;
				te.getLevel().markAndNotifyBlock(pos, player.getLevel().getChunkAt(pos), te.getLevel().getBlockState(pos).getBlock().defaultBlockState(), te.getLevel().getBlockState(pos), 2, 3);
				te.setChanged();
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
