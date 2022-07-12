package wily.betterfurnaces.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class FluidRenderUtil {
    public static void renderTiledFluid(PoseStack matrix, @Nullable AbstractContainerScreen abstractContainerScreen, int x, int y, int sizeX, int sizeY, FluidStack fluid, boolean hasColor){
        Screen screen = Minecraft.getInstance().screen;
        int GuiLeft = 0;
        int GuiTop = 0;
        if (abstractContainerScreen !=null){
            screen = abstractContainerScreen;
            GuiLeft = abstractContainerScreen.getGuiLeft();
            GuiTop = abstractContainerScreen.getGuiTop();
        }
        TextureAtlasSprite fluidSprite = fluidSprite(fluid, hasColor);
        RenderSystem.setShaderTexture(0, fluidSprite.atlas().location());
            screen.blit(matrix, GuiLeft + x, GuiTop + y, screen.getBlitOffset(), sizeX, sizeY, fluidSprite);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static TextureAtlasSprite fluidSprite(FluidStack fluid, boolean hasColor){
        TextureAtlasSprite fluidSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(IClientFluidTypeExtensions.of(fluid.getFluid()).getStillTexture()
                );
        if (hasColor){
            int color = IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor();
            float a = ((color & 0xFF000000) >> 24) / 255F;
            a = a <= 0.001F ? 1 : a;
            float r = ((color & 0xFF0000) >> 16) / 255F;
            float g = ((color & 0xFF00) >> 8) / 255F;
            float b = (color & 0xFF) / 255F;
            RenderSystem.setShaderColor(r,g,b,a);
        }
        return fluidSprite;
    }
}
