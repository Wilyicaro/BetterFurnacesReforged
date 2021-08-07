package wily.betterfurnaces.upgrade;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

/**
 * Represents an Upgrade for an Iron Furnace.
 * @author Shadows
 *
 */
public class Upgrade {

	protected final Ingredient items;

	public Upgrade(Ingredient items) {
		this.items = items;
	}

	public boolean matches(ItemStack stack) {
		return items.apply(stack);
	}

}
