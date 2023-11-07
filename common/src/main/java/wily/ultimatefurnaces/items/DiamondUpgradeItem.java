package wily.ultimatefurnaces.items;

import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.ultimatefurnaces.init.ModObjectsUF;

public class DiamondUpgradeItem extends TierUpgradeItem {


    public DiamondUpgradeItem(Properties properties) {
        super(properties, ModObjectsUF.AMETHYST_FURNACE.get(), ModObjects.DIAMOND_FURNACE.get());
    }
}
