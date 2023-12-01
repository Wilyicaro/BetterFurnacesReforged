package wily.ultimatefurnaces.items;

import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.ultimatefurnaces.init.ModObjectsUF;

public class UltimateUpgradeItem extends TierUpgradeItem {


    public UltimateUpgradeItem(Properties properties) {
        super(properties, ModObjects.EXTREME_FURNACE.get(), ModObjectsUF.ULTIMATE_FURNACE.get());
    }
}
