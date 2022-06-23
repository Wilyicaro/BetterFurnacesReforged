package wily.ultimatefurnaces.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.gui.BlockFurnaceScreenBase;
import wily.ultimatefurnaces.container.BlockCopperFurnaceContainer;
import wily.ultimatefurnaces.container.BlockSteelFurnaceContainer;

@OnlyIn(Dist.CLIENT)
public class BlockSteelFurnaceScreen extends BlockFurnaceScreenBase<BlockSteelFurnaceContainer> {


    public BlockSteelFurnaceScreen(BlockSteelFurnaceContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

}
