package wily.betterfurnaces.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import wily.betterfurnaces.inventory.AbstractInventoryMenu;
import wily.factoryapi.base.client.IWindowWidget;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBasicScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements IWindowWidget {

    public AbstractBasicScreen(T p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }
    protected final List<Widget> nestedWidgets = new ArrayList<>();

    @Override
    protected void init() {
        nestedWidgets.clear();
        super.init();
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        if (menu instanceof AbstractInventoryMenu<?> ) {
            ((AbstractInventoryMenu<?>) menu).be.syncAdditionalMenuData(menu, ((AbstractInventoryMenu<?>) menu).player);
        }
        super.renderBackground(poseStack);
        super.render(poseStack, i, j, f);
        super.renderTooltip(poseStack,i,j);
        IWindowWidget.super.render(poseStack,i,j,f);
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        if (IWindowWidget.super.mouseClicked(d,e,i)) return true;
        return super.mouseClicked(d, e, i);
    }
    @Override
    public boolean mouseReleased(double d, double e, int i) {
        if (IWindowWidget.super.mouseReleased(d,e,i)) return true;
        return super.mouseReleased(d, e, i);
    }

    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        if (IWindowWidget.super.mouseDragged(d,e,i,f,g)) return true;
        return super.mouseDragged(d, e, i, f, g);
    }

    @Override
    public Rect2i getBounds() {
        return new Rect2i(leftPos,topPos,width,height);
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public <R extends Widget> R addNestedWidget(R widget) {
        nestedWidgets.add(widget);
        return widget;
    }

    @Override
    public List<? extends Widget> getNestedWidgets() {
        return nestedWidgets;
    }

    protected void renderGuiItem(PoseStack poseStack, ItemStack stack, int Posx, int Posy, float scaleX, float scaleY) {

        minecraft.getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate(Posx, Posy, 100.0F + itemRenderer.blitOffset);
        posestack.translate(8.0D, 8.0D, 0.0D);
        posestack.scale(1.0F, -1.0F, 1.0F);
        posestack.scale(16.0F, 16.0F, 16.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack posestack1 = new PoseStack();
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        posestack1.scale(scaleX,scaleY,1.0F);
        itemRenderer.render(stack, ItemTransforms.TransformType.GUI, false, posestack1, multibuffersource$buffersource, 15728880, OverlayTexture.NO_OVERLAY, itemRenderer.getModel(stack, (Level)null, (LivingEntity)null, 0));
        multibuffersource$buffersource.endBatch();
        RenderSystem.enableDepthTest();
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
    }
}
