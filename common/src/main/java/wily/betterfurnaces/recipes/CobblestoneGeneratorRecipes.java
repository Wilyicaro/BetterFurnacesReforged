package wily.betterfurnaces.recipes;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import wily.betterfurnaces.init.ModObjects;

public record CobblestoneGeneratorRecipes(ItemStack result, int duration) implements Recipe<Container> {
    public static final CobblestoneGeneratorRecipes.Serializer SERIALIZER = new CobblestoneGeneratorRecipes.Serializer();

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        return true;
    }

    @Override
    public ItemStack assemble(Container p_44001_, RegistryAccess access) {
        return getResultItem(access).copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return result;
    }


    public static class Serializer implements RecipeSerializer<CobblestoneGeneratorRecipes> {


        private final Codec<CobblestoneGeneratorRecipes> codec;

        public Serializer() {
            codec = RecordCodecBuilder.create((instance) -> {
                Products.P2<RecordCodecBuilder.Mu<CobblestoneGeneratorRecipes>, ItemStack, Integer> product =
                        instance.group(
                                ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter((abstractCookingRecipe) -> abstractCookingRecipe.result),
                                Codec.INT.fieldOf("duration").orElse(80).forGetter((abstractCookingRecipe) -> abstractCookingRecipe.duration));
                return product.apply(instance, CobblestoneGeneratorRecipes::new);
            });
        }

        @Override
        public Codec<CobblestoneGeneratorRecipes> codec() {
            return codec;
        }

        @Override
        public CobblestoneGeneratorRecipes fromNetwork(FriendlyByteBuf friendlyByteBuf) {
            return new CobblestoneGeneratorRecipes(friendlyByteBuf.readItem(), friendlyByteBuf.readInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CobblestoneGeneratorRecipes recipe) {
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.duration);
        }
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModObjects.ROCK_GENERATING_RECIPE.get();
    }
}
