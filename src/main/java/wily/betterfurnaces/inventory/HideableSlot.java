package wily.betterfurnaces.inventory;

import wily.betterfurnaces.blockentity.InventoryBlockEntity;
import wily.factoryapi.base.FactoryItemSlot;
import wily.factoryapi.base.SlotsIdentifier;
import wily.factoryapi.base.TransportState;

import java.util.function.Predicate;

public class HideableSlot extends FactoryItemSlot {
    protected InventoryBlockEntity be;
    public HideableSlot(InventoryBlockEntity be, int index, int x, int y) {
        this(be, index, x, y, (s)->true);
    }
    public HideableSlot(InventoryBlockEntity be, int index, int x, int y, Predicate<FactoryItemSlot> isActive) {
        super(be.inventory, SlotsIdentifier.GENERIC, TransportState.EXTRACT_INSERT, index, x, y);
        this.be = be;
        this.active = isActive;
    }

}
