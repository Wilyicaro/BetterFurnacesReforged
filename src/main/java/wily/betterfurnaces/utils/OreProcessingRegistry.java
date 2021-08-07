package wily.betterfurnaces.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;

/**
 * Handles the ore processing upgrade.
 * @author Shadows
 *
 */
public class OreProcessingRegistry {

	public static List<Pair<Ingredient, ItemStack>> recipes = new ArrayList<>();

	public static void registerOre(String input, ItemStack result) {
		recipes.add(Pair.of(new OreIngredient(input), result));
	}

	public static void registerResult(ItemStack input, ItemStack result) {
		recipes.add(Pair.of(Ingredient.fromStacks(input), result));
	}

	public static ItemStack getSmeltingResult(ItemStack stack) {
		if (stack.isEmpty()) return ItemStack.EMPTY;
		for (Pair<Ingredient, ItemStack> ent : recipes) {
			if (ent.getLeft().apply(stack)) return ent.getRight();
		}
		return ItemStack.EMPTY;
	}

	public static void registerDefaults() {
		registerOre("sand", new ItemStack(Blocks.GLASS, 2));
		registerOre("cobblestone", new ItemStack(Blocks.STONE, 2));
		registerResult(new ItemStack(Blocks.NETHERRACK), new ItemStack(Items.NETHERBRICK, 2));
		registerOre("cobblestone", new ItemStack(Blocks.STONE, 2));
		registerResult(new ItemStack(Items.CLAY_BALL), new ItemStack(Items.BRICK, 2));
	}

}