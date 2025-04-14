package wily.betterfurnaces.items;

import wily.betterfurnaces.BFRConfig;

import wily.factoryapi.FactoryAPI;
import wily.factoryapi.base.FactoryItemFluidHandler;
import wily.factoryapi.base.IFluidHandlerItem;
import wily.factoryapi.base.IPlatformFluidHandler;
import wily.factoryapi.util.FluidInstance;

public class XpTankUpgradeItem extends UpgradeItem implements IFluidHandlerItem<FactoryItemFluidHandler> {

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

    @Override
    public int getCapacity() {
        return 2000;
    }

    @Override
    public boolean isFluidValid(FluidInstance fluidInstance) {
        return fluidInstance.getFluid().isSame(BFRConfig.getLiquidXP());
    }
}
