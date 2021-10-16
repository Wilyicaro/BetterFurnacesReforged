
package wily.betterfurnaces.itemgroup;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;

import wily.betterfurnaces.block.ExtremeFurnaceBlock;
import wily.betterfurnaces.BetterfurnacesreforgedModElements;

@BetterfurnacesreforgedModElements.ModElement.Tag
public class BetterFurnacesReforgedItemGroup extends BetterfurnacesreforgedModElements.ModElement {
	public BetterFurnacesReforgedItemGroup(BetterfurnacesreforgedModElements instance) {
		super(instance, 34);
	}

	@Override
	public void initElements() {
		tab = new ItemGroup("tabbetter_furnaces_reforged") {
			@OnlyIn(Dist.CLIENT)
			@Override
			public ItemStack createIcon() {
				return new ItemStack(ExtremeFurnaceBlock.block, (int) (1));
			}

			@OnlyIn(Dist.CLIENT)
			public boolean hasSearchBar() {
				return false;
			}
		};
	}
	public static ItemGroup tab;
}
