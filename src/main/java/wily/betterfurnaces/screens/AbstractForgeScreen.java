package wily.betterfurnaces.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.inventory.AbstractForgeContainer;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractForgeScreen<T extends AbstractForgeContainer> extends AbstractSmeltingScreen<T> {

    public AbstractForgeScreen(T t, PlayerInventory inv, ITextComponent name) {
        super(t, inv, name);
    }

    public ResourceLocation GUI() {
        return new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/forge_gui.png");
    }

    protected int FactoryShowButtonY() {
        return 44;
    }

    protected int[] FluidTank() {
        return new int[]{26, 98};
    } // 20x22pixels

    protected int[] EnergyTank() {
        return new int[]{8, 62};
    } // 16x34pixels

    protected int[] XPTank() {
        return new int[]{126, 102};
    } // 16x16pixels

    @Override
    public int getXSize() {
        return 176;
    }

    @Override
    public int getYSize() {
        return 206;
    }

    public int getGuiLeft() {
        return (this.width - getXSize()) / 2;
    }

    public int getGuiTop() {
        return (this.height - getYSize()) / 2;
    }

    @Override
    protected void blitSlotsLayer(MatrixStack matrix, boolean input, boolean both, boolean fuel, boolean output) {
        if (input || both) {
            this.blit(matrix, getGuiLeft() + 26, getGuiTop() + 61, 0, 171, 18, 18);
            this.blit(matrix, getGuiLeft() + 44, getGuiTop() + 61, 0, 171, 18, 18);
            this.blit(matrix, getGuiLeft() + 62, getGuiTop() + 61, 0, 171, 18, 18);
        }
        if (output || both) {
            this.blit(matrix, getGuiLeft() + 103, getGuiTop() + 75, 0, 229, 62, 26);
        }
        if (fuel) {
            this.blit(matrix, getGuiLeft() + 7, getGuiTop() + 99, 18, 171, 18, 18);
        }
    }

    @Override
    protected void blitSmeltingSprites(MatrixStack matrix) {
        int i;
        if (this.getMenu().BurnTimeGet() > 0) {
            i = this.getMenu().getBurnLeftScaled(13);
            this.blit(matrix, getGuiLeft() + 28, getGuiTop() + 93 - i, 176, 12 - i, 14, i + 1);
            this.blit(matrix, getGuiLeft() + 46, getGuiTop() + 93 - i, 176, 12 - i, 14, i + 1);
            this.blit(matrix, getGuiLeft() + 64, getGuiTop() + 93 - i, 176, 12 - i, 14, i + 1);
        }

        i = this.getMenu().getCookScaled(24);
        this.blit(matrix, getGuiLeft() + 80, getGuiTop() + 80, 176, 14, i + 1, 16);
    }
}
