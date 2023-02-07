package wily.betterfurnaces.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.inventory.DiamondFurnaceMenu;
import wily.factoryapi.base.FactoryItemSlot;
import wily.factoryapi.base.IPlatformFluidHandler;

import java.util.List;

public class DiamondFurnaceBlockEntity extends AbstractSmeltingBlockEntity {
    public DiamondFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.DIAMOND_FURNACE_TILE.get(), pos, state,6);
    }

    @Override
    public int getCookTimeConfig() {
        return Config.diamondTierSpeed;
    }

    @Override
    public AbstractContainerMenu IcreateMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new DiamondFurnaceMenu(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }

}
