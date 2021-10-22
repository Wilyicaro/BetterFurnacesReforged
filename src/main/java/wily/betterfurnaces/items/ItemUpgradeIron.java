package wily.betterfurnaces.items;

import net.minecraft.world.level.block.Blocks;
import wily.betterfurnaces.init.Registration;

public class ItemUpgradeIron extends ItemUpgradeTier {


    public ItemUpgradeIron(Properties properties) {
        super(properties, Blocks.FURNACE, Registration.IRON_FURNACE.get());
    }
}
