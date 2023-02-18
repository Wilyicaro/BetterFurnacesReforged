package wily.betterfurnaces.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.inventory.AbstractUpgradeMenu;
import wily.betterfurnaces.items.ColorUpgradeItem;


public abstract class AbstractUpgradeScreen<T extends AbstractUpgradeMenu> extends AbstractBasicScreen<T> {
    private Component name;

    public ResourceLocation GUI = new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/upgrades_gui.png");

    public AbstractUpgradeScreen(T t, Inventory inv, Component name) {
        super(t, inv, name);
        imageHeight = 94;
        this.name = name;
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int i, int j) {

    }

    @Override
    public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        renderColorFurnace(matrix, partialTicks, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
    }
    @Override
    protected void renderBg(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
        ItemStack stack = ((ColorUpgradeItem.ContainerColorUpgrade) this.getMenu()).itemStackBeingHeld;
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);

        this.blit(matrix, relX(), relY(), 0, 0, this.imageWidth, this.imageHeight);
        itemRenderer.renderGuiItem(stack, this.relX() + 154, this.relY() + 6);
        this.font.draw(matrix, name, (width - this.font.width(name) )/2, this.relY() + 8, 4210752);
    }
    protected void renderColorFurnace(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
    }

}
