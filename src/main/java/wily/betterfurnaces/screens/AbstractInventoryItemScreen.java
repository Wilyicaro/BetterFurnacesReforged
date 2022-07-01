package wily.betterfurnaces.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractInventoryItemScreen<T extends Container> extends ContainerScreen<T> {


    public AbstractInventoryItemScreen(T p_97741_, PlayerInventory p_97742_, ITextComponent p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    protected void renderGuiItem(ItemStack stack, int p_191962_2_, int p_191962_3_, float scaleX, float scaleY) {
        RenderSystem.pushMatrix();
        this.minecraft.getTextureManager().bind(AtlasTexture.LOCATION_BLOCKS);
        this.minecraft.getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS).setFilter(false, false);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.translatef((float) p_191962_2_, (float) p_191962_3_, 100.0F + this.getBlitOffset());
        RenderSystem.translatef(8.0F, 8.0F, 0.0F);
        RenderSystem.scalef(1.0F, -1.0F, 1.0F);
        RenderSystem.scalef(16.0F, 16.0F, 16.0F);
        MatrixStack matrixstack = new MatrixStack();
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
        matrixstack.scale(scaleX, scaleY, 0.5F);
        this.itemRenderer.render(stack, ItemCameraTransforms.TransformType.GUI, false, matrixstack, irendertypebuffer$impl, 15728880, OverlayTexture.NO_OVERLAY, this.itemRenderer.getModel(stack, null, null));
        irendertypebuffer$impl.endBatch();
        RenderSystem.enableDepthTest();
        RenderHelper.setupFor3DItems();

        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();
        RenderSystem.popMatrix();
    }
}
