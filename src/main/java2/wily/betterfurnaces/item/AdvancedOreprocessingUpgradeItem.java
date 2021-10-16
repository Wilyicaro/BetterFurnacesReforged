
package wily.betterfurnaces.item;

import net.minecraftforge.registries.ObjectHolder;

import net.minecraft.item.Rarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.block.BlockState;

import wily.betterfurnaces.itemgroup.BetterFurnacesReforgedItemGroup;
import wily.betterfurnaces.BetterfurnacesreforgedModElements;

@BetterfurnacesreforgedModElements.ModElement.Tag
public class AdvancedOreprocessingUpgradeItem extends BetterfurnacesreforgedModElements.ModElement {
	@ObjectHolder("betterfurnacesreforged:advanced_ore_processing_upgrade")
	public static final Item block = null;
	public AdvancedOreprocessingUpgradeItem(BetterfurnacesreforgedModElements instance) {
		super(instance, 14);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemCustom());
	}
	public static class ItemCustom extends Item {
		public ItemCustom() {
			super(new Item.Properties().group(BetterFurnacesReforgedItemGroup.tab).maxStackSize(1).rarity(Rarity.COMMON));
			setRegistryName("advanced_ore_processing_upgrade");
		}

		@Override
		public int getItemEnchantability() {
			return 0;
		}

		@Override
		public int getUseDuration(ItemStack itemstack) {
			return 0;
		}

		@Override
		public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
			return 1F;
		}
	}
}
