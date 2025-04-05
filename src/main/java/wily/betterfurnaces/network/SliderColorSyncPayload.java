package wily.betterfurnaces.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.items.ColorUpgradeItem;
import wily.factoryapi.base.network.CommonNetwork;

import java.util.function.Supplier;

public record SliderColorSyncPayload(Channel channel, int value) implements CommonNetwork.Payload {
	public static final CommonNetwork.Identifier<SliderColorSyncPayload> ID = CommonNetwork.Identifier.create(BetterFurnacesReforged.createModLocation("slider_color_sync"), SliderColorSyncPayload::new);


	public SliderColorSyncPayload(CommonNetwork.PlayBuf buf) {
		this(buf.get().readEnum(Channel.class), buf.get().readVarInt());
	}

	@Override
	public void apply(Context context) {
		context.executor().execute(() -> {
			ItemStack stack = context.player().getMainHandItem();
			if (stack.getItem() instanceof ColorUpgradeItem) {
				//? if <1.20.5 {
				/*CompoundTag nbt = stack.getOrCreateTag();
				nbt.putInt(channel.id, value);
				*///?} else {
				stack.set(ModObjects.BLOCK_TINT.get(), stack.getOrDefault(ModObjects.BLOCK_TINT.get(), ModObjects.BlockTint.WHITE).withChannel(channel, value));
				//?}
			}
		});
	}

	@Override
	public CommonNetwork.Identifier<? extends CommonNetwork.Payload> identifier() {
		return ID;
	}

	@Override
	public void encode(CommonNetwork.PlayBuf buf) {
		buf.get().writeEnum(channel);
		buf.get().writeVarInt(value);
	}

	public enum Channel {
		R("red"),G("green"),B("blue");
        public final String id;

        Channel(String name){
            this.id = name;
        }
	}

}
