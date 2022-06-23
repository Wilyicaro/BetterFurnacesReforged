package wily.ultimatefurnaces.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.gui.BlockFurnaceScreenBase;
import wily.ultimatefurnaces.container.BlockCopperFurnaceContainer;
import wily.ultimatefurnaces.container.BlockPlatinumFurnaceContainer;

@OnlyIn(Dist.CLIENT)
public class BlockPlatinumFurnaceScreen extends BlockFurnaceScreenBase<BlockPlatinumFurnaceContainer> {


    public BlockPlatinumFurnaceScreen(BlockPlatinumFurnaceContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

}
