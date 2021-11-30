package wily.ultimatefurnaces.items;

import wily.betterfurnaces.items.ItemUpgradeTier;
import wily.ultimatefurnaces.init.Registration;

public class ItemUpgradeIron extends ItemUpgradeTier {


    public ItemUpgradeIron(Properties properties) {
        super(properties, Registration.COPPER_FURNACE.get(), wily.betterfurnaces.init.Registration.IRON_FURNACE.get());
    }
}
