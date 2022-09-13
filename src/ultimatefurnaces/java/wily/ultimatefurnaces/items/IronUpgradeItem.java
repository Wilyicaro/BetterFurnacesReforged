package wily.ultimatefurnaces.items;

import wily.betterfurnaces.items.TierUpgradeItem;
import wily.ultimatefurnaces.init.RegistrationUF;

public class IronUpgradeItem extends TierUpgradeItem {


    public IronUpgradeItem(Properties properties) {
        super(properties, RegistrationUF.COPPER_FURNACE.get(), wily.betterfurnaces.init.Registration.IRON_FURNACE.get());
    }
}
