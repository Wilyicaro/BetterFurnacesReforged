package wily.betterfurnaces.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.inventory.ForgeMenu;
import wily.factoryapi.base.client.FactoryGuiGraphics;
import wily.factoryapi.base.client.drawable.DrawableStatic;
import wily.factoryapi.base.client.drawable.DrawableStaticProgress;

import static wily.betterfurnaces.client.screen.BetterFurnacesDrawables.*;
import static wily.betterfurnaces.client.screen.BetterFurnacesDrawables.ENERGY_CELL;


public class ForgeScreen extends SmeltingScreen<ForgeMenu> {
    public static final ResourceLocation GUI = BetterFurnacesReforged.createModLocation("textures/container/forge_gui.png");

    public ForgeScreen(ForgeMenu t, Inventory inv, Component name) {
        super(t, inv, name);
    }

    @Override
    public ResourceLocation getGuiLocation() {
        return GUI;
    }

    @Override
    protected int factoryShowButtonY(){
        return 44;
    }

    @Override
    protected DrawableStatic fluidTankType() {
        return FLUID_TANK.createStatic( leftPos + 26, topPos + 98);
    }

    @Override
    protected DrawableStaticProgress energyTankType() {
        return ENERGY_CELL.createStatic(  leftPos + 8, topPos + 62);
    }

    @Override
    protected DrawableStatic xpTankType() {
        return MINI_FLUID_TANK.createStatic( leftPos + 126, topPos + 102);
    }


    @Override
    protected void init() {
        imageHeight = 206;
        super.init();
    }

    @Override
    protected void blitSlotsLayer(GuiGraphics graphics, boolean input, boolean both, boolean fuel, boolean output){
        if (input || both) {
            FactoryGuiGraphics.of(graphics).blit(WIDGETS, leftPos + 26, topPos + 61, 0, 171, 18, 18);
            FactoryGuiGraphics.of(graphics).blit(WIDGETS, leftPos + 44, topPos + 61, 0, 171, 18, 18);
            FactoryGuiGraphics.of(graphics).blit(WIDGETS, leftPos + 62, topPos + 61, 0, 171, 18, 18);
        }
        if (output || both) {
            FactoryGuiGraphics.of(graphics).blit(WIDGETS, leftPos + 103, topPos + 75, 0, 229, 62, 26);
        }
        if (fuel) {
            FactoryGuiGraphics.of(graphics).blit(WIDGETS, leftPos + 7, topPos + 99, 18, 171, 18, 18);
        }
    }

    @Override
    protected void blitSmeltingSprites(GuiGraphics graphics) {
        int i;
        if (this.getMenu().getBurnTime() > 0) {
            i = this.getMenu().getBurnLeftScaled(13);
            FactoryGuiGraphics.of(graphics).blit(getGuiLocation(), leftPos + 28, topPos + 93 - i, 176, 12 - i, 14, i + 1);
            FactoryGuiGraphics.of(graphics).blit(getGuiLocation(), leftPos + 46, topPos + 93 - i, 176, 12 - i, 14, i + 1);
            FactoryGuiGraphics.of(graphics).blit(getGuiLocation(), leftPos + 64, topPos + 93 - i, 176, 12 - i, 14, i + 1);
        }

        i = this.getMenu().getCookScaled(24);
        FactoryGuiGraphics.of(graphics).blit(getGuiLocation(), leftPos + 80, topPos + 80, 176, 14, i + 1, 16);
    }

}
