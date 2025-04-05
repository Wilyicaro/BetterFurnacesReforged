package wily.betterfurnaces.items;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import wily.betterfurnaces.blockentity.FactoryUpgradeSettings;

public class FactoryUpgradeItem extends UpgradeItem {
    public boolean canOutput;
    public boolean canInput;
    public boolean pipeSide;
    public boolean redstoneSignal;

    public FactoryUpgradeItem(Properties properties, String tooltip, boolean output, boolean input, boolean pipe, boolean redstone) {
        super(properties, Type.FACTORY, tooltip);
        canOutput = output;
        canInput = input;
        pipeSide = pipe;
        redstoneSignal = redstone;
    }

    @Override
    public void inventoryTick(ItemStack stack, /*? if <1.21.5 {*//*Level world, Entity player, int slot, boolean selected*//*?} else {*/ServerLevel world, Entity player, EquipmentSlot equipmentSlot/*?}*/) {
        super.inventoryTick(stack, /*? if <1.21.5 {*//*world,  player, slot, selected*//*?} else {*/world, player, equipmentSlot/*?}*/);
        if (!FactoryUpgradeSettings.containsSettings(stack)) {
            FactoryUpgradeSettings.of(stack).putBlankSettings();
        }
    }

}
