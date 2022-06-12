package wily.betterfurnaces.items;

import wily.betterfurnaces.init.Registration;

public class ItemUpgradeDiamond extends ItemUpgradeTier {


    public ItemUpgradeDiamond(Properties properties) {
        super(properties, Registration.GOLD_FURNACE.get(), Registration.DIAMOND_FURNACE.get());
    }
}
