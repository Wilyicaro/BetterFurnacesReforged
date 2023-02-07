package wily.betterfurnaces.fabric;

import wily.betterfurnaces.fabriclike.BetterFurnacesFabricLike;
import net.fabricmc.api.ModInitializer;


public class BetterFurnacesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        BetterFurnacesFabricLike.init();
    }
}
