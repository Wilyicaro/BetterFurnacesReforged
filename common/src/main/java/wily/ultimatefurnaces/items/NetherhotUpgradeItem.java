package wily.ultimatefurnaces.items;

import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.ultimatefurnaces.init.ModObjectsUF;

public class NetherhotUpgradeItem extends TierUpgradeItem {


    public NetherhotUpgradeItem(Properties properties) {
        super(properties, ModObjectsUF.PLATINUM_FURNACE.get(), ModObjects.NETHERHOT_FURNACE.get());
    }
}
