package wily.betterfurnaces.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import wily.betterfurnaces.blockentity.AbstractSmeltingBlockEntity;
import wily.betterfurnaces.blocks.AbstractForgeBlock;

import java.util.function.Supplier;

public class PacketOrientationButton {

private BlockPos pos;
	private boolean state;

	public PacketOrientationButton(FriendlyByteBuf buf) {
		this(buf.readBlockPos(), buf.readBoolean());
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeBoolean(state);
	}

	public PacketOrientationButton(BlockPos pos, boolean state) {
		this.pos = pos;
		this.state = state;
	}

	public void handle(Supplier<NetworkManager.PacketContext> ctx) {
		ctx.get().queue(() -> {
			ServerPlayer player = (ServerPlayer) ctx.get().getPlayer();
			AbstractSmeltingBlockEntity be = (AbstractSmeltingBlockEntity) player.getLevel().getBlockEntity(pos);
			if (player.level.isLoaded(pos)) {
				player.level.setBlock(pos, player.level.getBlockState(pos).setValue(AbstractForgeBlock.SHOW_ORIENTATION, state),3);
				be.getLevel().setBlock(pos, be.getLevel().getBlockState(pos), 2, 3);
				be.setChanged();
			}
		});
	}
}
