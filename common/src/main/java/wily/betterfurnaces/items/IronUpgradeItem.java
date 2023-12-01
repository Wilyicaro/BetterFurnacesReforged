package wily.betterfurnaces.items;

import net.minecraft.world.level.block.Blocks;
import wily.betterfurnaces.init.ModObjects;

public class IronUpgradeItem extends TierUpgradeItem {


    public IronUpgradeItem(Properties properties) {
        super(properties, Blocks.FURNACE, ModObjects.IRON_FURNACE.get());
    }
}
