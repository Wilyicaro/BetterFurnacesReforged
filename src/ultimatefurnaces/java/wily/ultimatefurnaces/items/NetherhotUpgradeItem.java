package wily.ultimatefurnaces.items;

import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.ultimatefurnaces.init.RegistrationUF;

public class NetherhotUpgradeItem extends TierUpgradeItem {


    public NetherhotUpgradeItem(Properties properties) {
        super(properties, RegistrationUF.PLATINUM_FURNACE.get(), Registration.NETHERHOT_FURNACE.get());
    }
}
