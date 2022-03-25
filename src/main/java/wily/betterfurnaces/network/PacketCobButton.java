package wily.betterfurnaces.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import wily.betterfurnaces.tileentity.BlockCobblestoneGeneratorTile;

import java.util.function.Supplier;

public class PacketCobButton {

		private int x;
		private int y;
		private int z;

		public PacketCobButton(ByteBuf buf) {
			x = buf.readInt();
			y = buf.readInt();
			z = buf.readInt();
		}

		public void toBytes(ByteBuf buf) {
			buf.writeInt(x);
			buf.writeInt(y);
			buf.writeInt(z);
		}

		public PacketCobButton(BlockPos pos) {
			this.x = pos.getX();
			this.y = pos.getY();
			this.z = pos.getZ();
		}

		public void handle(Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				ServerPlayerEntity player = ctx.get().getSender();
				BlockPos pos = new BlockPos(x, y, z);
				BlockCobblestoneGeneratorTile te = (BlockCobblestoneGeneratorTile) player.getLevel().getBlockEntity(pos);
				if (player.level.isLoaded(pos)) {
					te.changeRecipe(true);
					te.setChanged();
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}

