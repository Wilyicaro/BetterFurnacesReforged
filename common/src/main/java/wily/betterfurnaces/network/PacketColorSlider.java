package wily.betterfurnaces.network;

import me.shedaniel.architectury.networking.NetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.items.ColorUpgradeItem;

import java.util.function.Supplier;

public class PacketColorSlider {

	private final int iden;
	private final int index;

	public PacketColorSlider(FriendlyByteBuf buf) {
		index = buf.readInt();
		iden= buf.readInt();
	}

	public void toBytes(ByteBuf buf) {
		buf.writeInt(index);
		buf.writeInt(iden);
	}

	public PacketColorSlider(int index, int iden) {
		this.index = index;
		this.iden = iden;
	}

	public void handle(Supplier<NetworkManager.PacketContext> ctx) {
		ctx.get().queue(() -> {
			ServerPlayer player = (ServerPlayer) ctx.get().getPlayer();
				ItemStack stack = player.getMainHandItem();
				if (stack.getItem() instanceof ColorUpgradeItem) {
					CompoundTag nbt = stack.getOrCreateTag();
					nbt.putInt(iden <= 1 ? "red" : iden < 3 ? "green"  : "blue", index);
					}
		});

	}
}
