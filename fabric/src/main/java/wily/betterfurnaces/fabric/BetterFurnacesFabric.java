package wily.betterfurnaces.fabric;

import net.fabricmc.api.ModInitializer;
import wily.betterfurnaces.BetterFurnacesReforged;


public class BetterFurnacesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        BetterFurnacesReforged.init();
    }
}
