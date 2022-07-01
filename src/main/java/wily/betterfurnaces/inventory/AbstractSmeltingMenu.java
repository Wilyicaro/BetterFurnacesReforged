package wily.betterfurnaces.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.AbstractSmeltingBlockEntity;
import wily.betterfurnaces.util.DirectionUtil;


public abstract class AbstractSmeltingMenu extends AbstractContainerMenu {

    public AbstractSmeltingBlockEntity te;
    protected ContainerData fields;
    protected Player playerEntity;
    protected IItemHandler playerInventory;
    protected final Level world;
    private RecipeType<? extends AbstractCookingRecipe> recipeType;

    public AbstractSmeltingMenu(MenuType<?> containerType, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        this(containerType, windowId, world, pos, playerInventory, player, new SimpleContainerData(5));
    }

    public AbstractSmeltingMenu(MenuType<?> containerType, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
        super(containerType, windowId);
        this.te = (AbstractSmeltingBlockEntity) world.getBlockEntity(pos);
        this.recipeType = te.recipeType;

        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        this.world = playerInventory.player.level;
        this.fields = fields;
        this.addInventorySlots();
        this.addDataSlots(this.fields);
        checkContainerSize(this.te, te.getInvSize);
        checkContainerDataCount(this.fields, 5);
    }

    public void addInventorySlots(){
        this.addSlot(new SlotInput(te, 0, 54, 18));
        this.addSlot(new SlotFuel(this.te, 1, 54, 54));
        this.addSlot(new SlotOutput(playerEntity, te, 2, 116, 35));
        this.addSlot(new SlotUpgrade(te, 3, 8, 18));
        this.addSlot(new SlotUpgrade(te, 4, 8, 36));
        this.addSlot(new SlotUpgrade(te, 5, 8, 54));
        layoutPlayerInventorySlots(8, 84);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean showInventoryButtons() {
        return this.te.fields.get(4) == 1;
    }

    @OnlyIn(Dist.CLIENT)
    public int getRedstoneMode() {
        return this.te.getRedstoneSetting();
    }

    @OnlyIn(Dist.CLIENT)
    public int getComSub() {
        return this.te.getRedstoneComSub();
    }

    @OnlyIn(Dist.CLIENT)
    public boolean getAutoInput() {
        return this.te.getAutoInput() == 1;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean getAutoOutput() {
        return this.te.getAutoOutput() == 1;
    }

    @OnlyIn(Dist.CLIENT)
    public Component getTooltip(int index) {
        switch (te.furnaceSettings.get(index))
        {
            case 1:
                return new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_input");
            case 2:
                return new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_output");
            case 3:
                return new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_input_output");
            case 4:
                return new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_fuel");
            default:
                return new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_none");
        }
    }

    @OnlyIn(Dist.CLIENT)
    public int getSettingTop()
    {
        return this.te.getSettingTop();
    }

    @OnlyIn(Dist.CLIENT)
    public int getSettingBottom()
    {
        return this.te.getSettingBottom();
    }

    @OnlyIn(Dist.CLIENT)
    public int getSettingFront()
    {
        return this.te.getSettingFront();
    }

    @OnlyIn(Dist.CLIENT)
    public int getSettingBack()
    {
        return this.te.getSettingBack();
    }

    @OnlyIn(Dist.CLIENT)
    public int getSettingLeft()
    {
        return this.te.getSettingLeft();
    }

    @OnlyIn(Dist.CLIENT)
    public int getSettingRight()
    {
        return this.te.getSettingRight();
    }

    @OnlyIn(Dist.CLIENT)
    public int getIndexFront()
    {
        return this.te.getIndexFront();
    }

    @OnlyIn(Dist.CLIENT)
    public int getIndexBack()
    {
        return this.te.getIndexBack();
    }

    @OnlyIn(Dist.CLIENT)
    public int getIndexLeft()
    {
        return this.te.getIndexLeft();
    }

    @OnlyIn(Dist.CLIENT)
    public int getIndexRight()
    {
        return this.te.getIndexRight();
    }

    @OnlyIn(Dist.CLIENT)
    public BlockPos getPos() {
        return this.te.getBlockPos();
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookScaled(int pixels) {
        int i = this.fields.get(2);
        int j = this.fields.get(3);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurnLeftScaled(int pixels) {
        int i = this.fields.get(1);
        if (i == 0) {
            i = 200;
        }

        return this.fields.get(0) * pixels / i;
    }
    public int getFluidStoredScaled(int pixels, boolean isXp) {
        Direction facing = null;
        if (isXp) facing = DirectionUtil.fromId(te.getIndexFront());
        int cur = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing).resolve().get().getFluidInTank(0).getAmount();
        int max = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing).resolve().get().getTankCapacity(0);
        return cur * pixels / max;
    }
    public FluidStack getFluidStackStored(boolean isXp) {
        Direction facing = null;
        if (isXp) facing = DirectionUtil.fromId(te.getIndexFront());
        return te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing).resolve().get().getFluidInTank(0);
    }
    public int getEnergyStoredScaled(int pixels) {
        int cur = te.getCapability(CapabilityEnergy.ENERGY, null).resolve().get().getEnergyStored();
        int max = te.getCapability(CapabilityEnergy.ENERGY, null).resolve().get().getMaxEnergyStored();
        return cur * pixels / max;
    }
    public int getEnergyStored() {
        return te.getCapability(CapabilityEnergy.ENERGY, null).resolve().get().getEnergyStored();
    }
    public int getEnergyMaxStored() {
        return te.getCapability(CapabilityEnergy.ENERGY, null).resolve().get().getMaxEnergyStored();
    }
    public int BurnTimeGet(){
        return this.fields.get(0);
    }

    protected boolean moveItemStackTo(ItemStack p_75135_1_, int p_75135_2_, int p_75135_3_, boolean p_75135_4_) {
        boolean flag = false;
        int i = p_75135_2_;
        if (p_75135_4_) {
            i = p_75135_3_ - 1;
        }

        if (p_75135_1_.isStackable()) {
            while(!p_75135_1_.isEmpty()) {
                if (p_75135_4_) {
                    if (i < p_75135_2_) {
                        break;
                    }
                } else if (i >= p_75135_3_) {
                    break;
                }

                Slot slot = this.slots.get(i);
                ItemStack itemstack = slot.getItem();
                if (!itemstack.isEmpty() && ItemStack.isSameItemSameTags(p_75135_1_, itemstack)) {
                    int j = itemstack.getCount() + p_75135_1_.getCount();
                    int maxSize = Math.min(slot.getMaxStackSize(slot.getItem()), p_75135_1_.getMaxStackSize());
                    if (j <= maxSize) {
                        p_75135_1_.setCount(0);
                        itemstack.setCount(j);
                        slot.setChanged();
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        p_75135_1_.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot.setChanged();
                        flag = true;
                    }
                }

                if (p_75135_4_) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (!p_75135_1_.isEmpty()) {
            if (p_75135_4_) {
                i = p_75135_3_ - 1;
            } else {
                i = p_75135_2_;
            }

            while(true) {
                if (p_75135_4_) {
                    if (i < p_75135_2_) {
                        break;
                    }
                } else if (i >= p_75135_3_) {
                    break;
                }

                Slot slot1 = this.slots.get(i);
                ItemStack itemstack1 = slot1.getItem();
                if (itemstack1.isEmpty() && slot1.mayPlace(p_75135_1_)) {
                    if (p_75135_1_.getCount() > slot1.getMaxStackSize(slot1.getItem())) {
                        slot1.set(p_75135_1_.split(slot1.getMaxStackSize(slot1.getItem())));
                    } else if (p_75135_1_.getCount() <= slot1.getMaxStackSize(p_75135_1_)) {
                        slot1.set(p_75135_1_.split(p_75135_1_.getCount()));
                    }

                    slot1.setChanged();
                    flag = true;
                    break;
                }

                if (p_75135_4_) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return flag;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < te.getInvSize) {
                if (!this.moveItemStackTo(itemstack1, te.getInvSize - 1, this.slots.size() - 1, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (!this.moveItemStackTo(itemstack1, 0, te.FOUTPUT(), false) && !(this.moveItemStackTo(itemstack1, te.UPGRADES()[0], te.getInvSize, false))) {
                if (index < te.getInvSize + 27) {
                    if (!this.moveItemStackTo(itemstack1, te.getInvSize - 1 + 27, this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, te.getInvSize - 1, te.getInvSize + 27, false)) {
                    return ItemStack.EMPTY;
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

    protected void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }
}
