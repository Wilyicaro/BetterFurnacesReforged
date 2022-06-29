package wily.betterfurnaces.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.container.IronFurnaceContainer;

@OnlyIn(Dist.CLIENT)
public class IronFurnaceScreen extends BlockFurnaceScreenBase<IronFurnaceContainer> {


    public IronFurnaceScreen(IronFurnaceContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }
}
