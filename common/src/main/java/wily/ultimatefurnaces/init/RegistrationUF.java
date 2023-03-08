package wily.ultimatefurnaces.init;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.blockentity.ForgeBlockEntity;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.blocks.ForgeBlock;
import wily.betterfurnaces.blocks.SmeltingBlock;
import wily.betterfurnaces.items.OreProcessingUpgradeItem;
import wily.betterfurnaces.util.registration.SmeltingBlocks;
import wily.ultimatefurnaces.UltimateFurnaces;
import wily.ultimatefurnaces.items.*;

import static wily.betterfurnaces.init.Registration.*;

public class RegistrationUF {
    public static final DeferredRegister<Block> BLOCK_ITEMS = DeferredRegister.create(BetterFurnacesReforged.MOD_ID, Registry.BLOCK_REGISTRY);

    public static void init() {
        BLOCK_ITEMS.register();
        BLOCK_ITEMS.forEach((b)-> ITEMS.getRegistrar().register( b.getId(),() -> new BlockItem(b.get(), defaultItemProperties())));
    }



    private static Item.Properties defaultItemProperties(){ return  new Item.Properties().tab(UltimateFurnaces.ITEM_GROUP);}
    private static Item.Properties uniqueStackItemProperties(){ return  defaultItemProperties().stacksTo(1);}
    public static final RegistrySupplier<SmeltingBlock> COPPER_FURNACE = BLOCK_ITEMS.register(SmeltingBlocks.COPPER_FURNACE.getName(), () -> new SmeltingBlock(BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK).strength(10.0F, 20.0F)));
    public static final RegistrySupplier<BlockEntityType<SmeltingBlockEntity>> COPPER_FURNACE_TILE = BLOCK_ENTITIES.register(SmeltingBlocks.COPPER_FURNACE.getName(), () -> BlockEntityType.Builder.of((l, b)-> new SmeltingBlockEntity(l,b, Config.copperTierSpeed), COPPER_FURNACE.get()).build(null));


    public static final RegistrySupplier<SmeltingBlock> STEEL_FURNACE = BLOCK_ITEMS.register(SmeltingBlocks.STEEL_FURNACE.getName(), () -> new SmeltingBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(20.0F, 60.0F)));
    public static final RegistrySupplier<BlockEntityType<SmeltingBlockEntity>> STEEL_FURNACE_TILE = BLOCK_ENTITIES.register(SmeltingBlocks.STEEL_FURNACE.getName(), () -> BlockEntityType.Builder.of((l, b)-> new SmeltingBlockEntity(l,b, Config.steelTierSpeed), STEEL_FURNACE.get()).build(null));

    public static final RegistrySupplier<SmeltingBlock> AMETHYST_FURNACE = BLOCK_ITEMS.register(SmeltingBlocks.AMETHYST_FURNACE.getName(), () -> new SmeltingBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK).strength(20.0F, 30.0F)));
    public static final RegistrySupplier<BlockEntityType<SmeltingBlockEntity>> AMETHYST_FURNACE_TILE = BLOCK_ENTITIES.register(SmeltingBlocks.AMETHYST_FURNACE.getName(), () -> BlockEntityType.Builder.of((l, b)-> new SmeltingBlockEntity(l,b, Config.amethystTierSpeed), AMETHYST_FURNACE.get()).build(null));

    public static final RegistrySupplier<SmeltingBlock> PLATINUM_FURNACE = BLOCK_ITEMS.register(SmeltingBlocks.PLATINUM_FURNACE.getName(), () -> new SmeltingBlock(BlockBehaviour.Properties.copy(Blocks.RAW_GOLD_BLOCK).strength(30.0F, 60.0F)));
    public static final RegistrySupplier<BlockEntityType<SmeltingBlockEntity>> PLATINUM_FURNACE_TILE = BLOCK_ENTITIES.register(SmeltingBlocks.PLATINUM_FURNACE.getName(), () -> BlockEntityType.Builder.of((l, b)-> new SmeltingBlockEntity(l,b, Config.platinumTierSpeed), PLATINUM_FURNACE.get()).build(null));


    public static final RegistrySupplier<SmeltingBlock> ULTIMATE_FURNACE = BLOCK_ITEMS.register(SmeltingBlocks.ULTIMATE_FURNACE.getName(), () -> new SmeltingBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).strength(50.0F, 6000.0F)));
    public static final RegistrySupplier<BlockEntityType<SmeltingBlockEntity>> ULTIMATE_FURNACE_TILE = BLOCK_ENTITIES.register(SmeltingBlocks.ULTIMATE_FURNACE.getName(), () -> BlockEntityType.Builder.of((l, b)-> new SmeltingBlockEntity(l,b, Config.ultimateTierSpeed), ULTIMATE_FURNACE.get()).build(null));


    public static final RegistrySupplier<ForgeBlock> COPPER_FORGE = BLOCK_ITEMS.register(SmeltingBlocks.COPPER_FORGE.getName(), () -> new ForgeBlock(BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK)));
    public static final RegistrySupplier<BlockEntityType<ForgeBlockEntity>> COPPER_FORGE_TILE = BLOCK_ENTITIES.register(SmeltingBlocks.COPPER_FORGE.getName(), () -> BlockEntityType.Builder.of((l, b)-> new ForgeBlockEntity(l,b, Config.copperTierSpeed), COPPER_FORGE.get()).build(null));

    public static final RegistrySupplier<ForgeBlock> IRON_FORGE = BLOCK_ITEMS.register(SmeltingBlocks.IRON_FORGE.getName(), () -> new ForgeBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistrySupplier<BlockEntityType<ForgeBlockEntity>> IRON_FORGE_TILE = BLOCK_ENTITIES.register(SmeltingBlocks.IRON_FORGE.getName(), () -> BlockEntityType.Builder.of((l, b)-> new ForgeBlockEntity(l,b, Config.ironTierSpeed), IRON_FORGE.get()).build(null));


    public static final RegistrySupplier<ForgeBlock> GOLD_FORGE = BLOCK_ITEMS.register(SmeltingBlocks.GOLD_FORGE.getName(), () -> new ForgeBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)));
    public static final RegistrySupplier<BlockEntityType<ForgeBlockEntity>> GOLD_FORGE_TILE = BLOCK_ENTITIES.register(SmeltingBlocks.GOLD_FORGE.getName(), () -> BlockEntityType.Builder.of((l, b)-> new ForgeBlockEntity(l,b, Config.goldTierSpeed), GOLD_FORGE.get()).build(null));


    public static final RegistrySupplier<ForgeBlock> DIAMOND_FORGE = BLOCK_ITEMS.register(SmeltingBlocks.DIAMOND_FORGE.getName(), () -> new ForgeBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final RegistrySupplier<BlockEntityType<ForgeBlockEntity>> DIAMOND_FORGE_TILE = BLOCK_ENTITIES.register(SmeltingBlocks.DIAMOND_FORGE.getName(), () -> BlockEntityType.Builder.of((l, b)-> new ForgeBlockEntity(l,b, Config.diamondTierSpeed), DIAMOND_FORGE.get()).build(null));


    public static final RegistrySupplier<ForgeBlock> NETHERHOT_FORGE = BLOCK_ITEMS.register(SmeltingBlocks.NETHERHOT_FORGE.getName(), () -> new ForgeBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final RegistrySupplier<BlockEntityType<ForgeBlockEntity>> NETHERHOT_FORGE_TILE = BLOCK_ENTITIES.register(SmeltingBlocks.NETHERHOT_FORGE.getName(), () -> BlockEntityType.Builder.of((l, b)-> new ForgeBlockEntity(l,b, Config.netherhotTierSpeed), NETHERHOT_FORGE.get()).build(null));


    public static final RegistrySupplier<ForgeBlock> ULTIMATE_FORGE = BLOCK_ITEMS.register(SmeltingBlocks.ULTIMATE_FORGE.getName(), () -> new ForgeBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).strength(50.0F, 6000.0F)));
    public static final RegistrySupplier<BlockEntityType<ForgeBlockEntity>> ULTIMATE_FORGE_TILE = BLOCK_ENTITIES.register(SmeltingBlocks.ULTIMATE_FORGE.getName(), () -> BlockEntityType.Builder.of((l, b)-> new ForgeBlockEntity(l,b, Config.ultimateTierSpeed), ULTIMATE_FORGE.get()).build(null));


    public static final RegistrySupplier<CopperUpgradeItem> COPPER_UPGRADE = ITEMS.register("copper_upgrade", () -> new CopperUpgradeItem(uniqueStackItemProperties()));

    public static final RegistrySupplier<IronUpgradeItem> IRON_UPGRADE = ITEMS.register("copper_iron_upgrade", () -> new IronUpgradeItem(uniqueStackItemProperties()));

    public static final RegistrySupplier<SteelUpgradeItem> STEEL_UPGRADE = ITEMS.register("steel_upgrade", () -> new SteelUpgradeItem(uniqueStackItemProperties()));

    public static final RegistrySupplier<GoldUpgradeItem> GOLD_UPGRADE = ITEMS.register("steel_gold_upgrade", () -> new GoldUpgradeItem(uniqueStackItemProperties()));

    public static final RegistrySupplier<AmethystUpgradeItem> AMETHYST_UPGRADE = ITEMS.register("amethyst_upgrade", () -> new AmethystUpgradeItem(uniqueStackItemProperties()));

    public static final RegistrySupplier<DiamondUpgradeItem> DIAMOND_UPGRADE = ITEMS.register("amethyst_diamond_upgrade", () -> new DiamondUpgradeItem(uniqueStackItemProperties()));

    public static final RegistrySupplier<PlatinumUpgradeItem> PLATINUM_UPGRADE = ITEMS.register("platinum_upgrade", () -> new PlatinumUpgradeItem(uniqueStackItemProperties()));

    public static final RegistrySupplier<NetherhotUpgradeItem> NETHERHOT_UPGRADE = ITEMS.register("platinum_netherhot_upgrade", () -> new NetherhotUpgradeItem(uniqueStackItemProperties()));

    public static final RegistrySupplier<UltimateUpgradeItem> ULTIMATE_UPGRADE = ITEMS.register("ultimate_upgrade", () -> new UltimateUpgradeItem(uniqueStackItemProperties()));

    public static final RegistrySupplier<OreProcessingUpgradeItem> UORE_PROCESSING = ITEMS.register("ultimate_ore_processing_upgrade", () -> new OreProcessingUpgradeItem(uniqueStackItemProperties(),4,true,true));

}
