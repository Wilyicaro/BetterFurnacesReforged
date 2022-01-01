package wily.betterfurnaces.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import wily.betterfurnaces.blockentity.BlockEntityCobblestoneGenerator;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ItemUpgradeFuelEfficiency;
import wily.betterfurnaces.items.ItemUpgradeOreProcessing;


public class BlockCobblestoneGeneratorContainer extends AbstractContainerMenu {

    protected BlockEntityCobblestoneGenerator te;
    protected ContainerData fields;
    protected Player playerEntity;
    protected IItemHandler playerInventory;
    protected final Level world;
    private RecipeType<? extends AbstractCookingRecipe> recipeType;

    public BlockCobblestoneGeneratorContainer(MenuType<?> containerType, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        this(containerType, windowId, world, pos, playerInventory, player, new SimpleContainerData(3));
    }

    public BlockCobblestoneGeneratorContainer(MenuType<?> containerType, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
        super(containerType, windowId);
        this.te = (BlockEntityCobblestoneGenerator) world.getBlockEntity(pos);

        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        this.world = playerInventory.player.level;
        this.fields = fields;

        this.addDataSlots(this.fields);
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
        int i = fields.get(2);
        return this.fields.get(0) * pixels / i;
    }
    public int getButtonstate() {
        if (this.fields.get(1) > 0) return this.fields.get(1);
        else return 1;
    }


    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
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
            } else if (!this.moveItemStackTo(itemstack1, 0, 5, false)) {
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
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(te.getLevel(), te.getBlockPos()), playerEntity, Registration.COBBLESTONE_GENERATOR.get());
    }
}
