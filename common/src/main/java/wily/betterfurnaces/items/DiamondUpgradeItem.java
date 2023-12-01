package wily.betterfurnaces.items;

import wily.betterfurnaces.init.ModObjects;

public class DiamondUpgradeItem extends TierUpgradeItem {


    public DiamondUpgradeItem(Properties properties) {
        super(properties, ModObjects.GOLD_FURNACE.get(), ModObjects.DIAMOND_FURNACE.get());
    }
}
