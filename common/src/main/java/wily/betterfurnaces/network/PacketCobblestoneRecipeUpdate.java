package wily.betterfurnaces.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import wily.betterfurnaces.blockentity.AbstractCobblestoneGeneratorBlockEntity;

import java.util.function.Supplier;

public class PacketCobblestoneRecipeUpdate {

private BlockPos pos;

	public PacketCobblestoneRecipeUpdate(FriendlyByteBuf buf) {
		this(buf.readBlockPos());
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
	}

	public PacketCobblestoneRecipeUpdate(BlockPos pos) {
		this.pos = pos;
	}

	public void handle(Supplier<NetworkManager.PacketContext> ctx) {
		ctx.get().queue(() -> {
			ServerPlayer player = (ServerPlayer) ctx.get().getPlayer();
			AbstractCobblestoneGeneratorBlockEntity be = (AbstractCobblestoneGeneratorBlockEntity) player.getLevel().getBlockEntity(pos);
			if (player.level.isLoaded(pos)) {
				be.changeRecipe(true);
				be.setChanged();
			}
		});
	}
}
