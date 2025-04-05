package wily.betterfurnaces.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.factoryapi.base.network.CommonNetwork;

import java.util.function.Supplier;

public record OrientationSyncPayload(BlockPos pos, boolean state) implements CommonNetwork.Payload {
	public static final CommonNetwork.Identifier<OrientationSyncPayload> ID = CommonNetwork.Identifier.create(BetterFurnacesReforged.createModLocation("orientation_sync"), OrientationSyncPayload::new);

	public OrientationSyncPayload(CommonNetwork.PlayBuf buf) {
		this(buf.get().readBlockPos(), buf.get().readBoolean());
	}

	@Override
	public void apply(Context context) {
		context.executor().execute(() -> {
			SmeltingBlockEntity be = (SmeltingBlockEntity) context.player().level().getBlockEntity(pos);
			if (context.player().level().isLoaded(pos)) {
				be.showOrientation = state;
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
		buf.get().writeBoolean(state);
	}
}
