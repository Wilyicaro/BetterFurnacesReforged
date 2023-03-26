package wily.betterfurnaces.fabric;

import net.fabricmc.api.ClientModInitializer;
import wily.betterfurnaces.client.ClientSide;

public class BetterFurnacesFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientSide.init();
    }
}
