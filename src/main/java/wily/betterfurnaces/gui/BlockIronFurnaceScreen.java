package wily.betterfurnaces.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.container.BlockIronFurnaceContainer;

@OnlyIn(Dist.CLIENT)
public class BlockIronFurnaceScreen extends BlockFurnaceScreenBase<BlockIronFurnaceContainer> {


    public BlockIronFurnaceScreen(BlockIronFurnaceContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }
}
