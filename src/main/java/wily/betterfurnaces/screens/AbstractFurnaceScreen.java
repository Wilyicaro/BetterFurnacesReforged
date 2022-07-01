package wily.betterfurnaces.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.inventory.AbstractFurnaceMenu;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractFurnaceScreen<T extends AbstractFurnaceMenu> extends AbstractSmeltingScreen<T> {

    public AbstractFurnaceScreen(T t, Inventory inv, Component name) {
        super(t, inv, name);
    }
}
