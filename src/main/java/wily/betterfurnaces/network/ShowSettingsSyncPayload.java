package wily.betterfurnaces.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.factoryapi.base.network.CommonNetwork;

import java.util.function.Supplier;

public record ShowSettingsSyncPayload(BlockPos pos, int set) implements CommonNetwork.Payload {
	public static final CommonNetwork.Identifier<ShowSettingsSyncPayload> ID = CommonNetwork.Identifier.create(BetterFurnacesReforged.createModLocation("show_furnace_settings_sync"), ShowSettingsSyncPayload::new);

	public ShowSettingsSyncPayload(CommonNetwork.PlayBuf buf) {
		this(buf.get().readBlockPos(),buf.get().readVarInt());
	}

	@Override
	public void apply(Context context) {
		context.executor().execute(() -> {
			SmeltingBlockEntity be = (SmeltingBlockEntity) context.player().level().getBlockEntity(pos);
			if (context.player().level().isLoaded(pos)) {
				be.showInventorySettings = set;
				be.getLevel().setBlock(pos, be.getLevel().getBlockState(pos), 2, 3);
				be.setChanged();
			}
		});
	}

	@Override
	public CommonNetwork.Identifier<? extends CommonNetwork.Payload> identifier() {
		return ID;
	}

	@Override
	public void encode(CommonNetwork.PlayBuf buf) {
		buf.get().writeBlockPos(pos);
		buf.get().writeVarInt(set);
	}
}
