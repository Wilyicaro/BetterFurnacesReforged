package wily.betterfurnaces.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.Direction;
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
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.tileentity.AbstractSmeltingTileEntity;
import wily.betterfurnaces.util.DirectionUtil;


public abstract class AbstractSmeltingContainer extends AbstractInventoryContainer<AbstractSmeltingTileEntity> {
    

    public AbstractSmeltingContainer(ContainerType<?> containerType, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        this(containerType, windowId, world, pos, playerInventory, player, new IntArray(5));
    }

    public AbstractSmeltingContainer(ContainerType<?> containerType, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, IIntArray fields) {
        super(containerType, windowId, world, pos, playerInventory, player, fields);
        checkContainerDataCount(this.fields, 5);
    }

    public void addInventorySlots() {
        this.addSlot(new SlotInput(te, 0, 54, 18));
        this.addSlot(new SlotFuel(this.te, 1, 54, 54));
        this.addSlot(new SlotOutput(playerEntity, te, 2, 116, 35));
        this.addSlot(new SlotUpgrade(te, 3, 8, 18));
        this.addSlot(new SlotUpgrade(te, 4, 8, 36));
        this.addSlot(new SlotUpgrade(te, 5, 8, 54));
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
        switch (te.furnaceSettings.get(index)) {
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
    public int getSettingTop() {
        return this.te.getSettingTop();
    }

    @OnlyIn(Dist.CLIENT)
    public int getSettingBottom() {
        return this.te.getSettingBottom();
    }

    @OnlyIn(Dist.CLIENT)
    public int getSettingFront() {
        return this.te.getSettingFront();
    }

    @OnlyIn(Dist.CLIENT)
    public int getSettingBack() {
        return this.te.getSettingBack();
    }

    @OnlyIn(Dist.CLIENT)
    public int getSettingLeft() {
        return this.te.getSettingLeft();
    }

    @OnlyIn(Dist.CLIENT)
    public int getSettingRight() {
        return this.te.getSettingRight();
    }

    @OnlyIn(Dist.CLIENT)
    public int getIndexFront() {
        return this.te.getIndexFront();
    }

    @OnlyIn(Dist.CLIENT)
    public int getIndexBack() {
        return this.te.getIndexBack();
    }

    @OnlyIn(Dist.CLIENT)
    public int getIndexLeft() {
        return this.te.getIndexLeft();
    }

    @OnlyIn(Dist.CLIENT)
    public int getIndexRight() {
        return this.te.getIndexRight();
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

    public int BurnTimeGet() {
        return this.fields.get(0);
    }

}
