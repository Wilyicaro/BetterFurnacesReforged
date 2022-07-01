package wily.ultimatefurnaces.items;

import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.ultimatefurnaces.init.RegistrationUF;

public class DiamondUpgradeItem extends TierUpgradeItem {


    public DiamondUpgradeItem(Properties properties) {
        super(properties, RegistrationUF.AMETHYST_FURNACE.get(), Registration.DIAMOND_FURNACE.get());
    }
}
