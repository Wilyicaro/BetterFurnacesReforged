package wily.betterfurnaces.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.FactoryUpgradeSettings;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.factoryapi.base.network.CommonNetwork;

import java.util.function.Supplier;

public record SettingsSyncPayload(BlockPos pos, FactoryUpgradeSettings.Type settingType, int[] indexes, int set) implements CommonNetwork.Payload {
	public static final CommonNetwork.Identifier<SettingsSyncPayload> ID = CommonNetwork.Identifier.create(BetterFurnacesReforged.createModLocation("furnace_settings_sync"), SettingsSyncPayload::new);

	public SettingsSyncPayload(CommonNetwork.PlayBuf buf) {
		this(buf.get().readBlockPos(), buf.get().readEnum(FactoryUpgradeSettings.Type.class), buf.get().readVarIntArray(), buf.get().readVarInt());
	}

	public SettingsSyncPayload(BlockPos pos, FactoryUpgradeSettings.Type settingType, int index, int set) {
		this(pos, settingType, new int[]{index},set);
	}

	@Override
	public void apply(Context context) {
		context.executor().execute(() -> {
			SmeltingBlockEntity be = (SmeltingBlockEntity) context.player().level().getBlockEntity(pos);
			if (context.player().level().isLoaded(pos)) {
				for (int index : indexes)
					be.furnaceSettings.set(settingType, index, set);
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
		buf.get().writeEnum(settingType);
		buf.get().writeVarIntArray(indexes);
		buf.get().writeVarInt(set);
	}
}

