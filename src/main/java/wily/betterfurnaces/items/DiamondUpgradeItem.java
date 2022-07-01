package wily.betterfurnaces.items;

import wily.betterfurnaces.init.Registration;

public class DiamondUpgradeItem extends TierUpgradeItem {


    public DiamondUpgradeItem(Properties properties) {
        super(properties, Registration.GOLD_FURNACE.get(), Registration.DIAMOND_FURNACE.get());
    }
}
