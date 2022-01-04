package wily.betterfurnaces.container;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
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
import wily.betterfurnaces.blockentity.BlockEntityForgeBase;
import wily.betterfurnaces.blockentity.BlockEntitySmeltingBase;
import wily.betterfurnaces.util.DirectionUtil;


public abstract class BlockForgeContainerBase extends AbstractContainerMenu {

    public BlockEntityForgeBase te;
    protected ContainerData fields;
    protected Player playerEntity;
    protected IItemHandler playerInventory;
    protected final Level world;
    public static final int[] UPMIS = { 11, 12, 13};
    private RecipeType<? extends AbstractCookingRecipe> recipeType;

    public BlockForgeContainerBase(MenuType<?> containerType, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        this(containerType, windowId, world, pos, playerInventory, player, new SimpleContainerData(5));
    }

    public BlockForgeContainerBase(MenuType<?> containerType, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
        super(containerType, windowId);
        this.te = (BlockEntityForgeBase) world.getBlockEntity(pos);
        this.recipeType = te.recipeType;

        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        this.world = playerInventory.player.level;
        this.fields = fields;

        this.addDataSlots(this.fields);
        int i = 20;
        int y1 = 62 - i;
        int y2 = 100 - i;
        int y3 = 80 - i;
        int y4 = 5 - i;
        this.addSlot(new SlotInput(te, 0, 27, y1));
        this.addSlot(new SlotInput(te, 1, 45, y1));
        this.addSlot(new SlotInput(te, 2, 63, y1));
        this.addSlot(new SlotFuel(this.te, 3, 8, y2));
        this.addSlot(new SlotOutput(playerEntity, te, 4, 108, y3));
        this.addSlot(new SlotOutput(playerEntity, te, 5, 126, y3));
        this.addSlot(new SlotOutput(playerEntity, te, 6, 144, y3));
        this.addSlot(new SlotUpgrade(te, 7, 7, y4));
        this.addSlot(new SlotUpgrade(te, 8, 25, y4));
        this.addSlot(new SlotUpgrade(te, 9, 43, y4));
        this.addSlot(new SlotHeater(te, 10, 79, y4));
        this.addSlot(new SlotUpgrade(te, 11, 115, y4));
        this.addSlot(new SlotUpgrade(te, 12, 133, y4));
        this.addSlot(new SlotUpgrade(te, 13, 151, y4));
        layoutPlayerInventorySlots(8, 106);
        checkContainerSize(this.te, 14);
        checkContainerDataCount(this.fields, 5);
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

    @Override
    public void slotsChanged(Container p_38868_) {
        this.broadcastChanges();
        te.setChanged();
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


    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
                    if (index < 14) {
                        if (!this.moveItemStackTo(itemstack1, 14, this.slots.size(), true)) {
                            return ItemStack.EMPTY;
                        }
                        slot.onQuickCraft(itemstack1, itemstack);
                    } else if (!this.moveItemStackTo(itemstack1, 0, 14, false)) {
                        if (index < 14 + 27) {
                            if (!this.moveItemStackTo(itemstack1, 14 + 27, this.slots.size(), true)) {
                                return ItemStack.EMPTY;
                            }
                        } else {
                            if (!this.moveItemStackTo(itemstack1, 14, 14 + 27, false)) {
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

    protected boolean hasRecipe(ItemStack stack) {
        ItemStack upgrade = this.getItems().get(3);

            if (this.recipeType != RecipeType.SMELTING) {
                this.recipeType = RecipeType.SMELTING;
            }
        return this.world.getRecipeManager().getRecipeFor((RecipeType)this.recipeType, new SimpleContainer(stack), this.world).isPresent();
    }
}
