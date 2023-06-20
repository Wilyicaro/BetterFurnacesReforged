package wily.betterfurnaces.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.inventory.AbstractInventoryMenu;

public abstract class AbstractBasicScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    protected int relX() {return (this.width - this.imageWidth) / 2;
}
    protected int relY()  {return  (this.height - this.imageHeight) / 2;}
    public AbstractBasicScreen(T p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        if (menu instanceof AbstractInventoryMenu<?> ) {
            ((AbstractInventoryMenu<?>) menu).be.syncAdditionalMenuData(menu, ((AbstractInventoryMenu<?>) menu).player);
        }
        super.render(poseStack, i, j, f);
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
