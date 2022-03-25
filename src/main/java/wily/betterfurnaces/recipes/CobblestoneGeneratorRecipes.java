package wily.betterfurnaces.recipes;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;


import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CobblestoneGeneratorRecipes implements IRecipe<IInventory> {
    public static final Serializer SERIALIZER = new Serializer();

    public static IRecipeType<CobblestoneGeneratorRecipes> TYPE;
    public ResourceLocation recipeId;
    private final HashMap<Ingredient, Integer> ingredients = new LinkedHashMap<>();
    public Ingredient result;
    public int duration;
    public CobblestoneGeneratorRecipes(ResourceLocation recipeId) {
        this.recipeId = recipeId;
    }
    public int getDuration() {
        return duration;
    }
    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return result.test(inv.getItem(0));
    }

    @Override
    public ItemStack assemble(IInventory p_44001_) {
        return getResultItem().copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return result.getItems()[0];
    }

    @Override
    public ResourceLocation getId() {
        return recipeId;
    }
    public Map<Ingredient, Integer> getIngredientMap() {
        return ImmutableMap.copyOf(ingredients);
    }
    public int getIngredientCost(ItemStack stack) {
        for (Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()) {
            if (entry.getKey().test(stack)) {
                return entry.getValue();
            }
        }
        return 0;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CobblestoneGeneratorRecipes> {

        @Override
        public CobblestoneGeneratorRecipes fromJson(ResourceLocation recipeId, JsonObject json) {
            CobblestoneGeneratorRecipes recipe = new CobblestoneGeneratorRecipes(recipeId);
            JsonObject ingredient = JSONUtils.getAsJsonObject(json, "result");
            recipe.result = Ingredient.fromJson(json.get("result"));
            recipe.duration = JSONUtils.getAsInt(json, "duration", 600);

            return recipe;
        }

        @Nullable
        @Override
        public CobblestoneGeneratorRecipes fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            CobblestoneGeneratorRecipes recipe = new CobblestoneGeneratorRecipes(recipeId);
            recipe.result = Ingredient.fromNetwork(buffer);
            recipe.duration = buffer.readInt();
            return recipe;
        }

        @Override
        public void toNetwork(PacketBuffer buffer, CobblestoneGeneratorRecipes recipe) {

            buffer.writeByte(recipe.ingredients.size());
            recipe.ingredients.forEach((ingredient, count) -> {
                ingredient.toNetwork(buffer);
                buffer.writeByte(count);
            });
            recipe.result.toNetwork(buffer);
            buffer.writeInt(recipe.duration);

        }
    }
        @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
    @Override
    public IRecipeType<?> getType() {
        return TYPE;
    }
}
