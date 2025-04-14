package wily.betterfurnaces.items;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import wily.betterfurnaces.BFRConfig;
import wily.factoryapi.ItemContainerPlatform;
import wily.factoryapi.base.FactoryItemFluidHandler;
import wily.factoryapi.base.FuelManager;
import wily.factoryapi.base.IFluidHandlerItem;
import wily.factoryapi.base.IPlatformFluidHandler;
import wily.factoryapi.util.FluidInstance;

public class LiquidFuelUpgradeItem extends UpgradeItem implements IFluidHandlerItem<FactoryItemFluidHandler> {

    private final int defaultCapacity;

    public LiquidFuelUpgradeItem(Properties properties, String tooltip, int defaultCapacity) {
        super(properties, Type.ALTERNATIVE_FUEL, tooltip);
        this.defaultCapacity = defaultCapacity;
    }

    public static boolean supportsFluid(Fluid fluid){
        return FuelManager.isFuel(fluid.getBucket().getDefaultInstance()) || supportsAdditionalFluid(fluid);
    }

    public static boolean supportsItemFluidHandler(ItemStack stack){
        return ItemContainerPlatform.isFluidContainer(stack) && (FuelManager.isFuel(stack) || supportsAdditionalFluid(ItemContainerPlatform.getFluid(stack).getFluid()));
    }

    public static boolean supportsAdditionalFluid(Fluid fluid){
        return BFRConfig.additionalLiquidFuels.get().contains(BuiltInRegistries.FLUID.getKey(fluid).toString());
    }

    @Override
    public boolean isFluidValid(FluidInstance fluidInstance) {
        return supportsFluid(fluidInstance.getFluid());
    }

    @Override
    public int getCapacity() {
        return defaultCapacity;
    }
}
