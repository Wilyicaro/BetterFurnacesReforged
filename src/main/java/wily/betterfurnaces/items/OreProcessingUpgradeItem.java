package wily.betterfurnaces.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.BetterFurnacesReforged;

import java.util.List;
import java.util.function.Consumer;

public class OreProcessingUpgradeItem extends UpgradeItem {

    public final int multiplier;
    public final boolean acceptOre;
    public final boolean acceptRaw;
    public OreProcessingUpgradeItem(Properties properties, int multiplier, boolean acceptOre, boolean acceptRaw) {
        super(properties, Type.ORE);
        this.multiplier = multiplier;
        this.acceptOre = acceptOre;
        this.acceptRaw = acceptRaw;
    }

    @Override
    public boolean isUpgradeCompatibleWith(UpgradeItem upg) {
        return upg.upgradeType != Type.MODE;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Consumer<Component> consumer, TooltipFlag flagIn) {
        super.appendHoverText(itemStack, consumer, flagIn);
        String s;
        String i;
        String o;
        if (!itemStack.isDamageableItem())
            i = Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.infinitely").getString();
        else i = "";
        if (acceptOre && !acceptRaw) o = Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.ore").getString();
        else o = Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.raw").getString();
        if (acceptRaw && acceptOre) o = Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.allore").getString();
        if (multiplier == 2) s = Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.double").getString();
        else if (multiplier == 4) s = Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.quadruple").getString();
        else if (multiplier > 4) s = Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.multiple").getString();
        else s = "";
        consumer.accept(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.ores",s,i,o).setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
    }
}
