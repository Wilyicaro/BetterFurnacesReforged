package wily.ultimatefurnaces.items;

import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.ultimatefurnaces.init.ModObjectsUF;

public class SteelUpgradeItem extends TierUpgradeItem {


    public SteelUpgradeItem(Properties properties) {
        super(properties, ModObjects.IRON_FURNACE.get(), ModObjectsUF.STEEL_FURNACE.get());
    }
}
