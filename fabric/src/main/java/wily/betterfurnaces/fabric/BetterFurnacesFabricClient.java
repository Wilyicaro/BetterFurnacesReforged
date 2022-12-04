package wily.betterfurnaces.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import wily.betterfurnaces.fabriclike.BetterFurnacesFabricLike;
import wily.betterfurnaces.fabriclike.BetterFurnacesFabricLikeClient;
import wily.betterfurnaces.init.ClientSide;

public class BetterFurnacesFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BetterFurnacesFabricLikeClient.init();
    }
}
