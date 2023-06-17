package wily.betterfurnaces.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import wily.betterfurnaces.inventory.AbstractInventoryMenu;

public abstract class AbstractBasicScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    protected int relX() {return (this.width - this.imageWidth) / 2;
}
    protected int relY()  {return  (this.height - this.imageHeight) / 2;}
    public AbstractBasicScreen(T p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    @Override
    public void render(GuiGraphics graphics, int i, int j, float f) {
        if (getMenu()instanceof AbstractInventoryMenu<?> menu) menu.be.syncAdditionalMenuData(menu, menu.player);
        renderBackground(graphics);
        super.render(graphics, i, j, f);
        renderTooltip(graphics,i,j);
    }
}
