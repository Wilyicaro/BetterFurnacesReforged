package wily.ultimatefurnaces.screens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.screens.AbstractFurnaceScreen;
import wily.betterfurnaces.screens.AbstractSmeltingScreen;
import wily.ultimatefurnaces.inventory.AmethystFurnaceContainer;

@OnlyIn(Dist.CLIENT)
public class AmethystFurnaceScreen extends AbstractSmeltingScreen<AmethystFurnaceContainer> {


    public AmethystFurnaceScreen(AmethystFurnaceContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

}
