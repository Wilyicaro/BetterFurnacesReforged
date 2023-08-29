package wily.betterfurnaces.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import wily.betterfurnaces.inventory.AbstractInventoryMenu;
import wily.factoryapi.base.client.IWindowWidget;

public abstract class AbstractBasicScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements IWindowWidget {

    protected int relX() {return (this.width - this.imageWidth) / 2;
}
    protected int relY()  {return  (this.height - this.imageHeight) / 2;}
    public AbstractBasicScreen(T p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    @Override
    public Rect2i getBounds() {
        return new Rect2i(leftPos,topPos,imageWidth,imageHeight);
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void render(GuiGraphics graphics, int i, int j, float f) {
        renderBackground(graphics);
        super.render(graphics, i, j, f);
        renderTooltip(graphics,i,j);
        renderButtons(graphics,i,j);
        renderButtonsTooltip(font, graphics,i,j);
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        if (mouseClickedButtons(d,e,i)) return true;
        return super.mouseClicked(d, e, i);
    }
    @Override
    public boolean mouseReleased(double d, double e, int i) {
        if (mouseReleasedSliders(d,e,i)) return true;
        return super.mouseReleased(d, e, i);
    }
    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        if (mouseDraggedSliders(d,e,i)) return true;
        return super.mouseDragged(d, e, i, f, g);
    }
}
