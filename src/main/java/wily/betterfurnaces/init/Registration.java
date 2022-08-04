package wily.betterfurnaces.init;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.*;
import wily.betterfurnaces.blocks.*;
import wily.betterfurnaces.inventory.*;
import wily.betterfurnaces.items.*;
import wily.betterfurnaces.recipes.CobblestoneGeneratorRecipes;

public class Registration {

    private static final DeferredRegister<Block> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.BLOCKS, BetterFurnacesReforged.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BetterFurnacesReforged.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, BetterFurnacesReforged.MOD_ID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, BetterFurnacesReforged.MOD_ID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPES_SEREALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BetterFurnacesReforged.MOD_ID);
    //private static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, MOD_ID);
    //private static final DeferredRegister<ModDimension> DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, MOD_ID);

    public static void init() {
        BLOCK_ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        RECIPES_SEREALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        //ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        //DIMENSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    @SubscribeEvent
    public static void registerBlockItems(final RegistryEvent.Register<Item> a){
        for (RegistryObject<Block> block : BLOCK_ITEMS.getEntries()){
            a.getRegistry().register(new BlockItem(block.get(), defaultItemProperties()).setRegistryName(block.getId()));
        }

    }

    private static Item.Properties defaultItemProperties(){ return new Item.Properties().tab(ModObjects.ITEM_GROUP);}
    
    public static final RegistryObject<RecipeSerializer<CobblestoneGeneratorRecipes>> COB_GENERATION_SEREALIZER = RECIPES_SEREALIZERS.register("rock_generating", () -> CobblestoneGeneratorRecipes.SERIALIZER);

    public static final RegistryObject<IronFurnaceBlock> IRON_FURNACE = BLOCK_ITEMS.register(IronFurnaceBlock.IRON_FURNACE, () -> new IronFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<BlockEntityType<IronFurnaceBlockEntity>> IRON_FURNACE_TILE = TILES.register(IronFurnaceBlock.IRON_FURNACE, () -> BlockEntityType.Builder.of(IronFurnaceBlockEntity::new, IRON_FURNACE.get()).build(null));

    public static final RegistryObject<MenuType<IronFurnaceMenu>> IRON_FURNACE_CONTAINER = CONTAINERS.register(IronFurnaceBlock.IRON_FURNACE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new IronFurnaceMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<GoldFurnaceBlock> GOLD_FURNACE = BLOCK_ITEMS.register(GoldFurnaceBlock.GOLD_FURNACE, () -> new GoldFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)));
    public static final RegistryObject<BlockEntityType<GoldFurnaceBlockEntity>> GOLD_FURNACE_TILE = TILES.register(GoldFurnaceBlock.GOLD_FURNACE, () -> BlockEntityType.Builder.of(GoldFurnaceBlockEntity::new, GOLD_FURNACE.get()).build(null));

    public static final RegistryObject<MenuType<GoldFurnaceMenu>> GOLD_FURNACE_CONTAINER = CONTAINERS.register(GoldFurnaceBlock.GOLD_FURNACE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new GoldFurnaceMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<MenuType<ColorUpgradeItem.ContainerColorUpgrade>> COLOR_UPGRADE_CONTAINER = CONTAINERS.register("color_upgrade", () -> IForgeMenuType.create((windowId, inv, data) -> {
        ItemStack helditem = inv.player.getMainHandItem();
        return new ColorUpgradeItem.ContainerColorUpgrade(windowId, inv, helditem);
    }));

    public static final RegistryObject<DiamondFurnaceBlock> DIAMOND_FURNACE = BLOCK_ITEMS.register(DiamondFurnaceBlock.DIAMOND_FURNACE, () -> new DiamondFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<BlockEntityType<DiamondFurnaceBlockEntity>> DIAMOND_FURNACE_TILE = TILES.register(DiamondFurnaceBlock.DIAMOND_FURNACE, () -> BlockEntityType.Builder.of(DiamondFurnaceBlockEntity::new, DIAMOND_FURNACE.get()).build(null));

    public static final RegistryObject<MenuType<DiamondFurnaceMenu>> DIAMOND_FURNACE_CONTAINER = CONTAINERS.register(DiamondFurnaceBlock.DIAMOND_FURNACE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new DiamondFurnaceMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<NetherhotFurnaceBlock> NETHERHOT_FURNACE = BLOCK_ITEMS.register(NetherhotFurnaceBlock.NETHERHOT_FURNACE, () -> new NetherhotFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_BLOCK)));
    public static final RegistryObject<BlockEntityType<NetherhotFurnaceBlockEntity>> NETHERHOT_FURNACE_TILE = TILES.register(NetherhotFurnaceBlock.NETHERHOT_FURNACE, () -> BlockEntityType.Builder.of(NetherhotFurnaceBlockEntity::new, NETHERHOT_FURNACE.get()).build(null));

    public static final RegistryObject<MenuType<NetherhotFurnaceMenu>> NETHERHOT_FURNACE_CONTAINER = CONTAINERS.register(NetherhotFurnaceBlock.NETHERHOT_FURNACE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new NetherhotFurnaceMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<ExtremeFurnaceBlock> EXTREME_FURNACE = BLOCK_ITEMS.register(ExtremeFurnaceBlock.EXTREME_FURNACE, () -> new ExtremeFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK).strength(20.0F, 3000.0F)));
    public static final RegistryObject<BlockEntityType<ExtremeFurnaceBlockEntity>> EXTREME_FURNACE_TILE = TILES.register(ExtremeFurnaceBlock.EXTREME_FURNACE, () -> BlockEntityType.Builder.of(ExtremeFurnaceBlockEntity::new, EXTREME_FURNACE.get()).build(null));

    public static final RegistryObject<MenuType<ExtremeFurnaceMenu>> EXTREME_FURNACE_CONTAINER = CONTAINERS.register(ExtremeFurnaceBlock.EXTREME_FURNACE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new ExtremeFurnaceMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<ExtremeForgeBlock> EXTREME_FORGE = BLOCK_ITEMS.register(ExtremeForgeBlock.EXTREME_FORGE, () -> new ExtremeForgeBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).strength(30.0F, 6000.0F)));
    public static final RegistryObject<BlockEntityType<ExtremeForgeBlockEntity>> EXTREME_FORGE_TILE = TILES.register(ExtremeForgeBlock.EXTREME_FORGE, () -> BlockEntityType.Builder.of(ExtremeForgeBlockEntity::new, EXTREME_FORGE.get()).build(null));

    public static final RegistryObject<MenuType<ExtremeForgeMenu>> EXTREME_FORGE_CONTAINER = CONTAINERS.register(ExtremeForgeBlock.EXTREME_FORGE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new ExtremeForgeMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<CobblestoneGeneratorBlock> COBBLESTONE_GENERATOR = BLOCK_ITEMS.register(CobblestoneGeneratorBlock.COBBLESTONE_GENERATOR, () -> new CobblestoneGeneratorBlock(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE)) {});
    public static final RegistryObject<BlockEntityType<AbstractCobblestoneGeneratorBlockEntity.CobblestoneGeneratorBlockEntity>> COB_GENERATOR_TILE = TILES.register(CobblestoneGeneratorBlock.COBBLESTONE_GENERATOR, () -> BlockEntityType.Builder.of(AbstractCobblestoneGeneratorBlockEntity.CobblestoneGeneratorBlockEntity::new, COBBLESTONE_GENERATOR.get()).build(null));

    public static final RegistryObject<MenuType<AbstractCobblestoneGeneratorMenu.CobblestoneGeneratorMenu>> COB_GENERATOR_CONTAINER = CONTAINERS.register(CobblestoneGeneratorBlock.COBBLESTONE_GENERATOR, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new AbstractCobblestoneGeneratorMenu.CobblestoneGeneratorMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<FuelVerifierBlock> FUEL_VERIFIER = BLOCK_ITEMS.register(FuelVerifierBlock.FUEL_VERIFIER, () -> new FuelVerifierBlock(BlockBehaviour.Properties.copy(Blocks.COBBLESTONE)) {});
    public static final RegistryObject<BlockEntityType<AbstractFuelVerifierBlockEntity.FuelVerifierBlockEntity>> FUEL_VERIFIER_TILE = TILES.register(FuelVerifierBlock.FUEL_VERIFIER, () -> BlockEntityType.Builder.of(AbstractFuelVerifierBlockEntity.FuelVerifierBlockEntity::new, FUEL_VERIFIER.get()).build(null));

    public static final RegistryObject<MenuType<AbstractFuelVerifierMenu.FuelVerifierMenu>> FUEL_VERIFIER_CONTAINER = CONTAINERS.register(FuelVerifierBlock.FUEL_VERIFIER, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new AbstractFuelVerifierMenu.FuelVerifierMenu(windowId, world, pos, inv, inv.player) {
        };
    }));


    public static final RegistryObject<ConductorBlock> IRON_CONDUCTOR_BLOCK = BLOCK_ITEMS.register("iron_conductor_block", () -> new ConductorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(8.0F, 20.0F)));

    public static final RegistryObject<ConductorBlock> GOLD_CONDUCTOR_BLOCK = BLOCK_ITEMS.register("gold_conductor_block", () -> new ConductorBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK).strength(8.0F, 20.0F)));

    public static final RegistryObject<ConductorBlock> NETHERHOT_CONDUCTOR_BLOCK = BLOCK_ITEMS.register("netherhot_conductor_block", () -> new ConductorBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_BLOCK).strength(10.0F, 40.0F)));

    public static final RegistryObject<IronUpgradeItem> IRON_UPGRADE = ITEMS.register("iron_upgrade", () -> new IronUpgradeItem(defaultItemProperties()));
    public static final RegistryObject<GoldUpgradeItem> GOLD_UPGRADE = ITEMS.register("gold_upgrade", () -> new GoldUpgradeItem(defaultItemProperties()));
    public static final RegistryObject<DiamondUpgradeItem> DIAMOND_UPGRADE = ITEMS.register("diamond_upgrade", () -> new DiamondUpgradeItem(defaultItemProperties()));
    public static final RegistryObject<NetherhotUpgradeItem> NETHERHOT_UPGRADE = ITEMS.register("netherhot_upgrade", () -> new NetherhotUpgradeItem(defaultItemProperties()));
    public static final RegistryObject<ExtremeUpgradeItem> EXTREME_UPGRADE = ITEMS.register("extreme_upgrade", () -> new ExtremeUpgradeItem(defaultItemProperties()));

    public static final RegistryObject<FuelEfficiencyUpgradeItem> FUEL = ITEMS.register("fuel_efficiency_upgrade", () -> new FuelEfficiencyUpgradeItem(defaultItemProperties().stacksTo(1).durability(256),2));
    public static final RegistryObject<OreProcessingUpgradeItem> ORE_PROCESSING = ITEMS.register("ore_processing_upgrade", () -> new OreProcessingUpgradeItem(defaultItemProperties().stacksTo(1).durability(128),2,true,false));
    public static final RegistryObject<OreProcessingUpgradeItem> RAWORE_PROCESSING = ITEMS.register("raw_ore_processing_upgrade", () -> new OreProcessingUpgradeItem(defaultItemProperties().stacksTo(1),2,false,true));
    public static final RegistryObject<FuelEfficiencyUpgradeItem> ADVFUEL = ITEMS.register("advanced_fuel_efficiency_upgrade", () -> new FuelEfficiencyUpgradeItem(defaultItemProperties().stacksTo(1),2));
    public static final RegistryObject<OreProcessingUpgradeItem> ADVORE_PROCESSING = ITEMS.register("advanced_ore_processing_upgrade", () -> new OreProcessingUpgradeItem(defaultItemProperties().stacksTo(1),2,true,false));
    public static final RegistryObject<FactoryUpgradeItem> FACTORY = ITEMS.register("factory_upgrade", () -> new FactoryUpgradeItem(defaultItemProperties().stacksTo(1), "factory", true,true,true,true));
    public static final RegistryObject<FactoryUpgradeItem> PIPING = ITEMS.register("piping_upgrade", () -> new FactoryUpgradeItem(defaultItemProperties().stacksTo(1), "piping", false,false,false,true));
    public static final RegistryObject<FactoryUpgradeItem> OUTPUT = ITEMS.register("autooutput_upgrade", () -> new FactoryUpgradeItem(defaultItemProperties().stacksTo(1), "output", true,false,true,false));
    public static final RegistryObject<FactoryUpgradeItem> INPUT = ITEMS.register("autoinput_upgrade", () -> new FactoryUpgradeItem(defaultItemProperties().stacksTo(1), "input", false,true,true,false));
    public static final RegistryObject<FactoryUpgradeItem> REDSTONE = ITEMS.register("redstone_signal_upgrade", () -> new FactoryUpgradeItem(defaultItemProperties().stacksTo(1), "redstone", false,false,false,true));
    public static final RegistryObject<ColorUpgradeItem> COLOR = ITEMS.register("color_upgrade", () -> new ColorUpgradeItem(defaultItemProperties().stacksTo(1),"color"));
    public static final RegistryObject<LiquidFuelUpgradeItem> LIQUID = ITEMS.register("liquid_fuel_upgrade", () -> new LiquidFuelUpgradeItem(defaultItemProperties().stacksTo(1),"liquid"));
    public static final RegistryObject<EnergyFuelUpgradeItem> ENERGY = ITEMS.register("energy_upgrade", () -> new EnergyFuelUpgradeItem(defaultItemProperties().stacksTo(1),"energy"));
    public static final RegistryObject<XpTankUpgradeItem> XP = ITEMS.register("xp_tank_upgrade", () -> new XpTankUpgradeItem(defaultItemProperties().stacksTo(1),"xp"));
    public static final RegistryObject<TypeUpgradeItem> BLAST = ITEMS.register("blasting_upgrade", () -> new TypeUpgradeItem(defaultItemProperties().stacksTo(1),"blasting"));
    public static final RegistryObject<TypeUpgradeItem> SMOKE = ITEMS.register("smoking_upgrade", () -> new TypeUpgradeItem(defaultItemProperties().stacksTo(1),"smoking"));




}
