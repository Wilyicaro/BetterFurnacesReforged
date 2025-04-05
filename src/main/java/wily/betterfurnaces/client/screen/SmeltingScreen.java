package wily.betterfurnaces.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.FactoryUpgradeSettings;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.inventory.SmeltingMenu;
import wily.betterfurnaces.items.FactoryUpgradeItem;
import wily.betterfurnaces.items.GeneratorUpgradeItem;
import wily.betterfurnaces.network.ShowSettingsSyncPayload;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.base.client.FactoryGuiGraphics;
import wily.factoryapi.base.client.drawable.DrawableStatic;
import wily.factoryapi.base.client.drawable.DrawableStaticProgress;
import wily.factoryapi.base.client.drawable.FactoryDrawableButton;
import wily.factoryapi.base.client.drawable.IFactoryDrawableType;
import wily.factoryapi.base.network.CommonNetwork;
import wily.factoryapi.util.StorageStringUtil;

import java.util.List;

import static wily.betterfurnaces.client.screen.BetterFurnacesDrawables.*;
import static wily.factoryapi.util.StorageStringUtil.getFluidTooltip;

public class SmeltingScreen<T extends SmeltingMenu> extends AbstractBasicScreen<T> {
    public static final ResourceLocation GUI = BetterFurnacesReforged.createModLocation("textures/container/furnace_gui.png");
    Inventory playerInv;

    private FactoryDrawableButton showConfigButton;



    public SmeltingScreen(T t, Inventory inv, Component name) {
        super(t, inv, name);
        playerInv = inv;
    }

    public ResourceLocation getGuiLocation() {
        return GUI;
    }

    protected int factoryShowButtonY() {
        return 3;
    }

    protected DrawableStatic fluidTankType() {
        return FLUID_TANK.createStatic( leftPos + 73, topPos + 49);
    }

    protected DrawableStaticProgress energyTankType() {
        int[] pos =  menu.be.hasUpgrade(ModObjects.GENERATOR.get()) ? new int[]{116,26} : new int[]{menu.be.hasUpgradeType(ModObjects.STORAGE.get()) ? 26 : 31,17};
        return (menu.be.hasUpgradeType(ModObjects.STORAGE.get()) ? THIN_ENERGY_CELL : ENERGY_CELL).createStatic(  leftPos +pos[0], topPos + pos[1]);
    }

    protected DrawableStatic generatorTankType() {
        return MINI_FLUID_TANK.createStatic( leftPos + 54, topPos + 18);
    }

    protected DrawableStatic xpTankType() {
        return MINI_FLUID_TANK.createStatic( leftPos + 73, topPos + 49);
    }

    public boolean storedFactoryUpgradeType(int type){
        if (getMenu().be.hasUpgradeType(ModObjects.FACTORY.get())) {
            FactoryUpgradeItem stack = ((FactoryUpgradeItem)getMenu().be.getUpgradeTypeSlotItem(ModObjects.FACTORY.get()).getItem());
            return switch (type){
                case 1 -> stack.canInput;
                case 2 -> stack.canOutput;
                case 3 -> stack.pipeSide;
                case 4 -> stack.redstoneSignal;
                default -> true;
            };
        }
        return false;
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (this.imageWidth - minecraft.font.width(getTitle().getString())) / 2;
        if (getMenu().be.isForge())
            inventoryLabelX = (this.imageWidth - this.minecraft.font.width(playerInventoryTitle.getString())) / 2;
        inventoryLabelY = this.imageHeight - 94;
        titleLabelY = imageHeight - 160;
        showConfigButton = addNestedRenderable(new FactoryDrawableButton(leftPos + 7, topPos + factoryShowButtonY(), FACTORY_BUTTON).selection(IFactoryDrawableType.Direction.HORIZONTAL).visible(()->storedFactoryUpgradeType(0)).onPress((b,i)-> CommonNetwork.sendToServer(new ShowSettingsSyncPayload(getMenu().getPos(),b.isSelected() ? 1 : 0))));
        addNestedRenderable(new FactoryUpgradeWindow(showConfigButton,leftPos - 53,topPos + factoryShowButtonY(),this));
    }

    @Override
    public void render(GuiGraphics graphics, int i, int j, float f) {
        showConfigButton.select(getMenu().showInventoryButtons());
        if (showConfigButton.isSelected())
            showConfigButton.tooltips(getShiftInfoGui());
        else showConfigButton.tooltip(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_open"));
        super.render(graphics, i, j, f);
        showConfigButton.clearTooltips();
    }

    public static List<Component> getShiftInfoGui() {
        List<Component> list = Lists.newArrayList();
        list.add(Component.translatable(("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_close")));
        MutableComponent tooltip1 = Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_hold_shift");
        MutableComponent shift = Component.literal("[Shift]");
        MutableComponent tooltip2 = Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_shift_more_options");
        tooltip1.withStyle(ChatFormatting.GRAY);
        shift.withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC);
        tooltip2.withStyle(ChatFormatting.GRAY);
        list.add(tooltip1.append(shift).append(tooltip2));
        return list;
    }
    @Override
    public Component getTitle() {
        return getMenu().be.getName();
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, getTitle(), this.titleLabelX, this.titleLabelY, 4210752, false);
        graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
        int actualMouseX = mouseX - leftPos;
        int actualMouseY = mouseY - topPos;
        if (getMenu().be.isLiquid() && fluidTankType().inMouseLimit(mouseX, mouseY))
            graphics.renderTooltip(font,getFluidTooltip("tooltip.factory_api.fluid_stored", getMenu().be.fluidTank), actualMouseX, actualMouseY);
        if (getMenu().be.hasUpgrade(ModObjects.GENERATOR.get()) && generatorTankType().inMouseLimit(mouseX, mouseY)){
            ItemStack gen = getMenu().be.getUpgradeSlotItem(ModObjects.GENERATOR.get());
            graphics.renderTooltip(font, getFluidTooltip("tooltip.factory_api.fluid_stored", FactoryAPIPlatform.getItemFluidHandler(gen)), actualMouseX, actualMouseY);
        }if ((getMenu().be.hasUpgrade(ModObjects.ENERGY.get()) || getMenu().be.hasUpgrade(ModObjects.GENERATOR.get())) && energyTankType().inMouseLimit(mouseX,mouseY)){
            graphics.renderTooltip(font, StorageStringUtil.getEnergyTooltip("tooltip.factory_api.energy_stored", getMenu().be.energyStorage), actualMouseX, actualMouseY);
        }
        if (getMenu().be.hasXPTank() && xpTankType().inMouseLimit(mouseX, mouseY))
            graphics.renderTooltip(font,getFluidTooltip("tooltip.factory_api.fluid_stored", getMenu().be.xpTank), actualMouseX, actualMouseY);

    }

    protected void blitSmeltingSprites(GuiGraphics graphics) {
        int i;
        if ((this.getMenu()).getBurnTime() > 0) {
            i = (this.getMenu()).getBurnLeftScaled(13);
            FactoryGuiGraphics.of(graphics).blit(getGuiLocation(), leftPos + 55, topPos + 37 + 12 - i, 176, 12 - i, 14, i + 1);
        }
        i = (this.getMenu()).getCookScaled(24);
        FactoryGuiGraphics.of(graphics).blit(getGuiLocation(), leftPos + 79, topPos + 34, 176, 14, i + 1, 16);
        SLOT.draw(graphics, leftPos + 53, topPos + 17);
        boolean storage = menu.be.hasUpgrade(ModObjects.STORAGE.get());
        if (storage) {
            SLOT.draw(graphics, leftPos + 35, topPos + 17);
            SLOT.draw(graphics, leftPos + 35, topPos + 53);
        }
        if (!getMenu().be.hasUpgrade(ModObjects.GENERATOR.get())) {
            BIG_SLOT.draw(graphics, leftPos + 111, topPos + 30);
            if (storage) SLOT.draw(graphics, leftPos + 137, topPos + 34);
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        FactoryGuiGraphics.of(graphics).blit(getGuiLocation(), leftPos, topPos, 0, 0, imageWidth, imageHeight);
        blitSmeltingSprites(graphics);
        if (getMenu().be.hasUpgrade(ModObjects.ENERGY.get()) || getMenu().be.hasUpgrade(ModObjects.GENERATOR.get())) {
            boolean storage = menu.be.hasUpgradeType(ModObjects.STORAGE.get());
            FactoryGuiGraphics.of(graphics).blit(WIDGETS, energyTankType().getX(), energyTankType().getY(), 240 + (storage ? 8 : 0), 34 * (storage ? 2 : 1), 16 - (storage ? 8 : 0), 34);
            energyTankType().drawProgress(graphics, this.getMenu().getEnergyStored(),this.getMenu().getMaxEnergyStored());
        }if (getMenu().be.isLiquid()){
            FactoryGuiGraphics.of(graphics).blit(WIDGETS, fluidTankType().getX(), fluidTankType().getY(), 192, 38, 20, 22);
            fluidTankType().drawAsFluidTank(graphics, this.getMenu().be.fluidTank.getFluidInstance(), this.getMenu().be.fluidTank.getMaxFluid(),true);
        }

        if (this.getMenu().be.hasXPTank()) {
            FactoryGuiGraphics.of(graphics).blit(WIDGETS, xpTankType().getX(), xpTankType().getY(), 208, 0, 16, 16);
            xpTankType().drawAsFluidTank(graphics, this.getMenu().be.xpTank.getFluidInstance(), 1,true);
        }
        if (this.getMenu().be.hasUpgrade(ModObjects.GENERATOR.get())) {
            ItemStack generatorUp = getMenu().be.getUpgradeSlotItem(ModObjects.GENERATOR.get());
            generatorTankType().drawAsFluidTank(graphics, FactoryAPIPlatform.getItemFluidHandler(generatorUp).getFluidInstance(),4000,true);
        }
        if (storedFactoryUpgradeType(3) && showConfigButton.isSelected()) {
            boolean input = false;
            boolean output = false;
            boolean both = false;
            boolean fuel = false;
            for (int set : menu.be.furnaceSettings.getFurnaceSetting(FactoryUpgradeSettings.Type.SIDES)) {
                if (set == 1) input = true;
                else if (set == 2) output = true;
                else if (set == 3) both = true;
                else if (set == 4) fuel = true;
            }
            blitSlotsLayer(graphics, input, both, fuel, output);
        }
    }


    protected void blitSlotsLayer(GuiGraphics graphics, boolean input, boolean both, boolean fuel, boolean output){
        boolean storage = menu.be.hasUpgradeType(ModObjects.STORAGE.get());
        if (!getMenu().be.hasUpgrade(ModObjects.GENERATOR.get())) {
            if (input || both) {
                if (storage) INPUT_SLOT_OUTLINE.draw(graphics, leftPos + 35, topPos + 17);
                INPUT_SLOT_OUTLINE.draw(graphics, leftPos + 53, topPos + 17);
            }
            if (output || both) {
                if (storage) OUTPUT_SLOT_OUTLINE.draw(graphics, leftPos + 137, topPos + 34);
                BIG_OUTPUT_SLOT_OUTLINE.draw(graphics, leftPos + 111, topPos + 30);
            }
        }
        if (fuel) {
            if (storage)  FUEL_SLOT_OUTLINE.draw(graphics, leftPos + 35, topPos + 53);
            FUEL_SLOT_OUTLINE.draw(graphics, leftPos + 53, topPos + 53);
        }
    }
}
