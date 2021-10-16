package wily.betterfurnaces.items;

import wily.betterfurnaces.init.Registration;

public class ItemUpgradeGold extends ItemUpgradeTier {


    public ItemUpgradeGold(Properties properties) {
        super(properties, Registration.IRON_FURNACE.get(), Registration.GOLD_FURNACE.get());
    }
}
