package wily.betterfurnaces.recipes;

import com.google.gson.JsonObject;
import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
//? if >=1.20.5 {
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
//?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import wily.betterfurnaces.init.ModObjects;
import wily.factoryapi.base.network.CommonNetwork;
import wily.factoryapi.util.DynamicUtil;

public record CobblestoneGeneratorRecipe(/*? if <1.20.2 {*//*ResourceLocation id, *//*?}*/ItemStack result, int duration) implements Recipe</*? if <1.20.5 {*//*Container*//*?} else {*/SingleRecipeInput/*?}*/> {
    public static final CobblestoneGeneratorRecipe.Serializer SERIALIZER = new CobblestoneGeneratorRecipe.Serializer();

    @Override
    public boolean isSpecial() {
        return true;
    }

    //? if <1.20.2 {
    /*@Override
    public ResourceLocation getId() {
        return id();
    }
    *///?}

    @Override
    public boolean matches(/*? if <1.20.5 {*//*Container*//*?} else {*/SingleRecipeInput/*?}*/ inv, Level worldIn) {
        return true;
    }

    @Override
    public ItemStack assemble(/*? if <1.20.5 {*//*Container*//*?} else {*/SingleRecipeInput/*?}*/ p_44001_/*? if <1.20.5 {*//*, RegistryAccess access*//*?} else {*/, HolderLookup.Provider access/*?}*/) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<CobblestoneGeneratorRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<CobblestoneGeneratorRecipe> getType() {
        return ModObjects.ROCK_GENERATING_RECIPE.get();
    }

    //? if >=1.21.2 {
    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }
    //?} else {
    /*@Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem(/^? if <1.20.5 {^//^RegistryAccess access^//^?} else {^/HolderLookup.Provider access/^?}^/) {
        return result;
    }
    *///?}

    public static class Serializer implements RecipeSerializer<CobblestoneGeneratorRecipe> {
        //? if >1.20.1 {
        private final MapCodec<CobblestoneGeneratorRecipe> codec = RecordCodecBuilder.mapCodec(i -> i.group(DynamicUtil.ITEM_CODEC.fieldOf("result").forGetter((rcp) -> rcp.result), Codec.INT.fieldOf("duration").orElse(80).forGetter((rcp) -> rcp.duration)).apply(i, CobblestoneGeneratorRecipe::new));

        public /*? if <1.21 {*//*Codec<CobblestoneGeneratorRecipe>*//*?} else {*/MapCodec<CobblestoneGeneratorRecipe>/*?}*/ codec() {
            return codec/*? if <1.21 {*//*.codec()*//*?}*/;
        }
        //?}
        //? if >=1.21 {
        private final StreamCodec<RegistryFriendlyByteBuf, CobblestoneGeneratorRecipe> streamCodec = StreamCodec.of((b,r)-> {
           CommonNetwork.encodeItemStack(()-> b, r.result);
           b.writeVarInt(r.duration);
        }, b-> new CobblestoneGeneratorRecipe(CommonNetwork.decodeItemStack(()->b), b.readVarInt()));
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CobblestoneGeneratorRecipe> streamCodec() {
            return streamCodec;
        }
        //?} else {

        /*public CobblestoneGeneratorRecipe fromNetwork(/^? if <1.20.2 {^//^ResourceLocation location, ^//^?}^/FriendlyByteBuf friendlyByteBuf) {
            return new CobblestoneGeneratorRecipe(/^? if <1.20.2 {^//^location, ^//^?}^/friendlyByteBuf.readItem(), friendlyByteBuf.readInt());
        }

        //? if <1.20.2 {
        /^@Override
        public CobblestoneGeneratorRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            return new CobblestoneGeneratorRecipe(resourceLocation, DynamicUtil.ITEM_CODEC.parse(JsonOps.INSTANCE, jsonObject.get("result")).result().get(), GsonHelper.getAsInt(jsonObject, "duration", 80));
        }
        ^///?}

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CobblestoneGeneratorRecipe recipe) {
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.duration);
        }
        *///?}

    }
}
