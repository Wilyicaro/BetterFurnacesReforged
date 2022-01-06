package wily.ultimatefurnaces.items;

import wily.betterfurnaces.items.ItemUpgradeTier;
import wily.ultimatefurnaces.init.RegistrationUF;

public class ItemUpgradeUltimate extends ItemUpgradeTier {


    public ItemUpgradeUltimate(Properties properties) {
        super(properties, wily.betterfurnaces.init.Registration.EXTREME_FURNACE.get(), RegistrationUF.ULTIMATE_FURNACE.get());
    }
}
