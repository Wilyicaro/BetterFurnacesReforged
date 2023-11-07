package wily.betterfurnaces.items;

import me.shedaniel.architectury.fluid.FluidStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.material.Fluids;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.ForgeBlockEntity;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.init.ModObjects;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.base.IFluidHandlerItem;
import wily.factoryapi.base.IPlatformFluidHandler;


public class GeneratorUpgradeItem extends UpgradeItem implements IFluidHandlerItem<IPlatformFluidHandler<?>> {
    public GeneratorUpgradeItem(Properties properties) {
        super(properties, 7, new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.generator", FactoryAPIPlatform.getPlatformEnergyComponent().getString()).setStyle(Style.EMPTY.applyFormat((ChatFormatting.GRAY))));
    }


    @Override
    public boolean isValid(SmeltingBlockEntity blockEntity) {
        return super.isValid(blockEntity) && !(blockEntity instanceof ForgeBlockEntity);
    }

    @Override
    public boolean isUpgradeCompatibleWith(UpgradeItem upg) {
        return upg != ModObjects.ENERGY.get() && upg.upgradeType != 3 && upg.upgradeType != 8;
    }

    @Override
    public long getCapacity() {
        return 4* FactoryAPIPlatform.getBucketAmount();
    }

    @Override
    public boolean isFluidValid(FluidStack fluidStack) {
        return fluidStack.getFluid().isSame(Fluids.WATER);
    }
}
