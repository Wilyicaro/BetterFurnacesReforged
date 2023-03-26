package wily.betterfurnaces.forge.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;
import wily.factoryapi.base.IFactoryStorage;
import wily.factoryapi.base.IPlatformHandlerApi;
import wily.factoryapi.forge.base.CapabilityUtil;

import java.util.Optional;

@Mixin(InventoryBlockEntity.class)
public class AddBetterFurnacesBlocksCapabilities extends BlockEntity {
    public AddBetterFurnacesBlocksCapabilities(BlockEntityType<?> arg, BlockPos arg2, BlockState arg3) {
        super(arg, arg2, arg3);

    }
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction direction) {

        InventoryBlockEntity be = ((InventoryBlockEntity)(Object) this);
        Optional<? extends IPlatformHandlerApi> storage =  be.getStorage(CapabilityUtil.capabilityToStorage(cap),direction);
        if (!this.isRemoved() && CapabilityUtil.capabilityToStorage(cap) != null && storage.isPresent())
            return LazyOptional.of(()->storage.get().getHandler()).cast();
        return super.getCapability(cap,direction);
    }
}
