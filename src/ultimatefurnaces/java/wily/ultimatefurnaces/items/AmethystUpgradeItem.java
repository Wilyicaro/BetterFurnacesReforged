package wily.ultimatefurnaces.items;

import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.ultimatefurnaces.init.RegistrationUF;

public class AmethystUpgradeItem extends TierUpgradeItem {


    public AmethystUpgradeItem(Properties properties) {
        super(properties, Registration.GOLD_FURNACE.get(), RegistrationUF.AMETHYST_FURNACE.get());
    }
}
