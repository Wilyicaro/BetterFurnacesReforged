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
//? if >=1.21.5 {
import net.minecraft.world.item.component.TooltipDisplay;
//?}
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.util.BFRComponents;

import java.util.List;
import java.util.function.Consumer;

public class UpgradeItem extends Item {
    protected final Component tooltip;
    public Type upgradeType;

    public UpgradeItem(Properties properties, Type upgradeType, Component tooltip) {
        super(properties);
        this.upgradeType = upgradeType;
        this.tooltip = tooltip;

    }

    public UpgradeItem(Properties properties, Type upgradeType, String tooltipName) {
        this(properties, upgradeType, Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade." + tooltipName).setStyle(Style.EMPTY.applyFormat((ChatFormatting.GRAY))));
    }

    public UpgradeItem(Properties properties, Type upgradeType) {
        this(properties, upgradeType, (Component) null);
    }

    public enum Type {
        FUEL,ORE,COLOR,ALTERNATIVE_FUEL,XP,MODE,FACTORY,STORAGE,TIER,MISC
    }

    public boolean isEnabled(){
        return true;
    }

    public boolean isSameType(UpgradeItem upg){
        return upgradeType == upg.upgradeType;
    }

    public boolean isValid(SmeltingBlockEntity blockEntity){
        return isEnabled() && blockEntity.getUpgrades().stream().allMatch(this::isUpgradeCompatibleWith);
    }

    public boolean isUpgradeCompatibleWith(UpgradeItem upg){
        return true;
    }

    public Component getDisabledMessage(){
        return BFRComponents.UPGRADE_DISABLED_MESSAGE;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, /*? if >=1.20.5 {*/Item.TooltipContext tooltipContext/*?} else {*//*@Nullable Level level*//*?}*/, /*? if >=1.21.5 {*/TooltipDisplay tooltipDisplay, Consumer<Component> tooltip/*?} else {*//*List<Component> tooltip*//*?}*/, TooltipFlag tooltipFlag) {
        appendHoverText(itemStack, tooltip/*? if <1.21.5 {*//*::add*//*?}*/, tooltipFlag);
    }

    public void appendHoverText(ItemStack itemStack, Consumer<Component> consumer, TooltipFlag flagIn) {
        if (isEnabled()) consumer.accept(BFRComponents.UPGRADE_RIGHT_CLICK);
        else consumer.accept(getDisabledMessage());
        if (this.tooltip != null)
            consumer.accept(this.tooltip);

    }

    @Override
    public void inventoryTick(ItemStack stack,/*? if <1.21.5 {*//*Level world, Entity player, int slot, boolean selected*//*?} else {*/ServerLevel world, Entity player, EquipmentSlot equipmentSlot/*?}*/) {
        if (stack.isDamageableItem() && stack.getDamageValue() >= stack.getMaxDamage()){
            stack.shrink(1);
        }
    }

}
