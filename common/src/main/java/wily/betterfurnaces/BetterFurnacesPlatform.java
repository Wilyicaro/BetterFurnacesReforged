package wily.betterfurnaces;

import dev.architectury.event.events.common.ChunkEvent;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ChunkPos;
import wily.betterfurnaces.blockentity.AbstractCobblestoneGeneratorBlockEntity;
import wily.betterfurnaces.blockentity.AbstractSmeltingBlockEntity;

import java.nio.file.Path;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class BetterFurnacesPlatform {
@ExpectPlatform
    public static Path getConfigDirectory() {
        throw new AssertionError();
    }
@ExpectPlatform
    public static TagKey<Item> getCommonItemTag(String tag){throw new AssertionError();}
    @ExpectPlatform
    public static void smeltingAutoIO(AbstractSmeltingBlockEntity be){
        throw new AssertionError();
    }
    @ExpectPlatform
    public static void transferEnergySides(AbstractSmeltingBlockEntity be){
        throw new AssertionError();
    }
    @ExpectPlatform
    public static void outputAutoIO(AbstractCobblestoneGeneratorBlockEntity be){
        throw new AssertionError();
    }

}
