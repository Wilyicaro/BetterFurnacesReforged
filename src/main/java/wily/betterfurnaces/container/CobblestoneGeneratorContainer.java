package wily.betterfurnaces.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ItemUpgradeFuelEfficiency;
import wily.betterfurnaces.items.ItemUpgradeOreProcessing;
import wily.betterfurnaces.tileentity.CobblestoneGeneratorTileEntity;


public class CobblestoneGeneratorContainer extends Container {

    public CobblestoneGeneratorTileEntity te;
    protected IIntArray fields;
    protected PlayerEntity playerEntity;
    protected IItemHandler playerInventory;
    protected final World world;
    private IRecipeType<? extends AbstractCookingRecipe> recipeType;

    public CobblestoneGeneratorContainer(ContainerType<?> containerType, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        this(containerType, windowId, world, pos, playerInventory, player, new IntArray(3));
    }

    public CobblestoneGeneratorContainer(ContainerType<?> containerType, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, IIntArray fields) {
        super(containerType, windowId);
        this.te = (CobblestoneGeneratorTileEntity) world.getBlockEntity(pos);

        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        this.world = playerInventory.player.level;
        this.fields = fields;

        this.addDataSlots(this.fields);
        this.addSlot(new Slot(te, 0, 53, 27){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return ( stack.getItem() == Items.LAVA_BUCKET.getItem());
            }
        });
        this.addSlot(new Slot(te, 1, 108, 27){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return ( stack.getItem() == Items.WATER_BUCKET.getItem());
            }
        });
        this.addSlot(new SlotOutput(playerEntity, te, 2, 80, 45));
        this.addSlot(new SlotUpgrade(te, 3, 8, 18){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return ( stack.getItem() instanceof ItemUpgradeFuelEfficiency);
            }
        });
        this.addSlot(new SlotUpgrade(te, 4, 8, 36){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return ( stack.getItem() instanceof ItemUpgradeOreProcessing);
            }
        });
        layoutPlayerInventorySlots(8, 84);
        checkContainerSize(this.te, 5);
        checkContainerDataCount(this.fields, 3);
    }


    @OnlyIn(Dist.CLIENT)
    public BlockPos getPos() {
        return this.te.getBlockPos();
    }

    @OnlyIn(Dist.CLIENT)
    public int getCobTimeScaled(int pixels) {
        int i = this.fields.get(0);
        int j = this.fields.get(2);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }


    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 5) {
                if (!this.moveItemStackTo(itemstack1, 5, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if ((!this.moveItemStackTo(itemstack1, 0, 1, false)) && (!this.moveItemStackTo(itemstack1, 3, 5, false))) {
                if (index < 5 + 27) {
                    if (!this.moveItemStackTo(itemstack1, 5 + 27, this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(itemstack1, 5, 5 + 27, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemstack1);
        }
        return itemstack;
    }



    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return stillValid(IWorldPosCallable.create(te.getLevel(), te.getBlockPos()), playerEntity, Registration.COBBLESTONE_GENERATOR.get());
    }
}
