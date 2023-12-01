package wily.betterfurnaces.items;

import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.init.ModObjects;


public class StorageUpgradeItem extends UpgradeItem {
    public StorageUpgradeItem(Properties properties) {
        super(properties, 8,"storage");
    }

    @Override
    public boolean isValid(SmeltingBlockEntity blockEntity) {
        return super.isValid(blockEntity) && !blockEntity.isForge();
    }

    @Override
    public boolean isUpgradeCompatibleWith(UpgradeItem upg) {
        return upg != ModObjects.GENERATOR.get();
    }
}
