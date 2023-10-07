package wily.betterfurnaces.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.inventory.ForgeMenu;
import wily.factoryapi.base.client.drawable.DrawableStatic;
import wily.factoryapi.base.client.drawable.DrawableStaticProgress;

import static wily.betterfurnaces.client.screen.BetterFurnacesDrawables.*;


public class ForgeScreen extends SmeltingScreen<ForgeMenu> {

    public ResourceLocation GUI() { return new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/forge_gui.png");}
    protected int factoryShowButtonY(){return 44;}
    protected DrawableStatic fluidTankType() {return FLUID_TANK.createStatic( leftPos + 26, topPos + 98);}

    protected DrawableStaticProgress energyTankType() {
        return ENERGY_CELL.createStaticProgress(  leftPos + 8, topPos + 62);
    }
    protected DrawableStatic xpTankType() {return MINI_FLUID_TANK.createStatic( leftPos + 126, topPos + 102);}

    public ForgeScreen(ForgeMenu t, Inventory inv, Component name) {
        super(t, inv, name);
    }

    @Override
    protected void init() {
        imageHeight = 206;
        super.init();
    }

    @Override
    protected void blitSlotsLayer(PoseStack stack, boolean input, boolean both, boolean fuel, boolean output){
        if (input || both) {
            this.blit(stack, leftPos + 26, topPos + 61, 0, 171, 18, 18);
            this.blit(stack, leftPos + 44, topPos + 61, 0, 171, 18, 18);
            this.blit(stack, leftPos + 62, topPos + 61, 0, 171, 18, 18);
        }
        if (output || both) {
            this.blit(stack, leftPos + 103, topPos + 75, 0, 229, 62, 26);
        }
        if (fuel) {
            this.blit(stack, leftPos + 7, topPos + 99, 18, 171, 18, 18);
        }
    }
    @Override
    protected void blitSmeltingSprites(PoseStack matrix) {
        int i;
        if (this.getMenu().BurnTimeGet() > 0) {
            i = this.getMenu().getBurnLeftScaled(13);
            for (int j = 0; j < 3; j++)
                this.blit(matrix, leftPos + 28 + j * 18, topPos + 93 - i, 176, 12 - i, 14, i + 1);
        }

        i = this.getMenu().getCookScaled(24);
        this.blit(matrix, leftPos + 80, topPos + 80, 176, 14, i + 1, 16);
    }

}
