package wily.ultimatefurnaces.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.gui.BlockFurnaceScreenBase;
import wily.ultimatefurnaces.blocks.BlockAmethystFurnace;
import wily.ultimatefurnaces.container.BlockAmethystFurnaceContainer;
import wily.ultimatefurnaces.container.BlockCopperFurnaceContainer;

@OnlyIn(Dist.CLIENT)
public class BlockAmethystFurnaceScreen extends BlockFurnaceScreenBase<BlockAmethystFurnaceContainer> {


    public BlockAmethystFurnaceScreen(BlockAmethystFurnaceContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

}
