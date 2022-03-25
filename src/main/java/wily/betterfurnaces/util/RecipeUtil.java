package wily.betterfurnaces.util;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.NonNullList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeUtil {

    public static List<IRecipe<?>> getRecipes(RecipeManager manager, IRecipeType<?> type) {
        Collection<IRecipe<?>> recipes = manager.getRecipes();
        return (List)recipes.stream().filter((iRecipe) -> {
            return iRecipe.getType() == type;
        }).collect(Collectors.toList());
    }

    public static <T> NonNullList<T> nnListOf(T... toList) {
        NonNullList<T> list = NonNullList.create();
        list.addAll(Arrays.asList(toList));
        return list;
    }

}