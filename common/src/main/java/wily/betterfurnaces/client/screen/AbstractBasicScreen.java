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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
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

    protected void renderGuiItem(ItemStack stack, int i, int j, float scaleX, float scaleY) {
        RenderSystem.pushMatrix();
        minecraft.getTextureManager().bind(TextureAtlas.LOCATION_BLOCKS);
        minecraft.getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.translatef((float)i, (float)j, 100.0F + this.getBlitOffset());
        RenderSystem.translatef(8.0F, 8.0F, 0.0F);
        RenderSystem.scalef(1.0F, -1.0F, 1.0F);
        RenderSystem.scalef(16.0F, 16.0F, 16.0F);
        PoseStack poseStack = new PoseStack();
        poseStack.scale(scaleX, scaleY, 0.5F);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        if (!(stack.getItem() instanceof BlockItem) )Lighting.setupForFlatItems();
        itemRenderer.render(stack, ItemTransforms.TransformType.GUI, false, poseStack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, itemRenderer.getModel(stack, null, null));
        bufferSource.endBatch();
        RenderSystem.enableDepthTest();
        Lighting.setupFor3DItems();


        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();
        RenderSystem.popMatrix();

    }
}
