package wily.betterfurnaces.items;

import net.minecraft.world.level.block.Blocks;
import wily.betterfurnaces.init.Registration;

public class IronUpgradeItem extends ItemUpgradeTier {


    public IronUpgradeItem(Properties properties) {
        super(properties, Blocks.FURNACE, Registration.IRON_FURNACE.get());
    }
}
