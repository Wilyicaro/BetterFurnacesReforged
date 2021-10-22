package wily.betterfurnaces.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.container.ItemUpgradeContainerBase;
import wily.betterfurnaces.items.ItemColorUpgrade;

@OnlyIn(Dist.CLIENT)
public abstract class ItemUpgradeScreen<T extends ItemUpgradeContainerBase> extends AbstractContainerScreen<T> {
    private Component name;
    @Override
    public int getXSize() {
        return 176;
    }
    @Override
    public int getYSize() {
        return 94;
    }
    public int getGuiLeft() {
        return (this.width - getXSize()) / 2;
    }

    public int getGuiTop() {
        return (this.height - getYSize()) / 2;
    }

    public ResourceLocation GUI = new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/upgrades_gui.png");

    public ItemUpgradeScreen(T t, Inventory inv, Component name) {

        super(t, inv, name);
        this.name = name;
    }

    @Override
    public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        renderColorFurnace(matrix, partialTicks, mouseX, mouseY);
    }
    @Override
    protected void renderLabels(PoseStack matrix, int mouseX, int mouseY) {
        int actualMouseX = mouseX - ((this.width - this.getXSize()) / 2);
        int actualMouseY = mouseY - ((this.height - this.getYSize()) / 2);
    }
    @Override
    protected void init() {
        super.init();
    }
    @Override
    protected void renderBg(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
        ItemStack stack = ((ItemColorUpgrade.ContainerColorUpgrade) this.getMenu()).itemStackBeingHeld;
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);
        int relX = (this.width - this.getXSize()) / 2;
        int relY = (this.height - this.getYSize()) / 2;
        this.blit(matrix, relX, relY, 0, 0, this.getXSize(), this.getYSize());
        itemRenderer.renderGuiItem(stack, this.getGuiLeft() + 154, this.getGuiTop() + 6);
        this.font.draw(matrix, name, width / 2 - this.font.width(name) / 2, this.getGuiTop() + 8, 4210752);
    }
    protected void renderColorFurnace(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
    }

}
