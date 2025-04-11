package wily.betterfurnaces.blockentity;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import wily.factoryapi.base.FactoryItemSlot;
import wily.factoryapi.base.TransportState;

import java.util.function.Consumer;

public interface IInventoryBlockEntity {

    Pair<int[], TransportState> getSlotsTransport(Direction side);

    boolean IcanExtractItem(int index, ItemStack stack);

    NonNullList<FactoryItemSlot> getSlots();

    default NonNullList<FactoryItemSlot> createSlots(@Nullable Player player){
        NonNullList<FactoryItemSlot> list = NonNullList.create();
        addSlots(list::add,player);
        return list;
    }

    void addSlots(Consumer<FactoryItemSlot> slots, @Nullable Player player);

    default int getInventorySize(){
        return getSlots().size();
    }

}
