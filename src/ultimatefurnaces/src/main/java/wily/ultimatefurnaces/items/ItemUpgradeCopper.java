package wily.ultimatefurnaces.items;

import net.minecraft.block.Blocks;
import wily.betterfurnaces.items.ItemUpgradeTier;
import wily.ultimatefurnaces.init.RegistrationUF;

public class ItemUpgradeCopper extends ItemUpgradeTier {


    public ItemUpgradeCopper(Properties properties) {
        super(properties, Blocks.FURNACE, RegistrationUF.COPPER_FURNACE.get());
    }
}
