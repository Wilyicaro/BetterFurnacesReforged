package wily.betterfurnaces.blockentity;

import com.mojang.datafixers.util.Pair;
import me.shedaniel.architectury.extensions.BlockEntityExtension;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import wily.factoryapi.base.TransportState;

public interface IInventoryBlockEntity extends BlockEntityExtension {

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
