package wily.betterfurnaces.items;

import wily.betterfurnaces.init.Registration;

public class ItemUpgradeExtreme extends ItemUpgradeTier {


    public ItemUpgradeExtreme(Properties properties) {
        super(properties, Registration.NETHERHOT_FURNACE.get(), Registration.EXTREME_FURNACE.get());
    }
}
