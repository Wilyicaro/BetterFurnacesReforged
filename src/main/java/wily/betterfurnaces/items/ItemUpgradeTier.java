package wily.betterfurnaces.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.tileentity.BlockFurnaceTileBase;

import javax.annotation.Nullable;
import java.util.List;

public class ItemUpgradeTier extends Item {

    public Block from;
    public Block to;
    boolean one = false;

    public ItemUpgradeTier(Properties properties, Block from, Block to) {
        super(properties);
        this.from = from;
        this.to = to;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade_shift_right_click").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GOLD).withItalic(true)));
        tooltip.add(new TextComponent(I18n.get("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.tier", from.getName().getString(), to.getName().getString())).setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        if (!world.isClientSide) {
            if ((ctx.getItemInHand().getItem() instanceof ItemUpgradeIron) && ModList.get().isLoaded("fastfurnace"))
            {
                ctx.getPlayer().sendMessage(new TextComponent("FastFurnace Mod is loaded, will not upgrade, drop the upgrade on the floor together with one cobblestone to get your materials back."), ctx.getPlayer().getUUID());
                return super.useOn(ctx);
            }
            BlockEntity te = world.getBlockEntity(pos);
            BlockPlaceContext ctx2 = new BlockPlaceContext(ctx);
            if (te instanceof FurnaceBlockEntity || te instanceof BlockFurnaceTileBase) {
                int cooktime = 0;
                int currentItemBurnTime = 0;
                int furnaceBurnTime = 0;
                int show = 0;
                int[] settings = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                if (te instanceof BlockFurnaceTileBase) {
                    furnaceBurnTime = ((BlockFurnaceTileBase) te).fields.get(0);
                    currentItemBurnTime = ((BlockFurnaceTileBase) te).fields.get(1);
                    cooktime = ((BlockFurnaceTileBase) te).fields.get(2);
                    show = ((BlockFurnaceTileBase) te).fields.get(4);
                    for (int i = 0; i < ((BlockFurnaceTileBase) te).furnaceSettings.size(); i++)
                    {
                        settings[i] = ((BlockFurnaceTileBase) te).furnaceSettings.get(i);
                    }

                }
                if (te.getBlockState().getBlock() != from)
                {
                    return InteractionResult.PASS;
                }
                BlockState next = to.getStateForPlacement(ctx2) != Blocks.AIR.getStateForPlacement(ctx2) ? to.getStateForPlacement(ctx2) : world.getBlockState(pos);
                if (next == world.getBlockState(pos)) {
                    return InteractionResult.PASS;
                }
                ItemStack input = ((Container) te).getItem(0).copy();
                ItemStack fuel  = ((Container) te).getItem(1).copy();
                ItemStack output  = ((Container) te).getItem(2).copy();
                ItemStack upgrade  = ItemStack.EMPTY;
                if (te instanceof BlockFurnaceTileBase) {
                    upgrade = ((Container) te).getItem(3).copy();
                }
                world.removeBlockEntity(te.getBlockPos());
                world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                world.setBlock(pos, next, 3);
                world.playSound(null, te.getBlockPos(), SoundEvents.ARMOR_EQUIP_NETHERITE, SoundSource.BLOCKS, 1.0F, 1.0F);
                BlockEntity newTe = world.getBlockEntity(pos);
                ((Container)newTe).setItem(0, input);
                ((Container)newTe).setItem(1, fuel);
                ((Container)newTe).setItem(2, output);
                if (newTe instanceof BlockFurnaceTileBase) {
                    ((Container)newTe).setItem(3, upgrade);
                    ((BlockFurnaceTileBase)newTe).fields.set(0, furnaceBurnTime);
                    ((BlockFurnaceTileBase)newTe).fields.set(1, currentItemBurnTime);
                    ((BlockFurnaceTileBase)newTe).fields.set(2, cooktime);
                    ((BlockFurnaceTileBase)newTe).fields.set(4, show);
                    for (int i = 0; i < ((BlockFurnaceTileBase)newTe).furnaceSettings.size(); i++)
                    {
                        ((BlockFurnaceTileBase)newTe).furnaceSettings.set(i, settings[i]);
                    }
                }
                world.markAndNotifyBlock(pos, world.getChunkAt(pos), world.getBlockState(pos).getBlock().defaultBlockState(), world.getBlockState(pos),3,  3);
                if (!ctx.getPlayer().isCreative()) {
                    ctx.getItemInHand().shrink(1);
                }
            }
        }
        return super.useOn(ctx);
    }
}
