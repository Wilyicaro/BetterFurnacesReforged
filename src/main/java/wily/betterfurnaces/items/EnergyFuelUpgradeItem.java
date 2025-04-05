package wily.betterfurnaces.items;

import net.minecraft.network.chat.Component;
import wily.betterfurnaces.init.ModObjects;

public class EnergyFuelUpgradeItem extends UpgradeItem {


    public EnergyFuelUpgradeItem(Properties properties, Component tooltip) {
        super(properties, Type.ALTERNATIVE_FUEL, tooltip);
    }

    public boolean isUpgradeCompatibleWith(UpgradeItem upg) {
        return upg != ModObjects.GENERATOR.get();
    }
}
