package wily.betterfurnaces.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import wily.betterfurnaces.blockentity.CobblestoneGeneratorBlockEntity;
import wily.betterfurnaces.blockentity.ForgeBlockEntity;
import wily.betterfurnaces.blockentity.FuelVerifierBlockEntity;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.blocks.CobblestoneGeneratorBlock;
import wily.betterfurnaces.blocks.FuelVerifierBlock;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.base.RegisterListing;

import java.util.function.Supplier;

import static wily.betterfurnaces.init.ModObjects.COBBLESTONE_GENERATOR;
import static wily.betterfurnaces.init.ModObjects.FUEL_VERIFIER;
import static wily.betterfurnaces.init.Registration.*;

public class BlockEntityTypes {
    public static void init() {
    }

    public static final RegisterListing.Holder<BlockEntityType<SmeltingBlockEntity>> BETTER_FURNACE_TILE = BLOCK_ENTITIES.add("furnace", () -> FactoryAPIPlatform.createBlockEntityType(SmeltingBlockEntity::new, Registration.getFurnaces().map(Supplier::get).toArray(Block[]::new)));
    public static final RegisterListing.Holder<BlockEntityType<ForgeBlockEntity>> FORGE_TILE = BLOCK_ENTITIES.add("forge", () -> FactoryAPIPlatform.createBlockEntityType(ForgeBlockEntity::new, Registration.getForges().map(Supplier::get).toArray(Block[]::new)));

    public static final RegisterListing.Holder<BlockEntityType<FuelVerifierBlockEntity>> FUEL_VERIFIER_TILE = BLOCK_ENTITIES.add(FuelVerifierBlock.FUEL_VERIFIER, () -> FactoryAPIPlatform.createBlockEntityType(FuelVerifierBlockEntity::new, FUEL_VERIFIER.get()));
    public static final RegisterListing.Holder<BlockEntityType<CobblestoneGeneratorBlockEntity>> COB_GENERATOR_TILE = BLOCK_ENTITIES.add(CobblestoneGeneratorBlock.COBBLESTONE_GENERATOR, () -> FactoryAPIPlatform.createBlockEntityType(CobblestoneGeneratorBlockEntity::new, COBBLESTONE_GENERATOR.get()));

}
