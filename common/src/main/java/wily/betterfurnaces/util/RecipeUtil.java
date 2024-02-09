package wily.betterfurnaces.util;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeUtil {
    public static <T extends Recipe<Container>> List<T> getRecipes(RecipeManager manager, RecipeType<?> type) {
        return manager.getRecipes().stream().filter((iRecipe) -> iRecipe.getType() == type).map(r-> (T) r).collect(Collectors.toList());
    }

}