package wily.betterfurnaces.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;
import wily.factoryapi.base.Bearer;

import java.util.List;
import java.util.function.Supplier;

public class PacketSyncAdditionalInt {

	private final BlockPos pos;

	private final int index;

	private final int value;

	public PacketSyncAdditionalInt(FriendlyByteBuf buf) {
		this(buf.readBlockPos(),buf.readInt(), buf.readInt());
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeInt(index);
		buf.writeInt(value);
	}
	public PacketSyncAdditionalInt(BlockPos pos, List<Bearer<Integer>> additionalInts, Bearer<Integer> oldValue, int value) {
		this(pos,additionalInts.indexOf(oldValue),value);
		if (oldValue.get() != value) oldValue.set(value);
	}
	public PacketSyncAdditionalInt(BlockPos pos, int index, int value) {
		this.pos = pos;
		this.index = index;
		this.value = value;
	}

	public void handle(Supplier<NetworkManager.PacketContext> ctx) {
		ctx.get().queue(() -> {
			Player player = ctx.get().getPlayer();
			if (player.level.isLoaded(pos) && player.level.getBlockEntity(pos) instanceof InventoryBlockEntity i) {
				i.additionalSyncInts.get(index).set(value);
				i.setChanged();
			}
		});
	}
}
