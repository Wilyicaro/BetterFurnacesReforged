package wily.betterfurnaces.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import wily.betterfurnaces.blockentity.AbstractFuelVerifierBlockEntity;
import wily.betterfurnaces.init.Registration;


public abstract class AbstractFuelVerifierMenu extends AbstractInventoryMenu<AbstractFuelVerifierBlockEntity.FuelVerifierBlockEntity> {


    public AbstractFuelVerifierMenu(MenuType<?> containerType, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        this(containerType, windowId, world, pos, playerInventory, player, new SimpleContainerData(1));
    }

    public AbstractFuelVerifierMenu(MenuType<?> containerType, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
        super(containerType, windowId, world, pos, playerInventory, player, fields);
        checkContainerDataCount(this.fields, 1);
    }

    @Override
    public void addInventorySlots() {
        this.addSlot(new SlotFuel(be, 0, 80, 48));
    }

    public static class FuelVerifierMenu extends AbstractFuelVerifierMenu {
        public FuelVerifierMenu(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
            super(Registration.FUEL_VERIFIER_CONTAINER.get(), windowId, world, pos, playerInventory, player);
        }
        public FuelVerifierMenu(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
            super(Registration.FUEL_VERIFIER_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public BlockPos getPos() {
        return this.be.getBlockPos();
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurnTimeScaled(int pixels) {
        int i = 20000;

        return this.fields.get(0) * pixels / i;
    }
    public float getBurnTime() {
        return (float) this.fields.get(0) / 200;
    }


}
