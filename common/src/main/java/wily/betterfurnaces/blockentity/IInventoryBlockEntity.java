package wily.betterfurnaces.blockentity;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import wily.factoryapi.base.FactoryItemSlot;
import wily.factoryapi.base.TransportState;

public interface IInventoryBlockEntity {

    Pair<int[], TransportState> getSlotsTransport(Direction side);

    boolean IcanExtractItem(int index, ItemStack stack);

    boolean IisItemValidForSlot(int index, ItemStack stack);

    default NonNullList<FactoryItemSlot> getSlots(@Nullable Player player){
        NonNullList<FactoryItemSlot> list = NonNullList.create();
        addSlots(list,player);
        return list;
    }
    void addSlots(NonNullList<FactoryItemSlot> slots, @Nullable Player player);

    default int getInventorySize(){
        return getSlots(null).size();
    }


}
