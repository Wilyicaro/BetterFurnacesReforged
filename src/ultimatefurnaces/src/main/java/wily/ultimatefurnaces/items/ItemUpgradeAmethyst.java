package wily.ultimatefurnaces.items;

import net.minecraft.world.level.block.Blocks;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ItemUpgradeTier;
import wily.ultimatefurnaces.init.RegistrationUF;

public class ItemUpgradeAmethyst extends ItemUpgradeTier {


    public ItemUpgradeAmethyst(Properties properties) {
        super(properties, Registration.GOLD_FURNACE.get(), RegistrationUF.AMETHYST_FURNACE.get());
    }
}
