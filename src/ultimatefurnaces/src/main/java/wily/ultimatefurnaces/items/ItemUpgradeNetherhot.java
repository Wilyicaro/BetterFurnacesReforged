package wily.ultimatefurnaces.items;

import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ItemUpgradeTier;
import wily.ultimatefurnaces.init.RegistrationUF;

public class ItemUpgradeNetherhot extends ItemUpgradeTier {


    public ItemUpgradeNetherhot(Properties properties) {
        super(properties, RegistrationUF.PLATINUM_FURNACE.get(), Registration.NETHERHOT_FURNACE.get());
    }
}
