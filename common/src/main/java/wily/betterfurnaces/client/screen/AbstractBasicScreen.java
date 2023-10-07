package wily.betterfurnaces.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import wily.betterfurnaces.inventory.AbstractInventoryMenu;
import wily.factoryapi.base.client.IWindowWidget;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBasicScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements IWindowWidget {

    public AbstractBasicScreen(T p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }
    protected final List<Renderable> nestedRenderables = new ArrayList<>();

    @Override
    protected void init() {
        nestedRenderables.clear();
        super.init();
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        if (menu instanceof AbstractInventoryMenu<?> ) {
            ((AbstractInventoryMenu<?>) menu).be.syncAdditionalMenuData(menu, ((AbstractInventoryMenu<?>) menu).player);
        }
        super.renderBackground(poseStack);
        super.render(poseStack, i, j, f);
        super.renderTooltip(poseStack,i,j);
        IWindowWidget.super.render(poseStack,i,j,f);
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        if (IWindowWidget.super.mouseClicked(d,e,i)) return true;
        return super.mouseClicked(d, e, i);
    }
    @Override
    public boolean mouseReleased(double d, double e, int i) {
        if (IWindowWidget.super.mouseReleased(d,e,i)) return true;
        return super.mouseReleased(d, e, i);
    }

    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        if (IWindowWidget.super.mouseDragged(d,e,i,f,g)) return true;
        return super.mouseDragged(d, e, i, f, g);
    }

    @Override
    public Rect2i getBounds() {
        return new Rect2i(leftPos,topPos,width,height);
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public <R extends Renderable> R addNestedRenderable(R renderable) {
        nestedRenderables.add(renderable);
        return renderable;
    }

    @Override
    public List<? extends Renderable> getNestedRenderables() {
        return nestedRenderables;
    }

}
