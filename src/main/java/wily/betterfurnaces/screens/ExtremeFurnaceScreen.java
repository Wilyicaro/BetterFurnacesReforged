package wily.betterfurnaces.screens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.inventory.ExtremeFurnaceContainer;

@OnlyIn(Dist.CLIENT)
public class ExtremeFurnaceScreen extends AbstractFurnaceScreen<ExtremeFurnaceContainer> {


    public ExtremeFurnaceScreen(ExtremeFurnaceContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }
}
