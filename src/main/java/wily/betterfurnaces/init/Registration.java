package wily.betterfurnaces.init;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blocks.*;
import wily.betterfurnaces.inventory.*;
import wily.betterfurnaces.items.*;
import wily.betterfurnaces.recipes.CobblestoneGeneratorRecipes;
import wily.betterfurnaces.tileentity.*;

public class Registration {

    private static final DeferredRegister<Block> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.BLOCKS, BetterFurnacesReforged.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BetterFurnacesReforged.MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, BetterFurnacesReforged.MOD_ID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, BetterFurnacesReforged.MOD_ID);
    private static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BetterFurnacesReforged.MOD_ID);
    //private static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, MOD_ID);
    //private static final DeferredRegister<ModDimension> DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, MOD_ID);

    public static void init() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCK_ITEMS.register(eventBus);
        ITEMS.register(eventBus);
        TILES.register(eventBus);
        CONTAINERS.register(eventBus);
        RECIPES.register(eventBus);
        recipeRegister("rock_generating");
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
    private static <T extends IRecipe<?>> IRecipeType<T> recipeRegister(final String key) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(BetterFurnacesReforged.MOD_ID, key), new IRecipeType<T>() {
            @Override
            public String toString() {
                return key;
            }
        });
    }
    public static IRecipeType<CobblestoneGeneratorRecipes> COB_GENERATION_RECIPE = recipeRegister("rock_generating");
    public static final RegistryObject<CobblestoneGeneratorRecipes.Serializer> COB_GENERATION_SERIALIZER = RECIPES.register("rock_generating", () -> CobblestoneGeneratorRecipes.SERIALIZER);

    public static final RegistryObject<IronFurnaceBlock> IRON_FURNACE = BLOCK_ITEMS.register(IronFurnaceBlock.IRON_FURNACE, () -> new IronFurnaceBlock(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<TileEntityType<IronFurnaceTileEntity>> IRON_FURNACE_TILE = TILES.register(IronFurnaceBlock.IRON_FURNACE, () -> TileEntityType.Builder.of(IronFurnaceTileEntity::new, IRON_FURNACE.get()).build(null));

    public static final RegistryObject<ContainerType<IronFurnaceContainer>> IRON_FURNACE_CONTAINER = CONTAINERS.register(IronFurnaceBlock.IRON_FURNACE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new IronFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<GoldFurnaceBlock> GOLD_FURNACE = BLOCK_ITEMS.register(GoldFurnaceBlock.GOLD_FURNACE, () -> new GoldFurnaceBlock(AbstractBlock.Properties.copy(Blocks.GOLD_BLOCK)));
    public static final RegistryObject<TileEntityType<GoldFurnaceTileEntity>> GOLD_FURNACE_TILE = TILES.register(GoldFurnaceBlock.GOLD_FURNACE, () -> TileEntityType.Builder.of(GoldFurnaceTileEntity::new, GOLD_FURNACE.get()).build(null));

    public static final RegistryObject<ContainerType<GoldFurnaceContainer>> GOLD_FURNACE_CONTAINER = CONTAINERS.register(GoldFurnaceBlock.GOLD_FURNACE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new GoldFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<ContainerType<ColorUpgradeItem.ContainerColorUpgrade>> COLOR_UPGRADE_CONTAINER = CONTAINERS.register("color_upgrade", () -> IForgeContainerType.create((windowId, inv, data) -> {
        ItemStack helditem = inv.player.getMainHandItem();
        return new ColorUpgradeItem.ContainerColorUpgrade(windowId, inv, helditem);
    }));

    public static final RegistryObject<DiamondFurnaceBlock> DIAMOND_FURNACE = BLOCK_ITEMS.register(DiamondFurnaceBlock.DIAMOND_FURNACE, () -> new DiamondFurnaceBlock(AbstractBlock.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<TileEntityType<DiamondFurnaceTileEntity>> DIAMOND_FURNACE_TILE = TILES.register(DiamondFurnaceBlock.DIAMOND_FURNACE, () -> TileEntityType.Builder.of(DiamondFurnaceTileEntity::new, DIAMOND_FURNACE.get()).build(null));

    public static final RegistryObject<ContainerType<DiamondFurnaceContainer>> DIAMOND_FURNACE_CONTAINER = CONTAINERS.register(DiamondFurnaceBlock.DIAMOND_FURNACE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new DiamondFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<NetherhotFurnaceBlock> NETHERHOT_FURNACE = BLOCK_ITEMS.register(NetherhotFurnaceBlock.NETHERHOT_FURNACE, () -> new NetherhotFurnaceBlock(AbstractBlock.Properties.copy(Blocks.REDSTONE_BLOCK)));
    public static final RegistryObject<TileEntityType<NetherhotFurnaceTileEntity>> NETHERHOT_FURNACE_TILE = TILES.register(NetherhotFurnaceBlock.NETHERHOT_FURNACE, () -> TileEntityType.Builder.of(NetherhotFurnaceTileEntity::new, NETHERHOT_FURNACE.get()).build(null));

    public static final RegistryObject<ContainerType<NetherhotFurnaceContainer>> NETHERHOT_FURNACE_CONTAINER = CONTAINERS.register(NetherhotFurnaceBlock.NETHERHOT_FURNACE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new NetherhotFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<ExtremeFurnaceBlock> EXTREME_FURNACE = BLOCK_ITEMS.register(ExtremeFurnaceBlock.EXTREME_FURNACE, () -> new ExtremeFurnaceBlock(AbstractBlock.Properties.copy(Blocks.DIAMOND_BLOCK).strength(20.0F, 3000.0F)));
    public static final RegistryObject<TileEntityType<ExtremeFurnaceTileEntity>> EXTREME_FURNACE_TILE = TILES.register(ExtremeFurnaceBlock.EXTREME_FURNACE, () -> TileEntityType.Builder.of(ExtremeFurnaceTileEntity::new, EXTREME_FURNACE.get()).build(null));

    public static final RegistryObject<ContainerType<ExtremeFurnaceContainer>> EXTREME_FURNACE_CONTAINER = CONTAINERS.register(ExtremeFurnaceBlock.EXTREME_FURNACE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new ExtremeFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<ExtremeForgeBlock> EXTREME_FORGE = BLOCK_ITEMS.register(ExtremeForgeBlock.EXTREME_FORGE, () -> new ExtremeForgeBlock(AbstractBlock.Properties.copy(Blocks.NETHERITE_BLOCK).strength(30.0F, 6000.0F)));
    public static final RegistryObject<TileEntityType<ExtremeForgeTileEntity>> EXTREME_FORGE_TILE = TILES.register(ExtremeForgeBlock.EXTREME_FORGE, () -> TileEntityType.Builder.of(ExtremeForgeTileEntity::new, EXTREME_FORGE.get()).build(null));

    public static final RegistryObject<ContainerType<ExtremeForgeContainer>> EXTREME_FORGE_CONTAINER = CONTAINERS.register(ExtremeForgeBlock.EXTREME_FORGE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new ExtremeForgeContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<CobblestoneGeneratorBlock> COBBLESTONE_GENERATOR = BLOCK_ITEMS.register(CobblestoneGeneratorBlock.COBBLESTONE_GENERATOR, () -> new CobblestoneGeneratorBlock(AbstractBlock.Properties.copy(Blocks.BLACKSTONE)) {});
    public static final RegistryObject<TileEntityType<AbstractCobblestoneGeneratorTileEntity.CobblestoneGeneratorTileEntity>> COB_GENERATOR_TILE = TILES.register(CobblestoneGeneratorBlock.COBBLESTONE_GENERATOR, () -> TileEntityType.Builder.of(AbstractCobblestoneGeneratorTileEntity.CobblestoneGeneratorTileEntity::new, COBBLESTONE_GENERATOR.get()).build(null));

    public static final RegistryObject<ContainerType<AbstractCobblestoneGeneratorContainer.CobblestoneGeneratorContainer>> COB_GENERATOR_CONTAINER = CONTAINERS.register(CobblestoneGeneratorBlock.COBBLESTONE_GENERATOR, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new AbstractCobblestoneGeneratorContainer.CobblestoneGeneratorContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<FuelVerifierBlock> FUEL_VERIFIER = BLOCK_ITEMS.register(FuelVerifierBlock.FUEL_VERIFIER, () -> new FuelVerifierBlock(AbstractBlock.Properties.copy(Blocks.COBBLESTONE)) {});
    public static final RegistryObject<TileEntityType<AbstractFuelVerifierTileEntity.FuelVerifierTileEntity>> FUEL_VERIFIER_TILE = TILES.register(FuelVerifierBlock.FUEL_VERIFIER, () -> TileEntityType.Builder.of(AbstractFuelVerifierTileEntity.FuelVerifierTileEntity::new, FUEL_VERIFIER.get()).build(null));

    public static final RegistryObject<ContainerType<AbstractFuelVerifierContainer.FuelVerifierContainer>> FUEL_VERIFIER_CONTAINER = CONTAINERS.register(FuelVerifierBlock.FUEL_VERIFIER, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new AbstractFuelVerifierContainer.FuelVerifierContainer(windowId, world, pos, inv, inv.player) {
        };
    }));

    public static final RegistryObject<ConductorBlock> IRON_CONDUCTOR_BLOCK = BLOCK_ITEMS.register("iron_conductor_block", () -> new ConductorBlock(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK).strength(8.0F, 20.0F)));
    public static final RegistryObject<ConductorBlock> GOLD_CONDUCTOR_BLOCK = BLOCK_ITEMS.register("gold_conductor_block", () -> new ConductorBlock(AbstractBlock.Properties.copy(Blocks.GOLD_BLOCK).strength(8.0F, 20.0F)));
    public static final RegistryObject<ConductorBlock> NETHERHOT_CONDUCTOR_BLOCK = BLOCK_ITEMS.register("netherhot_conductor_block", () -> new ConductorBlock(AbstractBlock.Properties.copy(Blocks.REDSTONE_BLOCK).strength(10.0F, 40.0F)));

    public static final RegistryObject<IronUpgradeItem> IRON_UPGRADE = ITEMS.register("iron_upgrade", () -> new IronUpgradeItem(defaultItemProperties()));
    public static final RegistryObject<GoldUpgradeItem> GOLD_UPGRADE = ITEMS.register("gold_upgrade", () -> new GoldUpgradeItem(defaultItemProperties()));
    public static final RegistryObject<DiamondUpgradeItem> DIAMOND_UPGRADE = ITEMS.register("diamond_upgrade", () -> new DiamondUpgradeItem(defaultItemProperties()));
    public static final RegistryObject<NetherhotUpgradeItem> NETHERHOT_UPGRADE = ITEMS.register("netherhot_upgrade", () -> new NetherhotUpgradeItem(defaultItemProperties()));
    public static final RegistryObject<ExtremeUpgradeItem> EXTREME_UPGRADE = ITEMS.register("extreme_upgrade", () -> new ExtremeUpgradeItem(defaultItemProperties()));


    public static final RegistryObject<FuelEfficiencyUpgradeItem> FUEL = ITEMS.register("fuel_efficiency_upgrade", () -> new FuelEfficiencyUpgradeItem(defaultItemProperties().stacksTo(1).durability(256),2));
    public static final RegistryObject<OreProcessingUpgradeItem> ORE_PROCESSING = ITEMS.register("ore_processing_upgrade", () -> new OreProcessingUpgradeItem(defaultItemProperties().stacksTo(1).durability(128),2));
    public static final RegistryObject<FuelEfficiencyUpgradeItem> ADVFUEL = ITEMS.register("advanced_fuel_efficiency_upgrade", () -> new FuelEfficiencyUpgradeItem(defaultItemProperties().stacksTo(1),2));
    public static final RegistryObject<OreProcessingUpgradeItem> ADVORE_PROCESSING = ITEMS.register("advanced_ore_processing_upgrade", () -> new OreProcessingUpgradeItem(defaultItemProperties().stacksTo(1),2));
    public static final RegistryObject<UpgradeItem> REDSTONE = ITEMS.register("redstone_signal_upgrade", () -> new FactoryUpgradeItem(defaultItemProperties().stacksTo(1),"redstone",false,false,false,true));
    public static final RegistryObject<UpgradeItem> PIPING = ITEMS.register("piping_upgrade", () -> new FactoryUpgradeItem(defaultItemProperties().stacksTo(1),"piping",false,false,true,false));
    public static final RegistryObject<UpgradeItem> INPUT_FACTORY = ITEMS.register("autoinput_upgrade", () -> new FactoryUpgradeItem(defaultItemProperties().stacksTo(1),"input",false,true,true,false));
    public static final RegistryObject<UpgradeItem> OUTPUT_FACTORY = ITEMS.register("autooutput_upgrade", () -> new FactoryUpgradeItem(defaultItemProperties().stacksTo(1), "output",true,false,true,false));
    public static final RegistryObject<UpgradeItem> FACTORY = ITEMS.register("factory_upgrade", () -> new FactoryUpgradeItem(defaultItemProperties().stacksTo(1),"factory",true,true,true,true));
    public static final RegistryObject<ColorUpgradeItem> COLOR = ITEMS.register("color_upgrade", () -> new ColorUpgradeItem(defaultItemProperties().stacksTo(1),"color"));
    public static final RegistryObject<LiquidFuelUpgradeItem> LIQUID = ITEMS.register("liquid_fuel_upgrade", () -> new LiquidFuelUpgradeItem(defaultItemProperties().stacksTo(1),"liquid"));
    public static final RegistryObject<EnergyFuelUpgradeItem> ENERGY = ITEMS.register("energy_upgrade", () -> new EnergyFuelUpgradeItem(defaultItemProperties().stacksTo(1),"energy"));
    public static final RegistryObject<UpgradeItemXpTank> XP = ITEMS.register("xp_tank_upgrade", () -> new UpgradeItemXpTank(defaultItemProperties().stacksTo(1),"xp"));
    public static final RegistryObject<TypeUpgradeItem> BLAST = ITEMS.register("blasting_upgrade", () -> new TypeUpgradeItem(defaultItemProperties().stacksTo(1),"blasting"));
    public static final RegistryObject<TypeUpgradeItem> SMOKE = ITEMS.register("smoking_upgrade", () -> new TypeUpgradeItem(defaultItemProperties().stacksTo(1),"smoking"));


}
