package wily.betterfurnaces.items;

import dev.architectury.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;

import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class TierUpgradeItem extends Item {

    public Block from;
    public Block to;

    public TierUpgradeItem(Properties properties, Block from, Block to) {
        super(properties);
        this.from = from;
        this.to = to;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade_shift_right_click").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GOLD).withItalic(true)));
        tooltip.add(Component.literal(I18n.get("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.tier", from.getName().getString(), to.getName().getString())).setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
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
                CompoundTag tag =  fromBe.saveWithoutMetadata();
                level.removeBlockEntity(pos);
                level.removeBlock(pos,false);
                level.setBlock(pos,to.getStateForPlacement(ctx2).setValue(BlockStateProperties.LIT, state.getValue(BlockStateProperties.LIT)),3);
                SmeltingBlockEntity toBe = (SmeltingBlockEntity) level.getBlockEntity(pos);
                level.blockEntityChanged(pos);
                if (fromBe instanceof FurnaceBlockEntity){
                    toBe.inventory.deserializeTag(tag);
                }
                toBe.load(toBe.saveWithoutMetadata().merge(tag));
                if (!ctx.getPlayer().isCreative())
                    ctx.getItemInHand().shrink(1);
                level.playSound(null, toBe.getBlockPos(), SoundEvents.ARMOR_EQUIP_NETHERITE, SoundSource.BLOCKS, 1.0F, 1.0F);

                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(ctx);
    }

}
