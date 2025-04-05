package wily.betterfurnaces.client.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import wily.betterfurnaces.BFRConfig;
import wily.betterfurnaces.client.BFRClientConfig;
import wily.betterfurnaces.util.BFRComponents;
import wily.factoryapi.base.client.screen.FactoryConfigScreen;

import java.util.List;

public class BFRConfigScreen extends FactoryConfigScreen {
    public static final Component TITLE = BFRComponents.optionsName("title");

    public BFRConfigScreen(Screen screen) {
        super(screen, List.of(BFRClientConfig.enableJEIPlugin,BFRClientConfig.enableJEICatalysts,BFRClientConfig.enableJEIClickArea, BFRConfig.checkUpdates,BFRConfig.cacheCapacity,BFRConfig.furnaceXPDropValue,BFRConfig.furnaceAccumulatedXPDropValue,BFRConfig.checkCommonOresName,BFRConfig.checkRawOresName,BFRConfig.copperTierSpeed,BFRConfig.ironTierSpeed,BFRConfig.steelTierSpeed,BFRConfig.goldTierSpeed,BFRConfig.amethystTierSpeed,BFRConfig.diamondTierSpeed,BFRConfig.platinumTierSpeed,BFRConfig.extremeTierSpeed,BFRConfig.ultimateTierSpeed,BFRConfig.xpFluidType), TITLE);
    }
}
