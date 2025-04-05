package wily.betterfurnaces.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.CobblestoneGeneratorBlockEntity;
import wily.factoryapi.base.network.CommonNetwork;

import java.util.function.Supplier;

public record CobblestoneGeneratorSyncPayload(BlockPos pos, Sync sync) implements CommonNetwork.Payload {
	public enum Sync {
		NEXT_RECIPE,PREVIOUS_RECIPE,AUTO_OUTPUT,DISABLE_AUTO_OUTPUT;
	}

	public static final CommonNetwork.Identifier<CobblestoneGeneratorSyncPayload> ID = CommonNetwork.Identifier.create(BetterFurnacesReforged.createModLocation("cobblestone_generator_sync"), CobblestoneGeneratorSyncPayload::new);

	public CobblestoneGeneratorSyncPayload(CommonNetwork.PlayBuf buf) {
		this(buf.get().readBlockPos(), buf.get().readEnum(Sync.class));
	}

	@Override
	public void apply(Context context) {
		context.executor().execute(() -> {
			CobblestoneGeneratorBlockEntity be = (CobblestoneGeneratorBlockEntity) context.player().level().getBlockEntity(pos);
			if (context.player().level().isLoaded(pos)) {
				switch (sync){
					case NEXT_RECIPE,PREVIOUS_RECIPE -> {
						be.changeRecipe(sync == Sync.NEXT_RECIPE);
						be.setChanged();
					}
					case AUTO_OUTPUT,DISABLE_AUTO_OUTPUT -> be.autoOutput = sync == Sync.AUTO_OUTPUT;
				}
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
		buf.get().writeEnum(sync);
	}
}
