package wily.betterfurnaces.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.AbstractSmeltingBlockEntity;
import wily.betterfurnaces.util.DirectionUtil;


public abstract class AbstractSmeltingMenu extends AbstractInventoryMenu<AbstractSmeltingBlockEntity> {



    public AbstractSmeltingMenu(MenuType<?> containerType, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        this(containerType, windowId, world, pos, playerInventory, player, new SimpleContainerData(5));
    }

    public AbstractSmeltingMenu(MenuType<?> containerType, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
        super(containerType, windowId, world, pos, playerInventory, player, fields);
        checkContainerDataCount(this.fields, 5);
    }

    public void addInventorySlots(){
        this.addSlot(new SlotInput(be, 0, 54, 18));
        this.addSlot(new SlotFuel(this.be, 1, 54, 54));
        this.addSlot(new SlotOutput(playerEntity, be, 2, 116, 35));
        this.addSlot(new SlotUpgrade(be, 3, 8, 18));
        this.addSlot(new SlotUpgrade(be, 4, 8, 36));
        this.addSlot(new SlotUpgrade(be, 5, 8, 54));
    }

    @OnlyIn(Dist.CLIENT)
    public boolean showInventoryButtons() {
        return this.fields.get(4) == 1;
    }

    @OnlyIn(Dist.CLIENT)
    public int getRedstoneMode() {
        return this.be.getRedstoneSetting();
    }

    @OnlyIn(Dist.CLIENT)
    public int getComSub() {
        return this.be.getRedstoneComSub();
    }

    @OnlyIn(Dist.CLIENT)
    public boolean getAutoInput() {
        return this.be.getAutoInput() == 1;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean getAutoOutput() {
        return this.be.getAutoOutput() == 1;
    }

    @OnlyIn(Dist.CLIENT)
    public Component getTooltip(int index) {
        switch (be.furnaceSettings.get(index))
        {
            case 1:
                return Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_input");
            case 2:
                return Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_output");
            case 3:
                return Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_input_output");
            case 4:
                return Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_fuel");
            default:
                return Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_none");
        }
    }

    @OnlyIn(Dist.CLIENT)
    public int getSettingTop()
    {
        return this.be.getSettingTop();
    }

    @OnlyIn(Dist.CLIENT)
    public int getSettingBottom()
    {
        return this.be.getSettingBottom();
    }

    @OnlyIn(Dist.CLIENT)
    public int getSettingFront()
    {
        return this.be.getSettingFront();
    }

    @OnlyIn(Dist.CLIENT)
    public int getSettingBack()
    {
        return this.be.getSettingBack();
    }

    @OnlyIn(Dist.CLIENT)
    public int getSettingLeft()
    {
        return this.be.getSettingLeft();
    }

    @OnlyIn(Dist.CLIENT)
    public int getSettingRight()
    {
        return this.be.getSettingRight();
    }

    @OnlyIn(Dist.CLIENT)
    public int getIndexFront()
    {
        return this.be.getIndexFront();
    }

    @OnlyIn(Dist.CLIENT)
    public int getIndexBack()
    {
        return this.be.getIndexBack();
    }

    @OnlyIn(Dist.CLIENT)
    public int getIndexLeft()
    {
        return this.be.getIndexLeft();
    }

    @OnlyIn(Dist.CLIENT)
    public int getIndexRight()
    {
        return this.be.getIndexRight();
    }

    @OnlyIn(Dist.CLIENT)
    public BlockPos getPos() {
        return this.be.getBlockPos();
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
        if (isXp) facing = DirectionUtil.fromId(be.getIndexFront());
        int cur = be.getCapability(ForgeCapabilities.FLUID_HANDLER, facing).resolve().get().getFluidInTank(0).getAmount();
        int max = be.getCapability(ForgeCapabilities.FLUID_HANDLER, facing).resolve().get().getTankCapacity(0);
        return cur * pixels / max;
    }
    public FluidStack getFluidStackStored(boolean isXp) {
        Direction facing = null;
        if (isXp) facing = DirectionUtil.fromId(be.getIndexFront());
        return be.getCapability(ForgeCapabilities.FLUID_HANDLER, facing).resolve().get().getFluidInTank(0);
    }
    public int getEnergyStoredScaled(int pixels) {
        int cur = be.getCapability(ForgeCapabilities.ENERGY, null).resolve().get().getEnergyStored();
        int max = be.getCapability(ForgeCapabilities.ENERGY, null).resolve().get().getMaxEnergyStored();
        return cur * pixels / max;
    }
    public int getEnergyStored() {
        return be.getCapability(ForgeCapabilities.ENERGY, null).resolve().get().getEnergyStored();
    }
    public int getEnergyMaxStored() {
        return be.getCapability(ForgeCapabilities.ENERGY, null).resolve().get().getMaxEnergyStored();
    }
    public int BurnTimeGet(){
        return this.fields.get(0);
    }


}
