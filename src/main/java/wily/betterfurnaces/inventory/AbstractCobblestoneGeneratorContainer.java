package wily.betterfurnaces.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.FuelEfficiencyUpgradeItem;
import wily.betterfurnaces.items.OreProcessingUpgradeItem;
import wily.betterfurnaces.tileentity.AbstractCobblestoneGeneratorTileEntity;


public abstract class AbstractCobblestoneGeneratorContainer extends AbstractInventoryContainer<AbstractCobblestoneGeneratorTileEntity.CobblestoneGeneratorTileEntity> {



    public AbstractCobblestoneGeneratorContainer(ContainerType<?> containerType, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        this(containerType, windowId, world, pos, playerInventory, player, new IntArray(3));
    }

    public AbstractCobblestoneGeneratorContainer(ContainerType<?> containerType, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, IIntArray fields) {
        super(containerType, windowId, world, pos, playerInventory, player, fields);
        checkContainerDataCount(this.fields, 3);
    }

    @OnlyIn(Dist.CLIENT)
    public int getCobTimeScaled(int pixels) {
        int i = this.fields.get(0);
        int j = this.fields.get(2);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }
    @Override
    public void addInventorySlots() {
        this.addSlot(new Slot(te, 0, 53, 27){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return ( stack.getItem() == Items.LAVA_BUCKET);
            }
        });
        this.addSlot(new Slot(te, 1, 108, 27){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return ( stack.getItem() == Items.WATER_BUCKET);
            }
        });
        this.addSlot(new SlotOutput(playerEntity, te, 2, 80, 45));
        this.addSlot(new SlotUpgrade(te, 3, 8, 18){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return ( stack.getItem() instanceof FuelEfficiencyUpgradeItem);
            }
        });
        this.addSlot(new SlotUpgrade(te, 4, 8, 36){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return ( stack.getItem() instanceof OreProcessingUpgradeItem);
            }
        });
    }

    public static class CobblestoneGeneratorContainer extends wily.betterfurnaces.inventory.AbstractCobblestoneGeneratorContainer {
        public CobblestoneGeneratorContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
            super(Registration.COB_GENERATOR_CONTAINER.get(), windowId, world, pos, playerInventory, player);
        }

        public CobblestoneGeneratorContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, IIntArray fields) {
            super(Registration.COB_GENERATOR_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
        }
    }
}
