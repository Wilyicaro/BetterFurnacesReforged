package wily.betterfurnaces.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.fluid.FluidStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.inventory.SmeltingMenu;
import wily.betterfurnaces.items.FactoryUpgradeItem;
import wily.betterfurnaces.items.GeneratorUpgradeItem;
import wily.betterfurnaces.network.Messages;
import wily.betterfurnaces.network.PacketShowSettingsButton;
import wily.factoryapi.ItemContainerUtil;
import wily.factoryapi.base.client.drawable.DrawableStatic;
import wily.factoryapi.base.client.drawable.DrawableStaticProgress;
import wily.factoryapi.base.client.drawable.FactoryDrawableButton;
import wily.factoryapi.base.client.drawable.IFactoryDrawableType;
import wily.factoryapi.util.StorageStringUtil;

import static wily.betterfurnaces.client.screen.BetterFurnacesDrawables.*;
import static wily.betterfurnaces.util.StringHelper.getShiftInfoGui;
import static wily.factoryapi.util.StorageStringUtil.getFluidTooltip;

public class SmeltingScreen<T extends SmeltingMenu> extends AbstractBasicScreen<T> {

    public ResourceLocation GUI() {return new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/furnace_gui.png");}
    public static final ResourceLocation WIDGETS = new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/widgets.png");
    Inventory playerInv;

    // Widgets x and y pos
    protected int factoryShowButtonY() {return 3;}
    protected DrawableStatic fluidTankType() {return FLUID_TANK.createStatic( leftPos + 73, topPos + 49);}

    protected DrawableStaticProgress energyTankType() {
        int[] pos =  menu.be.hasUpgrade(Registration.GENERATOR.get()) ? new int[]{116,26} : new int[]{menu.be.hasUpgradeType(Registration.STORAGE.get()) ? 26 : 31,17};
        return (menu.be.hasUpgradeType(Registration.STORAGE.get()) ? THIN_ENERGY_CELL : ENERGY_CELL).createStatic(  leftPos +pos[0], topPos + pos[1]);
    }
    protected DrawableStatic generatorTankType() {return MINI_FLUID_TANK.createStatic( leftPos + 54, topPos + 18);}

    protected DrawableStatic xpTankType() {return MINI_FLUID_TANK.createStatic( leftPos + 73, topPos + 49);}
    boolean storedFactoryUpgradeType(int type){
        if (getMenu().be.hasUpgradeType(Registration.FACTORY.get())) {
            FactoryUpgradeItem stack = ((FactoryUpgradeItem)getMenu().be.getUpgradeTypeSlotItem(Registration.FACTORY.get()).getItem());
            if (type == 0) return true;
            else if (type == 1)
                return stack.canInput;
            else if (type == 2)
                return stack.canOutput;
            else if (type == 3)
                return stack.pipeSide;
            else if (type == 4)
                return stack.redstoneSignal;
        }
        return false;
    }

    private FactoryDrawableButton showConfigButton;

    public SmeltingScreen(T t, Inventory inv, Component name) {
        super(t, inv, name);
        playerInv = inv;
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (this.imageWidth - minecraft.font.width(getTitle().getString())) / 2;
        if (getMenu().be.isForge())
            inventoryLabelX = (this.imageWidth - this.minecraft.font.width(playerInventoryTitle.getString())) / 2;
        inventoryLabelY = this.imageHeight - 94;
        titleLabelY = imageHeight - 160;
        showConfigButton = addNestedRenderable(new FactoryDrawableButton(leftPos + 7, topPos + factoryShowButtonY(), FACTORY_BUTTON).selection(IFactoryDrawableType.Direction.HORIZONTAL).visible(()->storedFactoryUpgradeType(0)).onPress((b, i)-> Messages.INSTANCE.sendToServer(new PacketShowSettingsButton(getMenu().getPos(),b.isSelected() ? 1 : 0))));
        addNestedRenderable(new FactoryUpgradeWindow(showConfigButton,leftPos - 53,topPos + factoryShowButtonY(),this));
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        showConfigButton.select(getMenu().showInventoryButtons());
        if (showConfigButton.isSelected())
            showConfigButton.tooltips(getShiftInfoGui());
        else showConfigButton.tooltip(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_open"));
        super.render(poseStack, i, j, f);
        showConfigButton.clearTooltips();
    }

    @Override
    public Component getTitle() {
        return getMenu().be.getName();
    }

    @Override
    protected void renderLabels(PoseStack matrix, int mouseX, int mouseY) {
        int invX = 7;
        int actualMouseX = mouseX - leftPos;
        int actualMouseY = mouseY - topPos;

        int titleX = (this.imageWidth - this.minecraft.font.width(getTitle().getString())) / 2;
        if (getMenu().be.isForge())
            invX = (this.imageWidth - this.minecraft.font.width(playerInventoryTitle.getString())) / 2;
        this.minecraft.font.draw(matrix, playerInventoryTitle, invX, this.imageHeight - 93, 4210752);
        this.minecraft.font.draw(matrix, getTitle(), titleX, imageHeight - 160, 4210752);
        if (getMenu().be.isLiquid() && fluidTankType().inMouseLimit(mouseX, mouseY))
            this.renderTooltip(matrix, getFluidTooltip("tooltip.factory_api.fluid_stored", getMenu().be.fluidTank),actualMouseX, actualMouseY);
        if (getMenu().be.hasUpgrade(Registration.GENERATOR.get()) && generatorTankType().inMouseLimit(mouseX, mouseY)){
            ItemStack gen = getMenu().be.getUpgradeSlotItem(Registration.GENERATOR.get());
            this.renderTooltip(matrix, getFluidTooltip("tooltip.factory_api.fluid_stored", ((GeneratorUpgradeItem)gen.getItem()).getFluidStorage(gen)), actualMouseX, actualMouseY);
        }if ((getMenu().be.hasUpgrade(Registration.ENERGY.get()) || getMenu().be.hasUpgrade(Registration.GENERATOR.get())) && energyTankType().inMouseLimit(mouseX,mouseY)){
            this.renderTooltip(matrix, StorageStringUtil.getEnergyTooltip("tooltip.factory_api.energy_stored", getMenu().be.energyStorage), actualMouseX, actualMouseY);
        }
        if (getMenu().be.hasXPTank() && xpTankType().inMouseLimit(mouseX, mouseY))
            this.renderTooltip(matrix,getFluidTooltip("tooltip.factory_api.fluid_stored", getMenu().be.xpTank), actualMouseX, actualMouseY);

    }

    protected void blitSmeltingSprites(PoseStack matrix) {
        int i;
        if ((this.getMenu()).BurnTimeGet() > 0) {
            i = (this.getMenu()).getBurnLeftScaled(13);
            blit(matrix, leftPos + 55, topPos + 37 + 12 - i, 176, 12 - i, 14, i + 1);
        }
        i = (this.getMenu()).getCookScaled(24);
        blit(matrix, leftPos + 79, topPos + 34, 176, 14, i + 1, 16);
        RenderSystem.setShaderTexture(0, WIDGETS);
        SLOT.draw(matrix, leftPos + 53, topPos + 17);
        boolean storage = menu.be.hasUpgradeType(Registration.STORAGE.get());
        if (storage){
            SLOT.draw(matrix, leftPos + 35, topPos + 17);
            SLOT.draw(matrix, leftPos + 35, topPos + 53);
        }
        if (!getMenu().be.hasUpgrade(Registration.GENERATOR.get())) {
            BIG_SLOT.draw(matrix, leftPos + 111, topPos + 30);
            if (storage) SLOT.draw(matrix, leftPos + 137, topPos + 34);
        }
    }
    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0,GUI());
        blit(stack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        blitSmeltingSprites(stack);
        RenderSystem.setShaderTexture(0,WIDGETS);
        if (getMenu().be.hasUpgrade(Registration.ENERGY.get()) || getMenu().be.hasUpgrade(Registration.GENERATOR.get())) {
            boolean storage = menu.be.hasUpgradeType(Registration.STORAGE.get());
            blit(stack, energyTankType().getX(), energyTankType().getY(), 240 + (storage ? 8 : 0), 34 * (storage ? 2 : 1), 16 - (storage ? 8 : 0), 34);
            energyTankType().drawProgress(stack, this.getMenu().getEnergyStored(),this.getMenu().getMaxEnergyStored());
        }if (getMenu().be.isLiquid()){
            blit(stack, fluidTankType().getX(), fluidTankType().getY(), 192, 38, 20, 22);
            fluidTankType().drawAsFluidTank(stack, this.getMenu().be.fluidTank.getFluidStack(), this.getMenu().be.fluidTank.getMaxFluid(),false);
        }
        if (this.getMenu().be.hasXPTank()) {
            blit(stack, xpTankType().getX(), xpTankType().getY(), 208, 0, 16, 16);
            xpTankType().drawAsFluidTank(stack, this.getMenu().be.xpTank.getFluidStack(), this.getMenu().be.xpTank.getMaxFluid(),true);
        }
        if (this.getMenu().be.hasUpgrade(Registration.GENERATOR.get())) {
            ItemStack generatorUp = getMenu().be.getUpgradeSlotItem(Registration.GENERATOR.get());
            generatorTankType().drawAsFluidTank(stack, ItemContainerUtil.getFluid(generatorUp),4 * FluidStack.bucketAmount(),true);
        }
        if (storedFactoryUpgradeType(3) && showConfigButton.isSelected()) {
            boolean input = false;
            boolean output = false;
            boolean both = false;
            boolean fuel = false;
            for (int set : menu.be.furnaceSettings.getFurnaceSetting("Settings")) {
                if (set == 1) input = true;
                else if (set == 2) output = true;
                else if (set == 3) both = true;
                else if (set == 4) fuel = true;
            }
            blitSlotsLayer(stack, input, both, fuel, output);
        }
    }

    protected void blitSlotsLayer(PoseStack stack, boolean input, boolean both, boolean fuel, boolean output){
        boolean storage = menu.be.hasUpgradeType(Registration.STORAGE.get());
        if (!getMenu().be.hasUpgrade(Registration.GENERATOR.get())) {
            if (input || both) {
                if (storage) INPUT_SLOT_OUTLINE.draw(stack, leftPos + 35, topPos + 17);
                INPUT_SLOT_OUTLINE.draw(stack, leftPos + 53, topPos + 17);
            }
            if (output || both) {
                if (storage) OUTPUT_SLOT_OUTLINE.draw(stack, leftPos + 137, topPos + 34);
                BIG_OUTPUT_SLOT_OUTLINE.draw(stack, leftPos + 111, topPos + 30);
            }
        }
        if (fuel) {
            if (storage)  FUEL_SLOT_OUTLINE.draw(stack, leftPos + 35, topPos + 53);
            FUEL_SLOT_OUTLINE.draw(stack, leftPos + 53, topPos + 53);
        }
    }

}
