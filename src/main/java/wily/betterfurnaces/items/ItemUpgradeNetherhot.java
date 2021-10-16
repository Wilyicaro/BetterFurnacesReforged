package wily.betterfurnaces.items;

import wily.betterfurnaces.init.Registration;

public class ItemUpgradeNetherhot extends ItemUpgradeTier {


    public ItemUpgradeNetherhot(Properties properties) {
        super(properties, Registration.DIAMOND_FURNACE.get(), Registration.NETHERHOT_FURNACE.get());
    }
}
