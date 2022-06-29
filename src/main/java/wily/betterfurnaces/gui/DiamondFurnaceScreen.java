package wily.betterfurnaces.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.container.DiamondFurnaceContainer;

@OnlyIn(Dist.CLIENT)
public class DiamondFurnaceScreen extends BlockFurnaceScreenBase<DiamondFurnaceContainer> {


    public DiamondFurnaceScreen(DiamondFurnaceContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }
}
