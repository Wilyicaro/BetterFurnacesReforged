package wily.betterfurnaces.client.screen;

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
    private final Component name;

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
        super.render(matrix, mouseX, mouseY, partialTicks);
        renderColorFurnace(matrix, partialTicks, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
        ItemStack stack = ((ColorUpgradeItem.ContainerColorUpgrade) this.getMenu()).itemStackBeingHeld;
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bind(GUI);

        this.blit(matrix, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
        renderGuiItem(stack, this.leftPos + 154, this.topPos + 6,1,1);
        this.font.draw(matrix, name, (width - this.font.width(name) )/2, this.topPos + 8, 4210752);
    }
    protected void renderColorFurnace(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
    }

}
