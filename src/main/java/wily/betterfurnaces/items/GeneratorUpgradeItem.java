package wily.betterfurnaces.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.material.Fluids;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.ForgeBlockEntity;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.init.ModObjects;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.base.IFluidHandlerItem;
import wily.factoryapi.base.IPlatformFluidHandler;
import wily.factoryapi.util.FluidInstance;


public class GeneratorUpgradeItem extends UpgradeItem implements IFluidHandlerItem<IPlatformFluidHandler> {
    public GeneratorUpgradeItem(Properties properties) {
        super(properties, Type.MODE, Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.generator", FactoryAPIPlatform.getPlatformEnergyComponent().getString()).setStyle(Style.EMPTY.applyFormat((ChatFormatting.GRAY))));
    }
    @Override
    public int getCapacity() {
        return 4000;
    }
    @Override
    public boolean isFluidValid(FluidInstance fluidInstance) {
        return fluidInstance.getFluid().isSame(Fluids.WATER);
    }

    @Override
    public boolean isValid(SmeltingBlockEntity blockEntity) {
        return super.isValid(blockEntity) && !(blockEntity instanceof ForgeBlockEntity);
    }

    @Override
    public boolean isUpgradeCompatibleWith(UpgradeItem upg) {
        return upg != ModObjects.ENERGY.get() && upg.upgradeType != Type.ORE && upg.upgradeType != Type.XP && upg.upgradeType != Type.STORAGE;
    }
}
