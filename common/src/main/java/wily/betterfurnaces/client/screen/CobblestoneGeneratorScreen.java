package wily.betterfurnaces.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.fluid.FluidStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
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
import wily.betterfurnaces.network.PacketSettingsButton;
import wily.betterfurnaces.network.PacketSyncAdditionalInt;
import wily.betterfurnaces.util.FluidRenderUtil;
import wily.factoryapi.base.client.drawable.FactoryDrawableButton;
import wily.factoryapi.base.client.drawable.IFactoryDrawableType;

import java.util.List;

public class CobblestoneGeneratorScreen extends AbstractBasicScreen<CobblestoneGeneratorMenu> {

    public ResourceLocation GUI = new ResourceLocation(BetterFurnacesReforged.MOD_ID , "textures/container/cobblestone_generator_gui.png");
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
        titleLabelX = 7 + imageWidth / 2 - font.width(name.getString()) / 2;;
    }

    @Override
    public List<? extends Renderable> getNestedRenderables() {
        return List.of(
                new FactoryDrawableButton(leftPos + 81, topPos + 25, BetterFurnacesDrawables.BUTTON).onPress((b,i)->Messages.INSTANCE.sendToServer(new PacketCobblestoneRecipeUpdate(this.getMenu().getPos()))).grave(0.3F).tooltips(getTooltipFromItem(this.minecraft, getMenu().be.getResult())),
                new FactoryDrawableButton(leftPos + 9,topPos + 55,BetterFurnacesDrawables.SURFACE_BUTTON).icon(BetterFurnacesDrawables.getButtonIcon(1)).select(menu.be.hasAutoOutput()).onPress((b, i)-> Messages.INSTANCE.sendToServer(new PacketSyncAdditionalInt(this.getMenu().getPos(),menu.be.additionalSyncInts,menu.be.autoOutput,menu.be.hasAutoOutput() ? 0 : 1))).tooltips(List.of(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_auto_output"), Component.translatable("options." + (menu.be.hasAutoOutput() ? "on" : "off")))));
    }



    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(GUI, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
        graphics.pose().pushPose();
        graphics.pose().translate(leftPos + 88,topPos + 32,0);
        graphics.pose().scale(0.75F,0.75F,0.75F);
        graphics.pose().translate(-8,-8,0);
        graphics.renderItem(getMenu().be.getResult(), 0, 0);
        graphics.pose().popPose();
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


}
