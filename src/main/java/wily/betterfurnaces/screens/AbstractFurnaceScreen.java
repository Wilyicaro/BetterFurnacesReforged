package wily.betterfurnaces.screens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.inventory.AbstractFurnaceContainer;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractFurnaceScreen<T extends AbstractFurnaceContainer> extends AbstractSmeltingScreen<T> {

    public AbstractFurnaceScreen(T t, PlayerInventory inv, ITextComponent name) {
        super(t, inv, name);
    }
}
