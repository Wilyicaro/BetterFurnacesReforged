package wily.ultimatefurnaces.items;

import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.ultimatefurnaces.init.RegistrationUF;

public class GoldUpgradeItem extends TierUpgradeItem {


    public GoldUpgradeItem(Properties properties) {
        super(properties, RegistrationUF.STEEL_FURNACE.get(), Registration.GOLD_FURNACE.get());
    }
}
