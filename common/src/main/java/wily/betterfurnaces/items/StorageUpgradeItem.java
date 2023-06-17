package wily.betterfurnaces.items;

import dev.architectury.fluid.FluidStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.material.Fluids;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.ForgeBlockEntity;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.init.Registration;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.base.IFluidItem;
import wily.factoryapi.base.IPlatformFluidHandler;
import wily.factoryapi.base.TransportState;


public class StorageUpgradeItem extends UpgradeItem {
    public StorageUpgradeItem(Properties properties) {
        super(properties, 8);
    }

    @Override
    public boolean isValid(SmeltingBlockEntity blockEntity) {
        return super.isValid(blockEntity) && !blockEntity.isForge();
    }
    @Override
    public boolean isUpgradeCompatibleWith(UpgradeItem upg) {
        return upg != Registration.GENERATOR.get();
    }
}
