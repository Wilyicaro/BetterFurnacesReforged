package wily.betterfurnaces.blockentity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.ArrayUtils;

public class FactoryUpgradeSettings {
    String Settings = "Settings";
    String AutoIO = "AutoIO";
    String Redstone = "Redstone";
    public ItemStack factory;

    public FactoryUpgradeSettings(ItemStack stack) {
        factory = stack;
        CompoundTag tag=  stack.getOrCreateTag();
        // (mode 1, 2, 3, 4, subtract) ignored, low/high, comparator, comparator sub, subtract
        if (!tag.contains(Settings) || !tag.contains(AutoIO) || !tag.contains(Redstone)){
            tag.putIntArray(Settings, new int[]{0, 0, 0, 0, 0, 0});
            tag.putIntArray(AutoIO, new int[]{0, 0});
            tag.putIntArray(Redstone, new int[]{0, 0});
        }

    }

    public int get(int index) {
        updateItem();
        if(factory.isEmpty()) return 0;
        int[] all = ArrayUtils.addAll(getFurnaceSetting(Settings),ArrayUtils.addAll(getFurnaceSetting(AutoIO),getFurnaceSetting(Redstone)));
        return all[index];
    }
    public<T> T byIndex(int index, T settings, T autoio, T redstone){
        return index < 6 ? settings : index < 8 ? autoio : redstone;
    }
    public void set(int index, int value) {
        updateItem();
        if(factory.isEmpty()) return;
        String actualSettings = byIndex(index,Settings,AutoIO,Redstone);
        int actualIndex = byIndex(index, index, index-6, index -8);
        int[] settings = getFurnaceSetting(actualSettings);
        settings[actualIndex] = value;
        setFurnaceSetting(actualSettings, settings);
        onChanged();

    }

    public int size() {
        if(factory.isEmpty()) return 0;
        return getFurnaceSetting(Settings).length + getFurnaceSetting(AutoIO).length + getFurnaceSetting(Redstone).length ;
    }

    public int[] getFurnaceSetting(String setting){
        return factory.getOrCreateTag().getIntArray(setting);
    }
    public void setFurnaceSetting(String setting,int[] settings){
        factory.getOrCreateTag().putIntArray(setting, settings);
    }
    public void updateItem() {

    }
    public void onChanged() {

    }
}