package wily.betterfurnaces.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.tileentity.BlockSmeltingTileBase;

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
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade_shift_right_click").setStyle(Style.EMPTY.applyFormat(TextFormatting.GOLD).withItalic(true)));
        tooltip.add(new StringTextComponent(I18n.get("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.tier", from.getName().getString(), to.getName().getString())).setStyle(Style.EMPTY.applyFormat(TextFormatting.GRAY)));
    }

    @Override
    public ActionResultType useOn(ItemUseContext ctx) {
        World world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        if (!world.isClientSide) {
            if ((ctx.getItemInHand().getItem() instanceof ItemUpgradeIron) && ModList.get().isLoaded("fastfurnace"))
            {
                ctx.getPlayer().sendMessage(new StringTextComponent("FastFurnace Mod is loaded, will not upgrade, drop the upgrade on the floor together with one cobblestone to get your materials back."), ctx.getPlayer().getUUID());
                return super.useOn(ctx);
            }
            TileEntity te = world.getBlockEntity(pos);
            BlockItemUseContext ctx2 = new BlockItemUseContext(ctx);
            if (te instanceof FurnaceTileEntity || te instanceof BlockSmeltingTileBase) {
                int cooktime = te.serializeNBT().getInt("CookTime");
                int cooktimetotal = te.serializeNBT().getInt("CookTimeTotal");
                int currentItemBurnTime = 0;
                int furnaceBurnTime = te.serializeNBT().getInt("BurnTime");
                int show = 0;
                int[] settings = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                if (te instanceof BlockSmeltingTileBase) {
                    furnaceBurnTime = ((BlockSmeltingTileBase) te).fields.get(0);
                    currentItemBurnTime = ((BlockSmeltingTileBase) te).fields.get(1);
                    cooktime = ((BlockSmeltingTileBase) te).fields.get(2);
                    show = ((BlockSmeltingTileBase) te).fields.get(4);
                    for (int i = 0; i < ((BlockSmeltingTileBase) te).furnaceSettings.size(); i++)
                    {
                        settings[i] = ((BlockSmeltingTileBase) te).furnaceSettings.get(i);
                    }

                }
                if (te.getBlockState().getBlock() != from)
                {
                    return ActionResultType.PASS;
                }
                BlockState next = to.getStateForPlacement(ctx2) != Blocks.AIR.getStateForPlacement(ctx2) ? to.getStateForPlacement(ctx2) : world.getBlockState(pos);
                if (next == world.getBlockState(pos)) {
                    return ActionResultType.PASS;
                }
                ItemStack input = ((IInventory) te).getItem(0).copy();
                ItemStack fuel  = ((IInventory) te).getItem(1).copy();
                ItemStack output  = ((IInventory) te).getItem(2).copy();
                ItemStack upgrade  = ItemStack.EMPTY;
                ItemStack upgrade1  = ItemStack.EMPTY;
                ItemStack upgrade2  = ItemStack.EMPTY;
                if (te instanceof BlockSmeltingTileBase) {
                    upgrade = ((IInventory) te).getItem(3).copy();
                    upgrade1 = ((IInventory) te).getItem(4).copy();
                    upgrade2 = ((IInventory) te).getItem(5).copy();
                }
                world.removeBlockEntity(te.getBlockPos());
                world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                world.setBlock(pos, next.setValue(BlockStateProperties.LIT, te.getBlockState().getValue(BlockStateProperties.LIT)), 3);
                world.playSound(null, te.getBlockPos(), SoundEvents.ARMOR_EQUIP_NETHERITE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                TileEntity newTe = world.getBlockEntity(pos);
                newTe.deserializeNBT(te.serializeNBT());
                ((IInventory)newTe).setItem(0, input);
                ((IInventory)newTe).setItem(1, fuel);
                ((IInventory)newTe).setItem(2, output);
                if (newTe instanceof BlockSmeltingTileBase) {
                    ((IInventory)newTe).setItem(3, upgrade);
                    ((IInventory)newTe).setItem(4, upgrade1);
                    ((IInventory)newTe).setItem(5, upgrade2);
                    ((BlockSmeltingTileBase)newTe).fields.set(0, furnaceBurnTime * ((BlockSmeltingTileBase) newTe).getCookTime() / cooktimetotal);
                    ((BlockSmeltingTileBase)newTe).fields.set(1, currentItemBurnTime);
                    ((BlockSmeltingTileBase)newTe).fields.set(2, cooktime);
                    ((BlockSmeltingTileBase)newTe).fields.set(4, show);
                    for (int i = 0; i < ((BlockSmeltingTileBase)newTe).furnaceSettings.size(); i++)
                    {
                        ((BlockSmeltingTileBase)newTe).furnaceSettings.set(i, settings[i]);
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

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (!ModList.get().isLoaded("fastfurnace"))
        {
            return false;
        }
        ItemStack materials = ItemStack.EMPTY;
        if (!(stack.getItem() instanceof ItemUpgradeIron))
        {
            return false;
        }
        else
        {
            if (stack.getItem() instanceof ItemUpgradeIron) {
                materials = new ItemStack(Items.IRON_INGOT, 8);
            }
        }
        World world = entity.level;
        if (!world.isClientSide) {
            List<ItemEntity> list = world.getEntities(EntityType.ITEM,
                    new AxisAlignedBB(entity.position().x - 0.5, entity.position().y - 0.5, entity.position().z - 0.5, entity.position().x + 0.5, entity.position().y + 0.5, entity.position().z + 0.5),
                    new UpgradeItems());

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getItem().equals(new ItemStack(Blocks.COBBLESTONE, 1), false)) {
                    one = true;
                }
            }
            if (one) {

                BlockPos pos = new BlockPos(entity.position().x, entity.position().y, entity.position().z);
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).remove();
                }
                LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
                lightningboltentity.moveTo(pos.getX(), pos.getY(), pos.getZ());
                lightningboltentity.setVisualOnly(true);
                world.addFreshEntity(lightningboltentity);
                world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), materials));

                one = false;
            }
        }
        return false;
    }
}
