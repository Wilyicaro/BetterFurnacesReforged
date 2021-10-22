package wily.betterfurnaces.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.fluids.FluidStack;

public class FluidRenderUtil {
    public static void renderTiledFluid(PoseStack matrix, AbstractContainerScreen screen, int x, int y, int sizeX, int sizeY, FluidStack fluid, boolean hasColor){
            TextureAtlasSprite fluidSprite = screen.getMinecraft().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                    .apply(fluid.getFluid().getAttributes().getStillTexture(fluid)
                    );
            if (hasColor){
                int color = fluid.getFluid().getAttributes().getColor();
                float a = ((color & 0xFF000000) >> 24) / 255F;
                a = a <= 0.001F ? 1 : a;
                float r = ((color & 0xFF0000) >> 16) / 255F;
                float g = ((color & 0xFF00) >> 8) / 255F;
                float b = (color & 0xFF) / 255F;
                RenderSystem.setShaderColor(r,g,b,a);
            }
        RenderSystem.setShaderTexture(0, fluidSprite.atlas().location());
            screen.blit(matrix, screen.getGuiLeft() + x, screen.getGuiTop() + y, screen.getBlitOffset(), sizeX, sizeY, fluidSprite);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
