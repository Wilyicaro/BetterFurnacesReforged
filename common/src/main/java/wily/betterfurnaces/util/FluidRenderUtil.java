package wily.betterfurnaces.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.FluidStackHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import org.jetbrains.annotations.Nullable;

public class FluidRenderUtil {
    public static void renderTiledFluid(PoseStack matrix, @Nullable AbstractContainerScreen abstractContainerScreen, int x, int y, int sizeX, int sizeY, FluidStack fluid, boolean hasColor){
        Screen screen = Minecraft.getInstance().screen;

        if (abstractContainerScreen !=null){
            screen = abstractContainerScreen;
        }
        TextureAtlasSprite fluidSprite = fluidSprite(fluid, hasColor);
        RenderSystem.setShaderTexture(0, fluidSprite.atlas().location());
            GuiComponent.blit(matrix, x, y, screen.getBlitOffset(), sizeX, sizeY, fluidSprite);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static TextureAtlasSprite fluidSprite(FluidStack fluid, boolean hasColor){
        TextureAtlasSprite fluidSprite = FluidStackHooks.getStillTexture(fluid);
        if (hasColor){
            int color = FluidStackHooks.getColor(fluid);
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
