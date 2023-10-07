package wily.betterfurnaces.client.screen;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluids;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.inventory.CobblestoneGeneratorMenu;
import wily.betterfurnaces.network.Messages;
import wily.betterfurnaces.network.PacketCobblestoneRecipeUpdate;
import wily.betterfurnaces.network.PacketSyncAdditionalInt;
import wily.betterfurnaces.util.FluidRenderUtil;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.base.client.drawable.FactoryDrawableButton;

import java.util.List;

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
    protected void renderLabels(PoseStack matrix, int mouseX, int mouseY) {
        font.draw(matrix, this.playerInv.getDisplayName(), 7, this.imageHeight - 93, 4210752);
        font.draw(matrix, name, 7 + imageWidth / 2 - font.width(name.getString()) / 2, 6, 4210752);
        if (mouseX >= 81 && mouseX <= 95 && mouseY >= 25 && mouseY <= 39)
            this.renderTooltip(matrix, getMenu().be.getResult(), mouseX, mouseY);
    }

    @Override
    public List<? extends Widget> getNestedWidgets() {
        return ImmutableList.of(
                new FactoryDrawableButton(leftPos + 81, topPos + 25, BetterFurnacesDrawables.BUTTON).onPress((b,i)->Messages.INSTANCE.sendToServer(new PacketCobblestoneRecipeUpdate(this.getMenu().getPos()))).grave(0.3F).tooltips(getTooltipFromItem(getMenu().be.getResult())),
                new FactoryDrawableButton(leftPos + 9,topPos + 55,BetterFurnacesDrawables.SURFACE_BUTTON).icon(BetterFurnacesDrawables.getButtonIcon(1)).select(menu.be.hasAutoOutput()).onPress((b, i)-> Messages.INSTANCE.sendToServer(new PacketSyncAdditionalInt(this.getMenu().getPos(),menu.be.additionalSyncInts,menu.be.autoOutput,menu.be.hasAutoOutput() ? 0 : 1))).tooltips(ImmutableList.of(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_auto_output"), new TranslatableComponent("options." + (menu.be.hasAutoOutput() ? "on" : "off")))));
    }

    @Override
    protected void renderBg(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bind(GUI);
        blit(matrix, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
        renderGuiItem(getMenu().be.getResult(),leftPos + 80, topPos + 24, 0.75F, 0.75F);
        minecraft.getTextureManager().bind(WIDGETS);
        int i;
        i = this.getMenu().getCobTimeScaled(16);
        if (i > 0) {
            FluidStack lava = FluidStack.create(Fluids.FLOWING_LAVA, Fraction.ofWhole(FactoryAPIPlatform.getBucketAmount()));
            FluidStack water = FluidStack.create(Fluids.WATER, Fraction.ofWhole(FactoryAPIPlatform.getBucketAmount()));
            FluidRenderUtil.renderTiledFluid(matrix,   leftPos + 58, topPos + 44, 17, 12, lava, false);
            FluidRenderUtil.renderTiledFluid(matrix,  leftPos + 101, topPos + 44, 17, 12, water, true);
            minecraft.getTextureManager().bind(GUI);
            blit(matrix, leftPos + 58, topPos + 44, 176, 24, i + 1, 12);
            blit(matrix, leftPos + 117 - i, topPos + 44, 192 - i, 36, 17, 12);

        }
        minecraft.getTextureManager().bind( GUI);
        blit(matrix, leftPos + 58, topPos + 44, 176, 0, 17, 12);
        blit(matrix, leftPos + 101, topPos + 44, 176, 12, 17, 12);
    }

}
