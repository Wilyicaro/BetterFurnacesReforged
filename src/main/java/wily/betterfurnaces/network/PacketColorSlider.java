package wily.betterfurnaces.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import wily.betterfurnaces.items.ColorUpgradeItem;

import java.util.function.Supplier;

public class PacketColorSlider {

	private int iden;
	private int index;

	public PacketColorSlider(ByteBuf buf) {
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

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
				ItemStack stack = player.getMainHandItem();
				if (stack.getItem() instanceof ColorUpgradeItem) {
					CompoundTag nbt = stack.getOrCreateTag();
					nbt.putInt(iden <= 1 ? "red" : iden < 3 ? "green"  : "blue", index);
					}
		});
		ctx.get().setPacketHandled(true);
	}
}
