package wily.betterfurnaces.quilt;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import wily.betterfurnaces.fabriclike.BetterFurnacesFabricLikeClient;

public class BetterFurnacesQuiltClient implements ClientModInitializer {

    @Override
    public void onInitializeClient(ModContainer mod) {
        BetterFurnacesFabricLikeClient.init();
    }
}
