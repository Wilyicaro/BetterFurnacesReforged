package wily.ultimatefurnaces.items;

import net.minecraft.world.level.block.Blocks;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ItemUpgradeTier;
import wily.ultimatefurnaces.init.RegistrationUF;

public class ItemUpgradePlatinum extends ItemUpgradeTier {


    public ItemUpgradePlatinum(Properties properties) {
        super(properties, Registration.DIAMOND_FURNACE.get(), RegistrationUF.PLATINUM_FURNACE.get());
    }
}
