package wily.betterfurnaces.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.BetterFurnacesReforged;

import java.util.List;
import java.util.function.Consumer;

public class FuelEfficiencyUpgradeItem extends UpgradeItem {
    public int getMultiplier;
    public FuelEfficiencyUpgradeItem(Properties properties, int Multiplier) {
        super(properties,Type.FUEL);
        this.getMultiplier = Multiplier;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Consumer<Component> consumer, TooltipFlag flagIn) {
        super.appendHoverText(itemStack, consumer, flagIn);
        String s;
        String i;
        if (!itemStack.isDamageableItem())
            i = Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.infinitely").getString();
        else i = "";
        if (getMultiplier == 2) s = Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.double").getString();
        else if (getMultiplier == 4) s = Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.quadruple").getString();
        else if (getMultiplier > 4) s = Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.multiple").getString();
        else s = "";
        consumer.accept(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.fuel",s,i).setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
    }

}
