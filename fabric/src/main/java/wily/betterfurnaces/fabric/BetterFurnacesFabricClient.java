package wily.betterfurnaces.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import wily.betterfurnaces.client.ClientSide;

public class BetterFurnacesFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientSide.init();
        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> ClientSide.registerExtraModels(out));
    }
}
