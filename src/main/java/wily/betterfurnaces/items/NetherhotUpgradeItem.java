package wily.betterfurnaces.items;

import wily.betterfurnaces.init.Registration;

public class NetherhotUpgradeItem extends ItemUpgradeTier {


    public NetherhotUpgradeItem(Properties properties) {
        super(properties, Registration.DIAMOND_FURNACE.get(), Registration.NETHERHOT_FURNACE.get());
    }
}
