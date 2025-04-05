package wily.betterfurnaces.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.factoryapi.base.FactoryStorage;
import wily.factoryapi.base.network.CommonNetwork;
import wily.factoryapi.util.FluidInstance;

import java.util.function.Supplier;

public record FluidSyncPayload(BlockPos pos, FluidInstance fluid) implements CommonNetwork.Payload {
    public static final CommonNetwork.Identifier<FluidSyncPayload> ID = CommonNetwork.Identifier.create(BetterFurnacesReforged.createModLocation("fluid_sync"), FluidSyncPayload::new);

    public FluidSyncPayload(CommonNetwork.PlayBuf buf) {
        this(buf.get().readBlockPos(), FluidInstance.decode(buf.get()));
    }

    @Override
    public void apply(Context context) {
        context.executor().execute(() -> {
            if (context.player().level().isLoaded(pos) && context.player().level().getBlockEntity(pos) instanceof SmeltingBlockEntity be) {
                be.getStorage(FactoryStorage.FLUID).ifPresent(t -> t.setFluid(fluid));
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
        FluidInstance.encode(buf.get(), fluid);
    }
}
