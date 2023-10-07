package wily.betterfurnaces.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.inventory.FuelVerifierMenu;


public class FuelVerifierScreen extends AbstractBasicScreen<FuelVerifierMenu> {

    public ResourceLocation GUI = new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/fuel_verifier_gui.png");
    Inventory playerInv;
    Component name;

    public FuelVerifierScreen(FuelVerifierMenu t, Inventory inv, Component name) {
        super(t, inv, name);
        playerInv = inv;
        this.name = name;
    }


    @Override
    protected void renderLabels(PoseStack matrix, int mouseX, int mouseY) {
        Component invname = this.playerInv.getDisplayName();
        Component burntime = new TranslatableComponent("gui.betterfurnacesreforged.fuel.melts").append(new TextComponent(String.valueOf(( this.getMenu()).getBurnTime()))).append( new TranslatableComponent("gui.betterfurnacesreforged.fuel.items"));
        this.minecraft.font.draw(matrix, invname, 7, this.imageHeight - 93, 4210752);
        if (this.getMenu().getBurnTime() > 0)
        this.minecraft.font.draw(matrix, burntime, this.imageWidth / 2 - this.minecraft.font.width(burntime.getString()) / 2, 6, 4210752);
        else this.minecraft.font.draw(matrix, name, 7 + this.imageWidth / 2 - this.minecraft.font.width(name.getString()) / 2, 6, 4210752);
    }


    @Override
    protected void renderBg(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bind( GUI);

        this.blit(matrix, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
        int i;
        i = (this.getMenu()).getBurnTimeScaled(26);
        if (i > 0) {
            this.blit(matrix, leftPos + 74, topPos + 13 + 26 - i, 176, 24 - i, 28, i + 2);
        }
    }


}
