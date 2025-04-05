package wily.betterfurnaces.items;

import wily.betterfurnaces.blockentity.SmeltingBlockEntity;


public class StorageUpgradeItem extends UpgradeItem {
    public StorageUpgradeItem(Properties properties) {
        super(properties, Type.STORAGE,"storage");
    }

    @Override
    public boolean isValid(SmeltingBlockEntity blockEntity) {
        return super.isValid(blockEntity) && !blockEntity.isForge();
    }

    @Override
    public boolean isUpgradeCompatibleWith(UpgradeItem upg) {
        return upg.upgradeType != Type.MODE;
    }
}
