package wily.betterfurnaces.jei;

import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.recipes.CobblestoneGeneratorRecipes;

public class BFRRecipeTypes {
    public static final mezz.jei.api.recipe.RecipeType<CobblestoneGeneratorRecipes> ROCK_GENERATING_JEI =
            mezz.jei.api.recipe.RecipeType.create(BetterFurnacesReforged.MOD_ID, "rock_generating", CobblestoneGeneratorRecipes.class);
}
