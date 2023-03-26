package wily.betterfurnaces.items;

import wily.betterfurnaces.init.Registration;

public class GoldUpgradeItem extends TierUpgradeItem {


    public GoldUpgradeItem(Properties properties) {
        super(properties, Registration.IRON_FURNACE.get(), Registration.GOLD_FURNACE.get());
    }
}
