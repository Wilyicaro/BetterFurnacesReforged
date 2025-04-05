package wily.betterfurnaces.items;

import wily.betterfurnaces.BFRConfig;

import wily.factoryapi.FactoryAPI;

public class XpTankUpgradeItem extends UpgradeItem {

    public XpTankUpgradeItem(Properties properties, String tooltip) {
        super(properties, Type.XP, tooltip);
    }

    @Override
    public boolean isEnabled() {
        return FactoryAPI.isModLoaded(BFRConfig.getLiquidXPMod());
    }

    @Override
    public boolean isUpgradeCompatibleWith(UpgradeItem upg) {
        return upg.upgradeType != Type.MODE;
    }
}
