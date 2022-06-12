package wily.ultimatefurnaces.items;

import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ItemUpgradeTier;
import wily.ultimatefurnaces.init.RegistrationUF;

public class ItemUpgradeDiamond extends ItemUpgradeTier {


    public ItemUpgradeDiamond(Properties properties) {
        super(properties, RegistrationUF.AMETHYST_FURNACE.get(), Registration.DIAMOND_FURNACE.get());
    }
}
