package wily.betterfurnaces.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;

import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.util.BFRComponents;
import wily.factoryapi.util.CompoundTagUtil;

import java.util.List;
import java.util.function.Consumer;

public class TierUpgradeItem extends UpgradeItem {
    public Block from;
    public Block to;

    public TierUpgradeItem(Properties properties, Block from, Block to) {
        super(properties, Type.TIER, Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.tier", from.getName(), to.getName()).setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
        this.from = from;
        this.to = to;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Consumer<Component> consumer, TooltipFlag flagIn) {
        consumer.accept(BFRComponents.UPGRADE_SHIFT_RIGHT_CLICK);
        consumer.accept(this.tooltip);
    }

    @Override
    public boolean isValid(SmeltingBlockEntity blockEntity) {
        return false;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        if (!level.isClientSide) {
            BlockEntity fromBe = level.getBlockEntity(pos);
            BlockPlaceContext ctx2 = new BlockPlaceContext(ctx);
            BlockState state = level.getBlockState(pos);
            if (state.is(from) && (fromBe instanceof FurnaceBlockEntity || fromBe instanceof SmeltingBlockEntity)) {
                CompoundTag tag = fromBe.saveWithoutMetadata(/*? if >=1.20.5 {*/ctx.getLevel().registryAccess()/*?}*/);
                level.removeBlockEntity(pos);
                level.removeBlock(pos,false);
                level.setBlock(pos,to.getStateForPlacement(ctx2).setValue(BlockStateProperties.LIT, state.getValue(BlockStateProperties.LIT)),3);
                SmeltingBlockEntity toBe = (SmeltingBlockEntity) level.getBlockEntity(pos);
                level.blockEntityChanged(pos);
                if (fromBe instanceof FurnaceBlockEntity)
                    toBe.inventory.deserializeTag(tag);
                tag.putInt("BurnTime", (int)(CompoundTagUtil.getInt(tag, "BurnTime").orElse(0) * (float) toBe.getDefaultCookTime() / Math.max(1, CompoundTagUtil.getInt(tag, "CookTimeTotal").orElse(0))));
                //? if <1.20.5 {
                /*toBe.load(toBe.saveWithoutMetadata().merge(tag));
                *///?} else {
                toBe.loadAdditional(toBe.saveWithoutMetadata(ctx.getLevel().registryAccess()).merge(tag),ctx.getLevel().registryAccess());
                //?}
                if (!ctx.getPlayer().isCreative())
                    ctx.getItemInHand().shrink(1);
                level.playSound(null, toBe.getBlockPos(), SoundEvents.ARMOR_EQUIP_NETHERITE/*? if >=1.20.5 {*/.value()/*?}*/, SoundSource.BLOCKS, 1.0F, 1.0F);

                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(ctx);
    }

}
