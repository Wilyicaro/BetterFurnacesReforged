package wily.betterfurnaces.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ItemColorUpgrade;
import wily.betterfurnaces.items.ItemFuelEfficiency;
import wily.betterfurnaces.items.ItemOreProcessing;
import wily.betterfurnaces.items.ItemUpgradeMisc;
import wily.betterfurnaces.tileentity.BlockForgeTileBase;


public abstract class BlockForgeContainerBase extends Container {

    protected BlockForgeTileBase te;
    protected IIntArray fields;
    protected PlayerEntity playerEntity;
    protected IItemHandler playerInventory;
    protected final World world;
    public static final int[] UPMIS = { 11, 12, 13};
    private IRecipeType<? extends AbstractCookingRecipe> recipeType;

    public BlockForgeContainerBase(ContainerType<?> containerType, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        this(containerType, windowId, world, pos, playerInventory, player, new IntArray(5));
    }

    public BlockForgeContainerBase(ContainerType<?> containerType, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, IIntArray fields) {
        super(containerType, windowId);
        this.te = (BlockForgeTileBase) world.getBlockEntity(pos);
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
        this.addSlot(new SlotUpgrade(te, 7, 7, y4){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return ( stack.getItem() instanceof ItemOreProcessing);
            }});
        this.addSlot(new SlotUpgrade(te, 8, 25, y4){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return ( stack.getItem() instanceof ItemFuelEfficiency);
            }});
        this.addSlot(new SlotUpgrade(te, 9, 43, y4){
            @Override
            public boolean mayPlace(ItemStack stack) {return (stack.getItem() == Registration.XP.get() && !te.isLiquid());}});
        this.addSlot(new SlotHeater(te, 10, 79, y4));
        this.addSlot(new SlotUpgrade(te, 11, 115, y4){
            @Override
            public boolean mayPlace(ItemStack stack) {return (stack.getItem() == Registration.FACTORY.get() );}});
        this.addSlot(new SlotUpgrade(te, 12, 133, y4){
            @Override
            public boolean mayPlace(ItemStack stack) {return (stack.getItem() instanceof ItemColorUpgrade);}});
        this.addSlot(new SlotUpgrade(te, 13, 151, y4){
            @Override
            public boolean mayPlace(ItemStack stack) {return (stack.getItem() instanceof ItemUpgradeMisc);}});
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

    @OnlyIn(Dist.CLIENT)
    public ITextComponent getTooltip(int index) {
        switch (te.furnaceSettings.get(index))
        {
            case 1:
                return new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_input");
            case 2:
                return new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_output");
            case 3:
                return new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_input_output");
            case 4:
                return new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_fuel");
            default:
                return new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_none");
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
    public int getFluidStoredScaled(int pixels) {
        int cur = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).resolve().get().getFluidInTank(0).getAmount();
        int max = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).resolve().get().getTankCapacity(0);
        return cur * pixels / max;
    }
    public FluidStack getFluidStackStored() {
        return te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).resolve().get().getFluidInTank(0);
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
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
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

            if (this.recipeType != IRecipeType.SMELTING) {
                this.recipeType = IRecipeType.SMELTING;
            }
        return this.world.getRecipeManager().getRecipeFor((IRecipeType)this.recipeType, new Inventory(stack), this.world).isPresent();
    }
}
