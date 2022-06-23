package wily.ultimatefurnaces.items;

import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ItemUpgradeTier;
import wily.ultimatefurnaces.init.RegistrationUF;

public class ItemUpgradeGold extends ItemUpgradeTier {


    public ItemUpgradeGold(Properties properties) {
        super(properties, RegistrationUF.STEEL_FURNACE.get(), Registration.GOLD_FURNACE.get());
    }
}
