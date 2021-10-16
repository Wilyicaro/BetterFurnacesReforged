package wily.ultimatefurnaces.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.gui.BlockForgeScreenBase;
import wily.ultimatefurnaces.container.BlockCopperForgeContainer;
import wily.ultimatefurnaces.container.BlockDiamondForgeContainer;

@OnlyIn(Dist.CLIENT)
    public class BlockDiamondForgeScreen extends BlockForgeScreenBase<BlockDiamondForgeContainer> {


    public BlockDiamondForgeScreen(BlockDiamondForgeContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

}
