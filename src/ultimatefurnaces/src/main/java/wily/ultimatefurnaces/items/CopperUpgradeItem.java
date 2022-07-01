package wily.ultimatefurnaces.items;

import net.minecraft.block.Blocks;
import wily.betterfurnaces.items.TierUpgradeItem;
import wily.ultimatefurnaces.init.RegistrationUF;

public class CopperUpgradeItem extends TierUpgradeItem {


    public CopperUpgradeItem(Properties properties) {
        super(properties, Blocks.FURNACE, RegistrationUF.COPPER_FURNACE.get());
    }
}
