package wily.betterfurnaces.init;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.BlockEntityExtremeFurnace;
import wily.betterfurnaces.blocks.*;
import wily.betterfurnaces.container.*;
import wily.betterfurnaces.items.*;
import wily.betterfurnaces.blockentity.*;
import wily.betterfurnaces.blockentity.BlockEntityDiamondFurnace;

public class Registration {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BetterFurnacesReforged.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BetterFurnacesReforged.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, BetterFurnacesReforged.MOD_ID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, BetterFurnacesReforged.MOD_ID);
    //private static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, MOD_ID);
    //private static final DeferredRegister<ModDimension> DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, MOD_ID);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        //ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        //DIMENSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }


    public static final RegistryObject<BlockIronFurnace> IRON_FURNACE = BLOCKS.register(BlockIronFurnace.IRON_FURNACE, () -> new BlockIronFurnace(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Item> IRON_FURNACE_ITEM = ITEMS.register(BlockIronFurnace.IRON_FURNACE, () -> new BlockItem(IRON_FURNACE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<BlockEntityIronFurnace>> IRON_FURNACE_TILE = TILES.register(BlockIronFurnace.IRON_FURNACE, () -> BlockEntityType.Builder.of(BlockEntityIronFurnace::new, IRON_FURNACE.get()).build(null));

    public static final RegistryObject<MenuType<BlockIronFurnaceContainer>> IRON_FURNACE_CONTAINER = CONTAINERS.register(BlockIronFurnace.IRON_FURNACE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new BlockIronFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockGoldFurnace> GOLD_FURNACE = BLOCKS.register(BlockGoldFurnace.GOLD_FURNACE, () -> new BlockGoldFurnace(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)));
    public static final RegistryObject<Item> GOLD_FURNACE_ITEM = ITEMS.register(BlockGoldFurnace.GOLD_FURNACE, () -> new BlockItem(GOLD_FURNACE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<BlockEntityGoldFurnace>> GOLD_FURNACE_TILE = TILES.register(BlockGoldFurnace.GOLD_FURNACE, () -> BlockEntityType.Builder.of(BlockEntityGoldFurnace::new, GOLD_FURNACE.get()).build(null));

    public static final RegistryObject<MenuType<BlockGoldFurnaceContainer>> GOLD_FURNACE_CONTAINER = CONTAINERS.register(BlockGoldFurnace.GOLD_FURNACE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new BlockGoldFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<MenuType<ItemColorUpgrade.ContainerColorUpgrade>> COLOR_UPGRADE_CONTAINER = CONTAINERS.register("color_upgrade", () -> IForgeMenuType.create((windowId, inv, data) -> {
        ItemStack helditem = inv.player.getMainHandItem();
        return new ItemColorUpgrade.ContainerColorUpgrade(windowId, inv, helditem);
    }));

    public static final RegistryObject<wily.betterfurnaces.blocks.BlockDiamondFurnace> DIAMOND_FURNACE = BLOCKS.register(wily.betterfurnaces.blocks.BlockDiamondFurnace.DIAMOND_FURNACE, () -> new wily.betterfurnaces.blocks.BlockDiamondFurnace(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<Item> DIAMOND_FURNACE_ITEM = ITEMS.register(wily.betterfurnaces.blocks.BlockDiamondFurnace.DIAMOND_FURNACE, () -> new BlockItem(DIAMOND_FURNACE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<BlockEntityDiamondFurnace>> DIAMOND_FURNACE_TILE = TILES.register(wily.betterfurnaces.blocks.BlockDiamondFurnace.DIAMOND_FURNACE, () -> BlockEntityType.Builder.of(BlockEntityDiamondFurnace::new, DIAMOND_FURNACE.get()).build(null));

    public static final RegistryObject<MenuType<BlockDiamondFurnaceContainer>> DIAMOND_FURNACE_CONTAINER = CONTAINERS.register(wily.betterfurnaces.blocks.BlockDiamondFurnace.DIAMOND_FURNACE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new BlockDiamondFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockNetherhotFurnace> NETHERHOT_FURNACE = BLOCKS.register(BlockNetherhotFurnace.NETHERHOT_FURNACE, () -> new BlockNetherhotFurnace(BlockBehaviour.Properties.copy(Blocks.REDSTONE_BLOCK)));
    public static final RegistryObject<Item> NETHERHOT_FURNACE_ITEM = ITEMS.register(BlockNetherhotFurnace.NETHERHOT_FURNACE, () -> new BlockItem(NETHERHOT_FURNACE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<BlockEntityNetherhotFurnace>> NETHERHOT_FURNACE_TILE = TILES.register(BlockNetherhotFurnace.NETHERHOT_FURNACE, () -> BlockEntityType.Builder.of(BlockEntityNetherhotFurnace::new, NETHERHOT_FURNACE.get()).build(null));

    public static final RegistryObject<MenuType<BlockNetherhotFurnaceContainer>> NETHERHOT_FURNACE_CONTAINER = CONTAINERS.register(BlockNetherhotFurnace.NETHERHOT_FURNACE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new BlockNetherhotFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<wily.betterfurnaces.blocks.BlockExtremeFurnace> EXTREME_FURNACE = BLOCKS.register(wily.betterfurnaces.blocks.BlockExtremeFurnace.EXTREME_FURNACE, () -> new wily.betterfurnaces.blocks.BlockExtremeFurnace(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK).strength(20.0F, 3000.0F)));
    public static final RegistryObject<Item> EXTREME_FURNACE_ITEM = ITEMS.register(wily.betterfurnaces.blocks.BlockExtremeFurnace.EXTREME_FURNACE, () -> new BlockItem(EXTREME_FURNACE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<BlockEntityExtremeFurnace>> EXTREME_FURNACE_TILE = TILES.register(wily.betterfurnaces.blocks.BlockExtremeFurnace.EXTREME_FURNACE, () -> BlockEntityType.Builder.of(BlockEntityExtremeFurnace::new, EXTREME_FURNACE.get()).build(null));

    public static final RegistryObject<MenuType<BlockExtremeFurnaceContainer>> EXTREME_FURNACE_CONTAINER = CONTAINERS.register(wily.betterfurnaces.blocks.BlockExtremeFurnace.EXTREME_FURNACE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new BlockExtremeFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockExtremeForge> EXTREME_FORGE = BLOCKS.register(BlockExtremeForge.EXTREME_FORGE, () -> new BlockExtremeForge(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).strength(30.0F, 6000.0F)));
    public static final RegistryObject<Item> EXTREME_FORGE_ITEM = ITEMS.register(BlockExtremeForge.EXTREME_FORGE, () -> new BlockItem(EXTREME_FORGE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<BlockEntityExtremeForge>> EXTREME_FORGE_TILE = TILES.register(BlockExtremeForge.EXTREME_FORGE, () -> BlockEntityType.Builder.of(BlockEntityExtremeForge::new, EXTREME_FORGE.get()).build(null));

    public static final RegistryObject<MenuType<BlockExtremeForgeContainer>> EXTREME_FORGE_CONTAINER = CONTAINERS.register(BlockExtremeForge.EXTREME_FORGE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new BlockExtremeForgeContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockCobblestoneGenerator> COBBLESTONE_GENERATOR = BLOCKS.register(BlockCobblestoneGenerator.COBBLESTONE_GENERATOR, () -> new BlockCobblestoneGenerator(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE)) {});
    public static final RegistryObject<Item> COBBLESTONE_GENERATOR_ITEM = ITEMS.register(BlockCobblestoneGenerator.COBBLESTONE_GENERATOR, () -> new BlockItem(COBBLESTONE_GENERATOR.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<BlockEntityCobblestoneGenerator.BlockEntityCobblestoneGeneratorDefinition>> COB_GENERATOR_TILE = TILES.register(BlockCobblestoneGenerator.COBBLESTONE_GENERATOR, () -> BlockEntityType.Builder.of(BlockEntityCobblestoneGenerator.BlockEntityCobblestoneGeneratorDefinition::new, COBBLESTONE_GENERATOR.get()).build(null));

    public static final RegistryObject<MenuType<BlockEntityCobblestoneGenerator.BlockCobblestoneGeneratorContainer>> COB_GENERATOR_CONTAINER = CONTAINERS.register(BlockCobblestoneGenerator.COBBLESTONE_GENERATOR, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new BlockEntityCobblestoneGenerator.BlockCobblestoneGeneratorContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockFuelVerifier> FUEL_VERIFIER = BLOCKS.register(BlockFuelVerifier.FUEL_VERIFIER, () -> new BlockFuelVerifier(BlockBehaviour.Properties.copy(Blocks.COBBLESTONE)) {});
    public static final RegistryObject<Item> FUEL_VERIFIER_ITEM = ITEMS.register(BlockFuelVerifier.FUEL_VERIFIER, () -> new BlockItem(FUEL_VERIFIER.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<BlockEntityFuelVerifier.BlockEntityFuelVerifierDefinition>> FUEL_VERIFIER_TILE = TILES.register(BlockFuelVerifier.FUEL_VERIFIER, () -> BlockEntityType.Builder.of(BlockEntityFuelVerifier.BlockEntityFuelVerifierDefinition::new, FUEL_VERIFIER.get()).build(null));

    public static final RegistryObject<MenuType<BlockEntityFuelVerifier.BlockFuelVerifierTileContainer>> FUEL_VERIFIER_CONTAINER = CONTAINERS.register(BlockFuelVerifier.FUEL_VERIFIER, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new BlockEntityFuelVerifier.BlockFuelVerifierTileContainer(windowId, world, pos, inv, inv.player);
    }));


    public static final RegistryObject<BlockConductorBase> IRON_CONDUCTOR_BLOCK = BLOCKS.register("iron_conductor_block", () -> new BlockConductorBase(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(8.0F, 20.0F)));
    public static final RegistryObject<Item>  IRON_CONDUCTOR_ITEM = ITEMS.register("iron_conductor_block", () -> new BlockItem(IRON_CONDUCTOR_BLOCK.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<BlockConductorBase> GOLD_CONDUCTOR_BLOCK = BLOCKS.register("gold_conductor_block", () -> new BlockConductorBase(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK).strength(8.0F, 20.0F)));
    public static final RegistryObject<Item>  GOLD_CONDUCTOR_ITEM = ITEMS.register("gold_conductor_block", () -> new BlockItem(GOLD_CONDUCTOR_BLOCK.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<BlockConductorBase> NETHERHOT_CONDUCTOR_BLOCK = BLOCKS.register("netherhot_conductor_block", () -> new BlockConductorBase(BlockBehaviour.Properties.copy(Blocks.REDSTONE_BLOCK).strength(10.0F, 40.0F)));
    public static final RegistryObject<Item>  NETHERHOT_CONDUCTOR_ITEM = ITEMS.register("netherhot_conductor_block", () -> new BlockItem(NETHERHOT_CONDUCTOR_BLOCK.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<ItemUpgradeIron> IRON_UPGRADE = ITEMS.register("iron_upgrade", () -> new ItemUpgradeIron(new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<ItemUpgradeGold> GOLD_UPGRADE = ITEMS.register("gold_upgrade", () -> new ItemUpgradeGold(new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<ItemUpgradeDiamond> DIAMOND_UPGRADE = ITEMS.register("diamond_upgrade", () -> new ItemUpgradeDiamond(new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<ItemUpgradeNetherhot> NETHERHOT_UPGRADE = ITEMS.register("netherhot_upgrade", () -> new ItemUpgradeNetherhot(new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<ItemUpgradeExtreme> EXTREME_UPGRADE = ITEMS.register("extreme_upgrade", () -> new ItemUpgradeExtreme(new Item.Properties().tab(ModObjects.ITEM_GROUP)));


    public static final RegistryObject<ItemEnergyFuel> ENERGY = ITEMS.register("energy_upgrade", () -> new ItemEnergyFuel(new Item.Properties().tab(ModObjects.ITEM_GROUP).stacksTo(1)));

    public static final RegistryObject<ItemFuelEfficiency> FUEL = ITEMS.register("fuel_efficiency_upgrade", () -> new ItemFuelEfficiency(new Item.Properties().tab(ModObjects.ITEM_GROUP).stacksTo(1).durability(256)));
    public static final RegistryObject<ItemOreProcessing> ORE_PROCESSING = ITEMS.register("ore_processing_upgrade", () -> new ItemOreProcessing(new Item.Properties().tab(ModObjects.ITEM_GROUP).stacksTo(1).durability(128)));
    public static final RegistryObject<ItemFuelEfficiency> ADVFUEL = ITEMS.register("advanced_fuel_efficiency_upgrade", () -> new ItemFuelEfficiency(new Item.Properties().tab(ModObjects.ITEM_GROUP).stacksTo(1)));
    public static final RegistryObject<ItemOreProcessing> ADVORE_PROCESSING = ITEMS.register("advanced_ore_processing_upgrade", () -> new ItemOreProcessing(new Item.Properties().tab(ModObjects.ITEM_GROUP).stacksTo(1)));
    public static final RegistryObject<ItemUpgradeMisc> FACTORY = ITEMS.register("factory_upgrade", () -> new ItemUpgradeMisc(new Item.Properties().tab(ModObjects.ITEM_GROUP).stacksTo(1)));
    public static final RegistryObject<ItemColorUpgrade> COLOR = ITEMS.register("color_upgrade", () -> new ItemColorUpgrade(new Item.Properties().tab(ModObjects.ITEM_GROUP).stacksTo(1)));
    public static final RegistryObject<ItemLiquidFuel> LIQUID = ITEMS.register("liquid_fuel_upgrade", () -> new ItemLiquidFuel(new Item.Properties().tab(ModObjects.ITEM_GROUP).stacksTo(1)));
    public static final RegistryObject<ItemXpTank> XP = ITEMS.register("xp_tank_upgrade", () -> new ItemXpTank(new Item.Properties().tab(ModObjects.ITEM_GROUP).stacksTo(1)));


    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
    }


    public static void registerItems(RegistryEvent.Register<Item> event)
    {
    }


}
