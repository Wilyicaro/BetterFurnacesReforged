package wily.betterfurnaces.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.inventory.AbstractFuelVerifierContainer;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractFuelVerifierScreen<T extends AbstractFuelVerifierContainer> extends ContainerScreen<T> {

    public ResourceLocation GUI = new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/fuel_verifier_gui.png");
    PlayerInventory playerInv;
    ITextComponent name;

    public AbstractFuelVerifierScreen(T t, PlayerInventory inv, ITextComponent name) {
        super(t, inv, name);
        playerInv = inv;
        this.name = name;
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrix, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderLabels(MatrixStack matrix, int mouseX, int mouseY) {
        ITextComponent invname = this.playerInv.getDisplayName();
        ITextComponent burntime = new TranslationTextComponent("gui.betterfurnacesreforged.fuel.melts").append(new StringTextComponent(String.valueOf(this.getMenu().getBurnTime()))).append(new TranslationTextComponent("gui.betterfurnacesreforged.fuel.items"));
        this.minecraft.font.draw(matrix, invname, 7, this.getYSize() - 93, 4210752);
        if (this.getMenu().getBurnTime() > 0)
            this.minecraft.font.draw(matrix, burntime, this.getXSize() / 2 - this.minecraft.font.width(burntime.getString()) / 2, 6, 4210752);
        else
            this.minecraft.font.draw(matrix, name, 7 + this.getXSize() / 2 - this.minecraft.font.width(name.getString()) / 2, 6, 4210752);
    }

    @Override
    protected void renderBg(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GUI);
        int relX = (this.width - this.getXSize()) / 2;
        int relY = (this.height - this.getYSize()) / 2;
        this.blit(matrix, relX, relY, 0, 0, this.getXSize(), this.getYSize());
        int i;
        i = (this.getMenu()).getBurnTimeScaled(26);
        if (i > 0) {
            this.blit(matrix, getGuiLeft() + 74, getGuiTop() + 13 + 26 - i, 176, 24 - i, 28, i + 2);
        }
    }

    public static class FuelVerifierScreen extends AbstractFuelVerifierScreen<AbstractFuelVerifierContainer> {
        public FuelVerifierScreen(AbstractFuelVerifierContainer container, PlayerInventory inv, ITextComponent name) {
            super(container, inv, name);
        }
    }

}
