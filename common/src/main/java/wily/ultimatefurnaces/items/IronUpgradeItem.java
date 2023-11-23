package wily.ultimatefurnaces.items;

import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.ultimatefurnaces.init.ModObjectsUF;

public class IronUpgradeItem extends TierUpgradeItem {


    public IronUpgradeItem(Properties properties) {
        super(properties, ModObjectsUF.COPPER_FURNACE.get(), ModObjects.IRON_FURNACE.get());
    }
}
