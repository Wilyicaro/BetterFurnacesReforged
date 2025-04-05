package wily.betterfurnaces.util;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeUtil {

    public static <T extends Recipe<?>> List<T> getRecipes(RecipeManager manager, RecipeType<?> type) {
        return manager.getRecipes().stream().filter((iRecipe) -> iRecipe/*? if >1.20.2 {*/.value()/*?}*/.getType() == type).map(h-> (T) h/*? if >1.20.2 {*/.value()/*?}*/).collect(Collectors.toList());
    }

}