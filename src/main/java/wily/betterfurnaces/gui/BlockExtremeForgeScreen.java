package wily.betterfurnaces.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.container.BlockExtremeForgeContainer;

@OnlyIn(Dist.CLIENT)
public class BlockExtremeForgeScreen extends BlockForgeScreenBase<BlockExtremeForgeContainer> {


    public BlockExtremeForgeScreen(BlockExtremeForgeContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }
}
