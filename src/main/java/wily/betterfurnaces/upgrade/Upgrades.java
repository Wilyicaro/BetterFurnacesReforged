package wily.betterfurnaces.upgrade;

import wily.betterfurnaces.init.ModObjects;
import net.minecraft.item.crafting.Ingredient;

/**
 * Holder class for Upgrades.
 * @author Shadows
 *
 */
public class Upgrades {

	public static final Upgrade ELECTRIC_FUEL = new Upgrade(Ingredient.fromItem(ModObjects.ENERGY_UPGRADE));
	public static final Upgrade EFFICIENCY = new Upgrade(Ingredient.fromItem(ModObjects.FUEL_EFFICIENCY_UPGRADE));
	public static final Upgrade COLOR = new Upgrade(Ingredient.fromItem(ModObjects.COLOR_UPGRADE));
	public static final Upgrade ORE_PROCESSING = new Upgrade(Ingredient.fromItem(ModObjects.ORE_PROCESSING_UPGRADE));
	public static final Upgrade ADVORE_PROCESSING = new Upgrade(Ingredient.fromItem(ModObjects.ADVANCED_ORE_PROCESSING_UPGRADE));
	public static final Upgrade ADVEFFICIENCY = new Upgrade(Ingredient.fromItem(ModObjects.ADVANCED_FUEL_EFFICIENCY_UPGRADE));
	public static final Upgrade LIQUID_FUEL = new Upgrade(Ingredient.fromItem(ModObjects.LIQUID_FUEL_UPGRADE));

}
