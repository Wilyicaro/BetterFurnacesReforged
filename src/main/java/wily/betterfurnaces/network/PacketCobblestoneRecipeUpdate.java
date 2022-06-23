package wily.betterfurnaces.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import wily.betterfurnaces.blockentity.BlockEntityCobblestoneGenerator;

import java.util.function.Supplier;

public class PacketCobblestoneRecipeUpdate {

	private int x;
	private int y;
	private int z;
	private boolean onlyUpdate;

	public PacketCobblestoneRecipeUpdate(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		onlyUpdate = buf.readBoolean();
	}

	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeBoolean(onlyUpdate);
	}

	public PacketCobblestoneRecipeUpdate(BlockPos pos, boolean onlyUpdate) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.onlyUpdate = onlyUpdate;
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			BlockPos pos = new BlockPos(x, y, z);
			BlockEntityCobblestoneGenerator te = (BlockEntityCobblestoneGenerator) player.getLevel().getBlockEntity(pos);
			if (player.level.isLoaded(pos)) {
				te.changeRecipe(true, onlyUpdate);
				te.setChanged();
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
