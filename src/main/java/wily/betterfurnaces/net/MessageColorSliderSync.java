package wily.betterfurnaces.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wily.betterfurnaces.items.ItemUpgradeColor;

public class MessageColorSliderSync implements IMessage {

	protected int iden;
	protected int index;


	@Override
	public void fromBytes(ByteBuf buf) {
		this.index = buf.readInt();
		this.iden = buf.readInt();
	}

	public void toBytes(ByteBuf buf) {
		buf.writeInt(index);
		buf.writeInt(iden);
	}
	public MessageColorSliderSync() {
	}
	public MessageColorSliderSync(int index, int iden) {
		this.index = index;
		this.iden = iden;
	}
	public static class Handler implements IMessageHandler<MessageColorSliderSync, IMessage> {

		@Override
		public IMessage onMessage(MessageColorSliderSync message, MessageContext ctx) {

				EntityPlayerMP player = ctx.getServerHandler().player;
				ItemStack stack = player.getHeldItemMainhand();
				if (stack.getItem() instanceof ItemUpgradeColor) {
					NBTTagCompound nbt = stack.getTagCompound();
					if (message.iden ==  1) nbt.setInteger("red", message.index);
					if (message.iden ==  2) nbt.setInteger("green", message.index);
					if (message.iden ==  3) nbt.setInteger("blue", message.index);
				}
			return null;
		}

	}
}
