package wily.ultimatefurnaces.items;

import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.ultimatefurnaces.init.ModObjectsUF;

public class GoldUpgradeItem extends TierUpgradeItem {


    public GoldUpgradeItem(Properties properties) {
        super(properties, ModObjectsUF.STEEL_FURNACE.get(), ModObjects.GOLD_FURNACE.get());
    }
}
