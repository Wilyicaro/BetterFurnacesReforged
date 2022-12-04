package wily.betterfurnaces.quilt;


import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import wily.betterfurnaces.fabriclike.BetterFurnacesFabricLike;

public class BetterFurnacesQuilt implements ModInitializer {

    @Override
    public void onInitialize(ModContainer mod) {
        BetterFurnacesFabricLike.init();
    }
}
