package wily.betterfurnaces.blockentity;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.init.ModObjects;

public interface FactoryUpgradeSettings {
    enum Type {
        SIDES("Settings",6),AUTO_IO("AutoIO", 2),REDSTONE("Redstone",2);
        public final String id;
        public final int size;
        Type(String id, int size){
            this.id = id;
            this.size = size;
        }

        public int[] emptySetting(){
            return new int[size];
        }
    }

    static FactoryUpgradeSettings of(ItemStack stack){
        return ()-> stack;
    }

    ItemStack getContainer();

    static boolean containsSettings(ItemStack container){
        for (Type type : Type.values()) {
            if (!containsSetting(type, container)) return false;
        }
        return true;
    }

    static boolean containsSetting(Type type, ItemStack container){
        //? if <1.20.5 {
        /*return container.getOrCreateTag().contains(type.id);
        *///?} else {
        return container.has(ModObjects.getSettingComponent(type).get());
        //?}
    }

    default void putBlankSettings(){
        // (mode 1, 2, 3, 4, subtract) ignored, low/high, comparator, comparator sub, subtract
        if (!containsSettings(getContainer())){
            for (Type value : Type.values()) {
                setFurnaceSetting(value, value == Type.SIDES ? getDefaultSides() : value.emptySetting());
            }
        }
    }

    default int get(Type type, int index) {
        if(getContainer().isEmpty() || !containsSetting(type, getContainer())) return 0;
        return getFurnaceSetting(type)[index];
    }

    default int getSides(int index){
        return get(Type.SIDES, index);
    }

    default int getAutoIO(int index){
        return get(Type.AUTO_IO, index);
    }

    default int getRedstone(int index){
        return get(Type.REDSTONE, index);
    }

    default void set(Type type, int index, int value) {
        if (getContainer().isEmpty()) return;
        int[] settings = getFurnaceSetting(type).clone();
        settings[index] = value;
        setFurnaceSetting(type, settings);
        onChanged();
    }

    default int size() {
        if(getContainer().isEmpty()) return 0;
        int size = 0;
        for (Type value : Type.values()) {
            size+=value.size;
        }
        return size;
    }

    default int[] getFurnaceSetting(Type type){
        if (!containsSetting(type, getContainer())) return new int[type.size];
        //? if <1.20.5 {
        /*return getContainer().getOrCreateTag().getIntArray(type.id);
        *///?} else {
        return getContainer().get(ModObjects.getSettingComponent(type).get());
        //?}
    }

    default void setFurnaceSetting(Type type, int[] settings){
        //? if <1.20.5 {
        /*getContainer().getOrCreateTag().putIntArray(type.id, settings);
        *///?} else {
        getContainer().set(ModObjects.getSettingComponent(type).get(), settings);
        //?}
    }

    default void onChanged() {
    }

    static int[] getDefaultSides() {
        int[] sides = Type.SIDES.emptySetting();
        for (Direction dir : Direction.values()) sides[dir.ordinal()] = 4;
        sides[0] = 2;
        sides[1] = 1;
        return sides;
    }
}