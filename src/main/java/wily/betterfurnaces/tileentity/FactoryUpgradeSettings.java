package wily.betterfurnaces.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import org.apache.commons.lang3.ArrayUtils;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FactoryUpgradeSettings {
    public static String Settings = "Settings";
    public static String AutoIO = "AutoIO";
    public static String Redstone = "Redstone";
    public Supplier<ItemStack> factory;

    public FactoryUpgradeSettings(Supplier<ItemStack> stack) {
        factory = stack;
        // (mode 1, 2, 3, 4, subtract) ignored, low/high, comparator, comparator sub, subtract
        if  (!containsAnyTag(factory.get())){
            CompoundNBT tag=  factory.get().getOrCreateTag();
            tag.putIntArray(Settings, new int[]{0, 0, 0, 0, 0, 0});
            tag.putIntArray(AutoIO, new int[]{0, 0});
            tag.putIntArray(Redstone, new int[]{0, 0});
            placeDefaultConfig();
            stack.get().setTag(tag);
        }


    }
    public static boolean containsAnyTag(ItemStack factory){
        CompoundNBT tag=  factory.getOrCreateTag();
        return  (tag.contains(Settings) && tag.contains(AutoIO) && tag.contains(Redstone));
    }

    public int get(int index) {

        if(factory.get().isEmpty() || !containsAnyTag(factory.get())) return 0;
        int[] all = ArrayUtils.addAll(getFurnaceSetting(Settings),ArrayUtils.addAll(getFurnaceSetting(AutoIO),getFurnaceSetting(Redstone)));
        return all[index];
    }
    public<T> T byIndex(int index, T settings, T autoio, T redstone){
        return index < 6 ? settings : index < 8 ? autoio : redstone;
    }
    public void set(int index, int value) {

        if(factory.get().isEmpty()) return;
        String actualSettings = byIndex(index,Settings,AutoIO,Redstone);
        int actualIndex = byIndex(index, index, index-6, index -8);
        int[] settings = getFurnaceSetting(actualSettings);
        settings[actualIndex] = value;
        setFurnaceSetting(actualSettings, settings);
        onChanged();

    }

    public int size() {
        if(factory.get().isEmpty()) return 0;
        return getFurnaceSetting(Settings).length + getFurnaceSetting(AutoIO).length + getFurnaceSetting(Redstone).length ;
    }

    public int[] getFurnaceSetting(String setting){
        return factory.get().getOrCreateTag().getIntArray(setting);
    }
    public void setFurnaceSetting(String setting,int[] settings){
        factory.get().getOrCreateTag().putIntArray(setting, settings);
    }

    public void onChanged() {

    }

    public void placeDefaultConfig() {
        for (Direction dir : Direction.values()) set(dir.ordinal(), 4);
        set(0, 2);
        set(1, 1);

    }
}