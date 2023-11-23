package wily.betterfurnaces.items;

import wily.betterfurnaces.init.ModObjects;

public class GoldUpgradeItem extends TierUpgradeItem {


    public GoldUpgradeItem(Properties properties) {
        super(properties, ModObjects.IRON_FURNACE.get(), ModObjects.GOLD_FURNACE.get());
    }
}
