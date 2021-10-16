package wily.ultimatefurnaces.items;

import wily.betterfurnaces.items.ItemUpgradeTier;
import wily.ultimatefurnaces.init.Registration;

public class ItemUpgradeUltimate extends ItemUpgradeTier {


    public ItemUpgradeUltimate(Properties properties) {
        super(properties, wily.betterfurnaces.init.Registration.NETHERHOT_FURNACE.get(), Registration.ULTIMATE_FURNACE.get());
    }
}
