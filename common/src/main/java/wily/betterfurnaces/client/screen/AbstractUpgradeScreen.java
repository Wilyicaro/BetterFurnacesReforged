package wily.betterfurnaces.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
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
    protected void renderLabels(GuiGraphics poseStack, int i, int j) {

    }


    @Override
    protected void init() {
        super.init();
    }
    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        ItemStack stack = ((ColorUpgradeItem.ContainerColorUpgrade) this.getMenu()).itemStackBeingHeld;
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        graphics.blit(GUI, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
        graphics.renderItem(stack, this.leftPos + 154, this.topPos + 6);
        graphics.drawString(font, name, (width - this.font.width(name) )/2, this.topPos + 8, 4210752,false);
    }

}
