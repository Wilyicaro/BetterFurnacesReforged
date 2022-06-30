package wily.betterfurnaces.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wily.betterfurnaces.container.BlockFurnaceContainerBase;

@OnlyIn(Dist.CLIENT)
public abstract class BlockFurnaceScreenBase<T extends BlockFurnaceContainerBase> extends AbstractSmeltingScreen<T> {

    public BlockFurnaceScreenBase(T t, Inventory inv, Component name) {
        super(t, inv, name);
    }
}
