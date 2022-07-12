package wily.betterfurnaces.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import wily.betterfurnaces.blockentity.AbstractCobblestoneGeneratorBlockEntity;

import java.util.function.Supplier;

public class PacketCobblestoneRecipeUpdate {

	private int x;
	private int y;
	private int z;

	public PacketCobblestoneRecipeUpdate(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

	public PacketCobblestoneRecipeUpdate(BlockPos pos, boolean onlyUpdate) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			BlockPos pos = new BlockPos(x, y, z);
			AbstractCobblestoneGeneratorBlockEntity te = (AbstractCobblestoneGeneratorBlockEntity) player.getLevel().getBlockEntity(pos);
			if (player.level.isLoaded(pos)) {
				te.changeRecipe(true);
				te.setChanged();
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
