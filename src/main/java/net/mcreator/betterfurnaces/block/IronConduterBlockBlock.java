
package net.mcreator.betterfurnaces.block;

import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.common.ToolType;

import net.minecraft.loot.LootContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraft.block.material.Material;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;

import net.mcreator.betterfurnaces.itemgroup.BetterFurnacesReforgedItemGroup;
import net.mcreator.betterfurnaces.BetterfurnacesreforgedModElements;

import java.util.List;
import java.util.Collections;

@BetterfurnacesreforgedModElements.ModElement.Tag
public class IronConduterBlockBlock extends BetterfurnacesreforgedModElements.ModElement {
	@ObjectHolder("betterfurnacesreforged:ironconduterblock")
	public static final Block block = null;
	public IronConduterBlockBlock(BetterfurnacesreforgedModElements instance) {
		super(instance, 8);
	}

	@Override
	public void initElements() {
		elements.blocks.add(() -> new CustomBlock());
		elements.items.add(() -> new BlockItem(block, new Item.Properties().group(BetterFurnacesReforgedItemGroup.tab))
				.setRegistryName(block.getRegistryName()));
	}
	public static class CustomBlock extends Block {
		public CustomBlock() {
			super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2f, 10f).setLightLevel(s -> 0).harvestLevel(1)
					.harvestTool(ToolType.PICKAXE).setRequiresTool());
			setRegistryName("ironconduterblock");
		}

		@Override
		public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
			List<ItemStack> dropsOriginal = super.getDrops(state, builder);
			if (!dropsOriginal.isEmpty())
				return dropsOriginal;
			return Collections.singletonList(new ItemStack(this, 1));
		}
	}
}
