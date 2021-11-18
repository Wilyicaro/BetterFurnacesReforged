package wily.ultimatefurnaces.items;

import net.minecraft.world.level.block.Blocks;
import wily.betterfurnaces.items.ItemUpgradeTier;
import wily.ultimatefurnaces.init.Registration;

public class ItemUpgradeCopper extends ItemUpgradeTier {


    public ItemUpgradeCopper(Properties properties) {
        super(properties, Blocks.FURNACE, Registration.COPPER_FURNACE.get());
    }
}
