package wily.ultimatefurnaces.items;

import net.minecraft.world.level.block.Blocks;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.ultimatefurnaces.init.ModObjectsUF;

public class CopperUpgradeItem extends TierUpgradeItem {


    public CopperUpgradeItem(Properties properties) {
        super(properties, Blocks.FURNACE, ModObjectsUF.COPPER_FURNACE.get());
    }
}
