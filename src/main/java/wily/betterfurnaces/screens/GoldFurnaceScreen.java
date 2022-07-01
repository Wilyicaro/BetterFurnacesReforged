package wily.betterfurnaces.screens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.inventory.GoldFurnaceContainer;

@OnlyIn(Dist.CLIENT)
public class GoldFurnaceScreen extends AbstractFurnaceScreen<GoldFurnaceContainer> {


    public GoldFurnaceScreen(GoldFurnaceContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }
}
