package wily.betterfurnaces.inventory;

import dev.architectury.fluid.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import wily.betterfurnaces.blockentity.CobblestoneGeneratorBlockEntity;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.FuelEfficiencyUpgradeItem;
import wily.betterfurnaces.items.OreProcessingUpgradeItem;
import wily.factoryapi.ItemContainerUtil;


public class CobblestoneGeneratorMenu extends AbstractInventoryMenu<CobblestoneGeneratorBlockEntity> {



    public CobblestoneGeneratorMenu( int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        this( windowId, world, pos, playerInventory, player, new SimpleContainerData(3));
    }

    public CobblestoneGeneratorMenu(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
        super(Registration.COB_GENERATOR_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
        checkContainerDataCount(this.fields, 3);
    }

    public int getCobTimeScaled(int pixels) {
        int i = this.fields.get(0);
        int j = this.fields.get(2);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }


}
