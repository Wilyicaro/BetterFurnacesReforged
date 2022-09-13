package wily.ultimatefurnaces.items;

import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.ultimatefurnaces.init.RegistrationUF;

public class PlatinumUpgradeItem extends TierUpgradeItem {


    public PlatinumUpgradeItem(Properties properties) {
        super(properties, Registration.DIAMOND_FURNACE.get(), RegistrationUF.PLATINUM_FURNACE.get());
    }
}
