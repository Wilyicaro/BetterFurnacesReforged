package wily.ultimatefurnaces.items;

import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.ultimatefurnaces.init.ModObjectsUF;

public class PlatinumUpgradeItem extends TierUpgradeItem {


    public PlatinumUpgradeItem(Properties properties) {
        super(properties, ModObjects.DIAMOND_FURNACE.get(), ModObjectsUF.PLATINUM_FURNACE.get());
    }
}
