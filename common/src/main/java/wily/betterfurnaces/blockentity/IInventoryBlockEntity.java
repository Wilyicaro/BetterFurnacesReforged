package wily.betterfurnaces.blockentity;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import wily.factoryapi.base.TransportState;

public interface IInventoryBlockEntity {

    public Pair<int[], TransportState> getSlotsTransport(Direction side);

    public boolean IcanExtractItem(int index, ItemStack stack);

    public Component IgetName();

    public boolean IisItemValidForSlot(int index, ItemStack stack);

    public AbstractContainerMenu IcreateMenu(int i, Inventory playerInventory, Player playerEntity);


}
