package wily.ultimatefurnaces.items;

import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ItemUpgradeTier;
import wily.ultimatefurnaces.init.RegistrationUF;

public class ItemUpgradeSteel extends ItemUpgradeTier {


    public ItemUpgradeSteel(Properties properties) {
        super(properties, Registration.IRON_FURNACE.get(), RegistrationUF.STEEL_FURNACE.get());
    }
}
