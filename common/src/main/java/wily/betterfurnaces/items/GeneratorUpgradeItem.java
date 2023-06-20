package wily.betterfurnaces.items;

import me.shedaniel.architectury.fluid.FluidStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.ForgeBlockEntity;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.init.Registration;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.base.IFluidItem;
import wily.factoryapi.base.IPlatformFluidHandler;
import wily.factoryapi.base.TransportState;


public class GeneratorUpgradeItem extends UpgradeItem implements IFluidItem<IPlatformFluidHandler<?>> {
    public GeneratorUpgradeItem(Properties properties) {
        super(properties, 7, new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.generator", FactoryAPIPlatform.getPlatformEnergyComponent().getString()).setStyle(Style.EMPTY.applyFormat((ChatFormatting.GRAY))));
    }

    @Override
    public FluidStorageBuilder getFluidStorageBuilder(ItemStack stack) {
        return new FluidStorageBuilder(4* FluidStack.bucketAmount().longValue(), (f)->f.getFluid().isSame(Fluids.WATER), TransportState.EXTRACT_INSERT);
    }

    @Override
    public boolean isValid(SmeltingBlockEntity blockEntity) {
        return super.isValid(blockEntity) && !(blockEntity instanceof ForgeBlockEntity);
    }

    @Override
    public boolean isUpgradeCompatibleWith(UpgradeItem upg) {
        return upg != Registration.ENERGY.get() && upg.upgradeType != 3 && upg.upgradeType != 8;
    }
}
