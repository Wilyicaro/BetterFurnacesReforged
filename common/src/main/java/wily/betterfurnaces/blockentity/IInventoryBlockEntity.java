package wily.betterfurnaces.blockentity;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import wily.factoryapi.base.TransportState;

public interface IInventoryBlockEntity {

    Pair<int[], TransportState> getSlotsTransport(Direction side);

    boolean IcanExtractItem(int index, ItemStack stack);

    boolean IisItemValidForSlot(int index, ItemStack stack);

    default NonNullList<Slot> getSlots(@Nullable Player player){
        NonNullList<Slot> list = NonNullList.create();
        addSlots(list,player);
        return list;
    }
    void addSlots(NonNullList<Slot> slots, @Nullable Player player);

    default int getInventorySize(){
        return getSlots(null).size();
    }


}
