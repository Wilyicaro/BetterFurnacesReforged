package wily.betterfurnaces.forge.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;
import wily.betterfurnaces.items.GeneratorUpgradeItem;
import wily.factoryapi.base.IFluidItem;
import wily.factoryapi.base.IPlatformFluidHandler;
import wily.factoryapi.base.IPlatformHandlerApi;
import wily.factoryapi.forge.base.CapabilityUtil;
import wily.factoryapi.forge.base.ForgeItemFluidHandler;

import java.util.Optional;

@Mixin(GeneratorUpgradeItem.class)
public class AddBetterFurnacesItemsCapabilities extends Item {

    public AddBetterFurnacesItemsCapabilities(Properties arg) {
        super(arg);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        IFluidItem<IPlatformFluidHandler> cell = ((IFluidItem<IPlatformFluidHandler>)this);
        return (ForgeItemFluidHandler)cell.getFluidStorage(stack);
    }
}
