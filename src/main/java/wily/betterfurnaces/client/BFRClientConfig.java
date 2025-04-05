package wily.betterfurnaces.client;

import wily.betterfurnaces.util.BFRComponents;
import wily.factoryapi.base.config.FactoryConfig;
import wily.factoryapi.base.config.FactoryConfigControl;
import wily.factoryapi.base.config.FactoryConfigDisplay;

public class BFRClientConfig {
    public static final FactoryConfig.StorageHandler CLIENT_STORAGE = new FactoryConfig.StorageHandler().withFile("betterfurnacesreforged_client.json");

    public static final FactoryConfig<Boolean> enableJEIPlugin = CLIENT_STORAGE.register(FactoryConfig.create("jeiPlugin", FactoryConfigDisplay.createToggle(BFRComponents.optionsName("jeiPlugin")), FactoryConfigControl.TOGGLE, true, b-> {}, CLIENT_STORAGE));
    public static final FactoryConfig<Boolean> enableJEICatalysts = CLIENT_STORAGE.register(FactoryConfig.create("jeiCatalysts", FactoryConfigDisplay.createToggle(BFRComponents.optionsName("jeiCatalysts")), FactoryConfigControl.TOGGLE, true, b-> {}, CLIENT_STORAGE));
    public static final FactoryConfig<Boolean> enableJEIClickArea = CLIENT_STORAGE.register(FactoryConfig.create("jeiClickArea", FactoryConfigDisplay.createToggle(BFRComponents.optionsName("jeiClickArea")), FactoryConfigControl.TOGGLE, true, b-> {}, CLIENT_STORAGE));
}
