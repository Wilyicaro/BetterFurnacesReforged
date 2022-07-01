package wily.betterfurnaces.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.inventory.AbstractFuelVerifierMenu;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractFuelVerifierScreen<T extends AbstractFuelVerifierMenu> extends AbstractContainerScreen<T> {

    public ResourceLocation GUI = new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/fuel_verifier_gui.png");
    Inventory playerInv;
    Component name;

    public AbstractFuelVerifierScreen(T t, Inventory inv, Component name) {
        super(t, inv, name);
        playerInv = inv;
        this.name = name;
    }
    public static class FuelVerifierScreen extends AbstractFuelVerifierScreen<AbstractFuelVerifierMenu> {
        public FuelVerifierScreen(AbstractFuelVerifierMenu container, Inventory inv, Component name) {
            super(container, inv, name);
        }
    }

    @Override
    public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrix, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
    }


    @Override
    protected void renderLabels(PoseStack matrix, int mouseX, int mouseY) {
        Component invname = this.playerInv.getDisplayName();
        Component burntime = Component.translatable("gui.betterfurnacesreforged.fuel.melts").append(Component.literal(String.valueOf(((AbstractFuelVerifierMenu) this.getMenu()).getBurnTime()))).append( Component.translatable("gui.betterfurnacesreforged.fuel.items"));
        this.minecraft.font.draw(matrix, invname, 7, this.getYSize() - 93, 4210752);
        if (this.getMenu().getBurnTime() > 0)
        this.minecraft.font.draw(matrix, burntime, this.getXSize() / 2 - this.minecraft.font.width(burntime.getString()) / 2, 6, 4210752);
        else this.minecraft.font.draw(matrix, name, 7 + this.getXSize() / 2 - this.minecraft.font.width(name.getString()) / 2, 6, 4210752);
    }


    @Override
    protected void renderBg(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);
        int relX = (this.width - this.getXSize()) / 2;
        int relY = (this.height - this.getYSize()) / 2;
        this.blit(matrix, relX, relY, 0, 0, this.getXSize(), this.getYSize());
        int i;
        i = ((AbstractFuelVerifierMenu) this.getMenu()).getBurnTimeScaled(26);
        if (i > 0) {
            this.blit(matrix, getGuiLeft() + 74, getGuiTop() + 13 + 26 - i, 176, 24 - i, 28, i + 2);
        }
    }


}
