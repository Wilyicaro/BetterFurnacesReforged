package wily.betterfurnaces.items;

import dev.architectury.registry.fuel.FuelRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import wily.betterfurnaces.Config;
import wily.factoryapi.ItemContainerUtil;

public class LiquidFuelUpgradeItem extends UpgradeItem {


    public LiquidFuelUpgradeItem(Properties properties, String tooltip) {
        super(properties,1, tooltip);
    }

    public static boolean supportsFluid(Fluid fluid){
        return FuelRegistry.get(fluid.getBucket().getDefaultInstance()) > 0 || supportsAdditionalFluid(fluid);
    }
    public static boolean supportsItemFluidHandler(ItemStack stack){
        return ItemContainerUtil.isFluidContainer(stack) && (FuelRegistry.get(stack) > 0 || supportsAdditionalFluid(ItemContainerUtil.getFluid(stack).getFluid()));
    }
    public static boolean supportsAdditionalFluid(Fluid fluid){
        return Config.additionalLiquidFuels.get().contains(BuiltInRegistries.FLUID.getKey(fluid).toString());
    }
}
