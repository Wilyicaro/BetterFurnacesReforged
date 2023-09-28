package wily.betterfurnaces.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import wily.factoryapi.base.client.IWindowWidget;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBasicScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements IWindowWidget {

    protected final List<Renderable> renderables = new ArrayList<>();
    public AbstractBasicScreen(T p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    @Override
    protected void init() {
        super.init();
        renderables.clear();
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
        IWindowWidget.super.render(graphics,i,j,f);
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        if (IWindowWidget.super.mouseClicked(d,e,i)) return true;
        return super.mouseClicked(d, e, i);
    }

    @Override
    public boolean mouseReleased(double d, double e, int i) {
        this.setDragging(false);
        if (IWindowWidget.super.mouseReleased(d,e,i)) return true;
        return super.mouseReleased(d, e, i);
    }
    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        if (IWindowWidget.super.mouseDragged(d,e,i,f,g)) return true;
        return super.mouseDragged(d, e, i, f, g);
    }

    @Override
    public <R extends Renderable> R addNestedRenderable(R drawable) {
        renderables.add(drawable);
        return drawable;
    }

    @Override
    public List<? extends Renderable> getNestedRenderables() {
        return renderables;
    }
}
