package wily.ultimatefurnaces.items;

import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.ultimatefurnaces.init.RegistrationUF;

public class SteelUpgradeItem extends TierUpgradeItem {


    public SteelUpgradeItem(Properties properties) {
        super(properties, Registration.IRON_FURNACE.get(), RegistrationUF.STEEL_FURNACE.get());
    }
}
