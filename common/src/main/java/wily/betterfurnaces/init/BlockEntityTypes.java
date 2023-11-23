package wily.betterfurnaces.init;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import wily.betterfurnaces.blockentity.CobblestoneGeneratorBlockEntity;
import wily.betterfurnaces.blockentity.ForgeBlockEntity;
import wily.betterfurnaces.blockentity.FuelVerifierBlockEntity;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.blocks.CobblestoneGeneratorBlock;
import wily.betterfurnaces.blocks.FuelVerifierBlock;

import java.util.function.Supplier;

import static wily.betterfurnaces.init.ModObjects.COBBLESTONE_GENERATOR;
import static wily.betterfurnaces.init.ModObjects.FUEL_VERIFIER;
import static wily.betterfurnaces.init.Registration.*;

public class BlockEntityTypes {
    public static void init() {
    }
    public static final RegistrySupplier<BlockEntityType<SmeltingBlockEntity>> BETTER_FURNACE_TILE = BLOCK_ENTITIES.register("furnace", () -> BlockEntityType.Builder.of(SmeltingBlockEntity::new, FURNACES.stream().map(Supplier::get).toArray(Block[]::new)).build(null));
    public static final RegistrySupplier<BlockEntityType<ForgeBlockEntity>> FORGE_TILE = BLOCK_ENTITIES.register("forge", () -> BlockEntityType.Builder.of(ForgeBlockEntity::new, FORGES.stream().map(Supplier::get).toArray(Block[]::new)).build(null));

    public static final RegistrySupplier<BlockEntityType<FuelVerifierBlockEntity>> FUEL_VERIFIER_TILE = BLOCK_ENTITIES.register(FuelVerifierBlock.FUEL_VERIFIER, () -> BlockEntityType.Builder.of(FuelVerifierBlockEntity::new, FUEL_VERIFIER.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<CobblestoneGeneratorBlockEntity>> COB_GENERATOR_TILE = BLOCK_ENTITIES.register(CobblestoneGeneratorBlock.COBBLESTONE_GENERATOR, () -> BlockEntityType.Builder.of(CobblestoneGeneratorBlockEntity::new, COBBLESTONE_GENERATOR.get()).build(null));

}
