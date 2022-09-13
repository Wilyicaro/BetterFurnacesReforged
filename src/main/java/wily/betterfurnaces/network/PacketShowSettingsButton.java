package wily.betterfurnaces.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import wily.betterfurnaces.blockentity.AbstractSmeltingBlockEntity;

import java.util.function.Supplier;

public class PacketShowSettingsButton {

	private int x;
	private int y;
	private int z;
	private int set;

	public PacketShowSettingsButton(ByteBuf buf) {
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

	public PacketShowSettingsButton(BlockPos pos, int set) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.set = set;
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			BlockPos pos = new BlockPos(x, y, z);
			AbstractSmeltingBlockEntity be = (AbstractSmeltingBlockEntity) player.getLevel().getBlockEntity(pos);
			if (player.level.isLoaded(pos)) {
				be.show_inventory_settings = set;
				be.getLevel().markAndNotifyBlock(pos, player.getLevel().getChunkAt(pos), be.getLevel().getBlockState(pos).getBlock().defaultBlockState(), be.getLevel().getBlockState(pos), 2, 3);
				be.setChanged();
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
