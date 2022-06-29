package wily.betterfurnaces.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.container.GoldFurnaceContainer;

@OnlyIn(Dist.CLIENT)
public class GoldFurnaceScreen extends BlockFurnaceScreenBase<GoldFurnaceContainer> {


    public GoldFurnaceScreen(GoldFurnaceContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }
}
