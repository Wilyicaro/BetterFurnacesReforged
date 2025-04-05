package wily.betterfurnaces.items;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import wily.betterfurnaces.BFRConfig;
import wily.factoryapi.ItemContainerPlatform;
import wily.factoryapi.base.FuelManager;

public class LiquidFuelUpgradeItem extends UpgradeItem {

    public LiquidFuelUpgradeItem(Properties properties, String tooltip) {
        super(properties, Type.ALTERNATIVE_FUEL, tooltip);
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
}
