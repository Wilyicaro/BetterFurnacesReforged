package wily.ultimatefurnaces.items;

import wily.betterfurnaces.items.TierUpgradeItem;
import wily.ultimatefurnaces.init.RegistrationUF;

public class UltimateUpgradeItem extends TierUpgradeItem {


    public UltimateUpgradeItem(Properties properties) {
        super(properties, wily.betterfurnaces.init.Registration.EXTREME_FURNACE.get(), RegistrationUF.ULTIMATE_FURNACE.get());
    }
}
