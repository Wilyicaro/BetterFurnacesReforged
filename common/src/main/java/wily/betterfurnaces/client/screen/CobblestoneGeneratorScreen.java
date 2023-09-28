package wily.betterfurnaces.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.fluid.FluidStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluids;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.inventory.CobblestoneGeneratorMenu;
import wily.betterfurnaces.network.Messages;
import wily.betterfurnaces.network.PacketCobblestoneRecipeUpdate;
import wily.betterfurnaces.util.FluidRenderUtil;

public class CobblestoneGeneratorScreen extends AbstractBasicScreen<CobblestoneGeneratorMenu> {

    public ResourceLocation GUI = new ResourceLocation(BetterFurnacesReforged.MOD_ID , "textures/container/cobblestone_generator_gui.png");
    public static final ResourceLocation WIDGETS = new ResourceLocation(BetterFurnacesReforged.MOD_ID , "textures/container/widgets.png");
    Inventory playerInv;
    Component name;


    public CobblestoneGeneratorScreen(CobblestoneGeneratorMenu t, Inventory inv, Component name) {
        super(t, inv, name);
        playerInv = inv;
        this.name = name;
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = 7 + imageWidth / 2 - font.width(name.getString()) / 2;
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics,mouseX,mouseY);
        int actualMouseX = mouseX - leftPos;
        int actualMouseY = mouseY - topPos;
        if (mouseX >= 81 && mouseX <= 95 && mouseY >= 25 && mouseY <= 39) {
            graphics.renderTooltip(font, getMenu().be.getResult(), actualMouseX, actualMouseY);
        }
    }


    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        double actualMouseX = mouseX - leftPos;
        double actualMouseY = mouseY - topPos;
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(GUI, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
        graphics.pose().pushPose();
        graphics.pose().translate(leftPos + 88,topPos + 32,0);
        graphics.pose().scale(0.75F,0.75F,0.75F);
        graphics.pose().translate(-8,-8,0);
        graphics.renderItem(getMenu().be.getResult(), 0, 0);
        graphics.pose().popPose();
        RenderSystem.setShaderTexture(0, WIDGETS);
            if (actualMouseX>= 81 && actualMouseX <= 95 && actualMouseY >= 25 && actualMouseY <= 39){
                graphics.blit(WIDGETS, leftPos + 81, topPos + 25, 98, 157, 14, 14);
        } else graphics.blit(WIDGETS, leftPos + 81, topPos + 25, 84, 157, 14, 14);
        int i;
        i = this.getMenu().getCobTimeScaled(16);
        if (i > 0) {
            FluidStack lava = FluidStack.create(Fluids.FLOWING_LAVA, 1000);
            FluidStack water = FluidStack.create(Fluids.WATER, 1000);
            FluidRenderUtil.renderTiledFluid(graphics,   leftPos + 58, topPos + 44, 17, 12, lava, false);
            FluidRenderUtil.renderTiledFluid(graphics,  leftPos + 101, topPos + 44, 17, 12, water, true);
            graphics.blit(GUI, leftPos + 58, topPos + 44, 176, 24, i + 1, 12);
            graphics.blit(GUI, leftPos + 117 - i, topPos + 44, 192 - i, 36, 17, 12);

        }
        graphics.blit(GUI, leftPos + 58, topPos + 44, 176, 0, 17, 12);
        graphics.blit(GUI, leftPos + 101, topPos + 44, 176, 12, 17, 12);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        double actualMouseX = mouseX - leftPos;
        double actualMouseY = mouseY - topPos;
        if (actualMouseX >= 81 && actualMouseX <= 95 && actualMouseY >= 25 && actualMouseY <= 39) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 0.3F, 0.3F));
            Messages.INSTANCE.sendToServer(new PacketCobblestoneRecipeUpdate(this.getMenu().getPos()));
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

}
