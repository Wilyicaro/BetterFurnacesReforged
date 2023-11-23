package wily.ultimatefurnaces.items;

import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.ultimatefurnaces.init.ModObjectsUF;

public class AmethystUpgradeItem extends TierUpgradeItem {


    public AmethystUpgradeItem(Properties properties) {
        super(properties, ModObjects.GOLD_FURNACE.get(), ModObjectsUF.AMETHYST_FURNACE.get());
    }
}
