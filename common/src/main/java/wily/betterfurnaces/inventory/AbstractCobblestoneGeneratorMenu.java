package wily.betterfurnaces.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import wily.betterfurnaces.blockentity.AbstractCobblestoneGeneratorBlockEntity;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.FuelEfficiencyUpgradeItem;
import wily.betterfurnaces.items.OreProcessingUpgradeItem;


public abstract class AbstractCobblestoneGeneratorMenu extends AbstractInventoryMenu<AbstractCobblestoneGeneratorBlockEntity> {



    public AbstractCobblestoneGeneratorMenu(MenuType<?> containerType, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        this(containerType, windowId, world, pos, playerInventory, player, new SimpleContainerData(3));
    }

    public AbstractCobblestoneGeneratorMenu(MenuType<?> containerType, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
        super(containerType, windowId, world, pos, playerInventory, player, fields);
        checkContainerDataCount(this.fields, 3);
    }
    public static class CobblestoneGeneratorMenu extends AbstractCobblestoneGeneratorMenu {
        public CobblestoneGeneratorMenu(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
            super(Registration.COB_GENERATOR_CONTAINER.get(), windowId, world, pos, playerInventory, player);
        }

        public CobblestoneGeneratorMenu(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
            super(Registration.COB_GENERATOR_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
        }
    }

    public BlockPos getPos() {
        return this.be.getBlockPos();
    }

    public int getCobTimeScaled(int pixels) {
        int i = this.fields.get(0);
        int j = this.fields.get(2);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    @Override
    public void addInventorySlots() {
        this.addSlot(new Slot(be.inventory, 0, 53, 27){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return ( stack.getItem() == Items.LAVA_BUCKET);
            }
        });
        this.addSlot(new Slot(be.inventory, 1, 108, 27){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return ( stack.getItem() == Items.WATER_BUCKET);
            }
        });
        this.addSlot(new SlotOutput(playerEntity, be, 2, 80, 45));
        this.addSlot(new SlotUpgrade(be, 3, 8, 18){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return ( stack.getItem() instanceof FuelEfficiencyUpgradeItem);
            }
        });
        this.addSlot(new SlotUpgrade(be, 4, 8, 36){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return ( stack.getItem() instanceof OreProcessingUpgradeItem);
            }
        });
    }
}
