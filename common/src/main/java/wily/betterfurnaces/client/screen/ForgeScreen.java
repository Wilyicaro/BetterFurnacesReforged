package wily.betterfurnaces.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.inventory.ForgeMenu;

import static wily.betterfurnaces.client.screen.BetterFurnacesDrawables.WIDGETS;


public class ForgeScreen extends SmeltingScreen<ForgeMenu> {

    public ResourceLocation GUI() { return new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/forge_gui.png");}
    protected int factoryShowButtonY(){return 44;}
    protected int[] fluidTankPos() { return new int[]{26,98};} // 20x22pixels
    protected int[] energyCellPos() { return new int[]{8,62};} // 16x34pixels
    protected int[] xpTankPos() { return new int[]{126,102};} // 16x16pixels

    public ForgeScreen(ForgeMenu t, Inventory inv, Component name) {
        super(t, inv, name);
    }

    @Override
    protected void init() {
        imageHeight = 206;
        super.init();
    }

    @Override
    protected void blitSlotsLayer(GuiGraphics graphics, boolean input, boolean both, boolean fuel, boolean output){
        if (input || both) {
            graphics.blit(WIDGETS, leftPos + 26, topPos + 61, 0, 171, 18, 18);
            graphics.blit(WIDGETS, leftPos + 44, topPos + 61, 0, 171, 18, 18);
            graphics.blit(WIDGETS, leftPos + 62, topPos + 61, 0, 171, 18, 18);
        }
        if (output || both) {
            graphics.blit(WIDGETS, leftPos + 103, topPos + 75, 0, 229, 62, 26);
        }
        if (fuel) {
            graphics.blit(WIDGETS, leftPos + 7, topPos + 99, 18, 171, 18, 18);
        }
    }
    @Override
    protected void blitSmeltingSprites(GuiGraphics graphics) {
        int i;
        if (this.getMenu().BurnTimeGet() > 0) {
            i = this.getMenu().getBurnLeftScaled(13);
            graphics.blit(GUI(), leftPos + 28, topPos + 93 - i, 176, 12 - i, 14, i + 1);
            graphics.blit(GUI(), leftPos + 46, topPos + 93 - i, 176, 12 - i, 14, i + 1);
            graphics.blit(GUI(), leftPos + 64, topPos + 93 - i, 176, 12 - i, 14, i + 1);
        }

        i = this.getMenu().getCookScaled(24);
        graphics.blit(GUI(), leftPos + 80, topPos + 80, 176, 14, i + 1, 16);
    }

}
