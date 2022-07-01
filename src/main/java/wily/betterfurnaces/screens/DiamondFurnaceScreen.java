package wily.betterfurnaces.screens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.inventory.DiamondFurnaceContainer;

@OnlyIn(Dist.CLIENT)
public class DiamondFurnaceScreen extends AbstractFurnaceScreen<DiamondFurnaceContainer> {


    public DiamondFurnaceScreen(DiamondFurnaceContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }
}
