package wily.betterfurnaces;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import wily.betterfurnaces.blockentity.CobblestoneGeneratorBlockEntity;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;

import java.nio.file.Path;

public class BetterFurnacesPlatform {
@ExpectPlatform
    public static Path getConfigDirectory() {
        throw new AssertionError();
    }
@ExpectPlatform
    public static Tag<Item> getCommonItemTag(String tag){throw new AssertionError();}
    @ExpectPlatform
    public static void smeltingAutoIO(SmeltingBlockEntity be){
        throw new AssertionError();
    }
    @ExpectPlatform
    public static void transferEnergySides(SmeltingBlockEntity be){
        throw new AssertionError();
    }
    @ExpectPlatform
    public static void autoOutput(InventoryBlockEntity be, int output) {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static void registerModel(ResourceLocation modelResourceLocation) {throw new AssertionError();}
}
