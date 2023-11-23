package wily.betterfurnaces.init;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.blocks.*;
import wily.betterfurnaces.inventory.*;
import wily.betterfurnaces.items.*;
import wily.betterfurnaces.recipes.CobblestoneGeneratorRecipes;
import wily.betterfurnaces.util.registration.SmeltingBlocks;
import wily.factoryapi.FactoryAPIPlatform;

import static wily.betterfurnaces.init.Registration.*;
import static wily.betterfurnaces.init.Registration.ITEMS;

public class ModObjects {



    public static Item.Properties defaultItemProperties(){ return  new Item.Properties().tab(BetterFurnacesReforged.ITEM_GROUP);}

    private static Item.Properties uniqueStackItemProperties(){ return  defaultItemProperties().stacksTo(1);}

    public static final RegistrySupplier<RecipeSerializer<CobblestoneGeneratorRecipes>> COB_GENERATION_SERIALIZER = RECIPES_SERIALIZERS.register("rock_generating", () -> CobblestoneGeneratorRecipes.SERIALIZER);

    public static final RegistrySupplier<RecipeType<CobblestoneGeneratorRecipes>> ROCK_GENERATING_RECIPE = RECIPES.register("rock_generating", () -> new RecipeType<>() {});

    public static final RegistrySupplier<MenuType<SmeltingMenu>> FURNACE_CONTAINER = CONTAINERS.register("furnace", () -> MenuRegistry.ofExtended((windowId, inv, data) -> new SmeltingMenu(windowId, inv.player.level, data.readBlockPos(), inv, inv.player)));

    public static final RegistrySupplier<MenuType<ForgeMenu>> FORGE_CONTAINER = CONTAINERS.register("forge", () -> MenuRegistry.ofExtended((windowId, inv, data) -> new ForgeMenu(windowId, inv.player.level, data.readBlockPos(), inv, inv.player)));


    public static final RegistrySupplier<SmeltingBlock> IRON_FURNACE = BLOCK_ITEMS.register(SmeltingBlocks.IRON_FURNACE.getName(), () -> new SmeltingBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK),Config.ironTierSpeed));
    public static final RegistrySupplier<SmeltingBlock> GOLD_FURNACE = BLOCK_ITEMS.register(SmeltingBlocks.GOLD_FURNACE.getName(), () -> new SmeltingBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK),Config.goldTierSpeed));
    public static final RegistrySupplier<MenuType<ColorUpgradeItem.ContainerColorUpgrade>> COLOR_UPGRADE_CONTAINER = CONTAINERS.register("color_upgrade", () -> MenuRegistry.ofExtended((windowId, inv, data) -> new ColorUpgradeItem.ContainerColorUpgrade(windowId, inv, inv.player.getMainHandItem())));
    public static final RegistrySupplier<SmeltingBlock> DIAMOND_FURNACE = BLOCK_ITEMS.register(SmeltingBlocks.DIAMOND_FURNACE.getName(), () -> new SmeltingBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK).noOcclusion(),Config.diamondTierSpeed));
    public static final RegistrySupplier<SmeltingBlock> NETHERHOT_FURNACE = BLOCK_ITEMS.register(SmeltingBlocks.NETHERHOT_FURNACE.getName(), () -> new SmeltingBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_BLOCK),Config.netherhotTierSpeed));
    public static final RegistrySupplier<SmeltingBlock> EXTREME_FURNACE = BLOCK_ITEMS.register(SmeltingBlocks.EXTREME_FURNACE.getName(), () -> new SmeltingBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK).strength(20.0F, 3000.0F),Config.extremeTierSpeed));
    public static final RegistrySupplier<ForgeBlock> EXTREME_FORGE = BLOCK_ITEMS.register(SmeltingBlocks.EXTREME_FORGE.getName(), () -> new ForgeBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).strength(30.0F, 6000.0F), Config.extremeTierSpeed));
    public static final RegistrySupplier<CobblestoneGeneratorBlock> COBBLESTONE_GENERATOR = BLOCK_ITEMS.register(CobblestoneGeneratorBlock.COBBLESTONE_GENERATOR, () -> new CobblestoneGeneratorBlock(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE)));
    public static final RegistrySupplier<MenuType<CobblestoneGeneratorMenu>> COB_GENERATOR_CONTAINER = CONTAINERS.register(CobblestoneGeneratorBlock.COBBLESTONE_GENERATOR, () -> MenuRegistry.ofExtended((windowId, inv, data) -> new CobblestoneGeneratorMenu(windowId, inv.player.level, data.readBlockPos(), inv, inv.player)));

    public static final RegistrySupplier<FuelVerifierBlock> FUEL_VERIFIER = BLOCK_ITEMS.register(FuelVerifierBlock.FUEL_VERIFIER, () -> new FuelVerifierBlock(BlockBehaviour.Properties.copy(Blocks.COBBLESTONE)));
    public static final RegistrySupplier<MenuType<FuelVerifierMenu>> FUEL_VERIFIER_CONTAINER = CONTAINERS.register(FuelVerifierBlock.FUEL_VERIFIER, () -> MenuRegistry.ofExtended((windowId, inv, data) -> new FuelVerifierMenu(windowId, inv.player.level,data.readBlockPos(), inv, inv.player) {
    }));


    public static final RegistrySupplier<BFRBlock> IRON_CONDUCTOR_BLOCK = BLOCK_ITEMS.register("iron_conductor_block", () -> new BFRBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(8.0F, 20.0F)));

    public static final RegistrySupplier<BFRBlock> GOLD_CONDUCTOR_BLOCK = BLOCK_ITEMS.register("gold_conductor_block", () -> new BFRBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK).strength(8.0F, 20.0F)));

    public static final RegistrySupplier<BFRBlock> NETHERHOT_CONDUCTOR_BLOCK = BLOCK_ITEMS.register("netherhot_conductor_block", () -> new BFRBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_BLOCK).strength(10.0F, 40.0F)));

    public static final RegistrySupplier<IronUpgradeItem> IRON_UPGRADE = ITEMS.register("iron_upgrade", () -> new IronUpgradeItem(uniqueStackItemProperties()));
    public static final RegistrySupplier<GoldUpgradeItem> GOLD_UPGRADE = ITEMS.register("gold_upgrade", () -> new GoldUpgradeItem(uniqueStackItemProperties()));
    public static final RegistrySupplier<DiamondUpgradeItem> DIAMOND_UPGRADE = ITEMS.register("diamond_upgrade", () -> new DiamondUpgradeItem(uniqueStackItemProperties()));
    public static final RegistrySupplier<NetherhotUpgradeItem> NETHERHOT_UPGRADE = ITEMS.register("netherhot_upgrade", () -> new NetherhotUpgradeItem(uniqueStackItemProperties()));
    public static final RegistrySupplier<ExtremeUpgradeItem> EXTREME_UPGRADE = ITEMS.register("extreme_upgrade", () -> new ExtremeUpgradeItem(uniqueStackItemProperties()));

    public static final RegistrySupplier<FuelEfficiencyUpgradeItem> FUEL = ITEMS.register("fuel_efficiency_upgrade", () -> new FuelEfficiencyUpgradeItem(uniqueStackItemProperties().durability(256),2));
    public static final RegistrySupplier<OreProcessingUpgradeItem> ORE_PROCESSING = ITEMS.register("ore_processing_upgrade", () -> new OreProcessingUpgradeItem(uniqueStackItemProperties().durability(128),2,true,false));
    public static final RegistrySupplier<OreProcessingUpgradeItem> RAWORE_PROCESSING = ITEMS.register("raw_ore_processing_upgrade", () -> new OreProcessingUpgradeItem(uniqueStackItemProperties(),2,false,true));
    public static final RegistrySupplier<FuelEfficiencyUpgradeItem> ADVFUEL = ITEMS.register("advanced_fuel_efficiency_upgrade", () -> new FuelEfficiencyUpgradeItem(uniqueStackItemProperties(),2));
    public static final RegistrySupplier<OreProcessingUpgradeItem> ADVORE_PROCESSING = ITEMS.register("advanced_ore_processing_upgrade", () -> new OreProcessingUpgradeItem(uniqueStackItemProperties(),2,true,false));
    public static final RegistrySupplier<FactoryUpgradeItem> FACTORY = ITEMS.register("factory_upgrade", () -> new FactoryUpgradeItem(uniqueStackItemProperties(), "factory", true,true,true,true));
    public static final RegistrySupplier<FactoryUpgradeItem> PIPING = ITEMS.register("piping_upgrade", () -> new FactoryUpgradeItem(uniqueStackItemProperties(), "piping", false,false,true,false));
    public static final RegistrySupplier<FactoryUpgradeItem> OUTPUT = ITEMS.register("autooutput_upgrade", () -> new FactoryUpgradeItem(uniqueStackItemProperties(), "output", true,false,true,false));
    public static final RegistrySupplier<FactoryUpgradeItem> INPUT = ITEMS.register("autoinput_upgrade", () -> new FactoryUpgradeItem(uniqueStackItemProperties(), "input", false,true,true,false));
    public static final RegistrySupplier<FactoryUpgradeItem> REDSTONE = ITEMS.register("redstone_signal_upgrade", () -> new FactoryUpgradeItem(uniqueStackItemProperties(), "redstone", false,false,false,true));
    public static final RegistrySupplier<ColorUpgradeItem> COLOR = ITEMS.register("color_upgrade", () -> new ColorUpgradeItem(uniqueStackItemProperties(),"color"));
    public static final RegistrySupplier<LiquidFuelUpgradeItem> LIQUID = ITEMS.register("liquid_fuel_upgrade", () -> new LiquidFuelUpgradeItem(uniqueStackItemProperties(),"liquid"));
    public static final RegistrySupplier<EnergyFuelUpgradeItem> ENERGY = ITEMS.register("energy_upgrade", () -> new EnergyFuelUpgradeItem(uniqueStackItemProperties(), new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.energy", FactoryAPIPlatform.getPlatformEnergyComponent().getString()).setStyle(Style.EMPTY.applyFormat((ChatFormatting.GRAY)))));
    public static final RegistrySupplier<XpTankUpgradeItem> XP = ITEMS.register("xp_tank_upgrade", () -> new XpTankUpgradeItem(uniqueStackItemProperties(),"xp"));
    public static final RegistrySupplier<TypeUpgradeItem> BLAST = ITEMS.register("blasting_upgrade", () -> new TypeUpgradeItem(uniqueStackItemProperties(),"blasting"));
    public static final RegistrySupplier<TypeUpgradeItem> SMOKE = ITEMS.register("smoking_upgrade", () -> new TypeUpgradeItem(uniqueStackItemProperties(),"smoking"));
    public static final RegistrySupplier<GeneratorUpgradeItem> GENERATOR = ITEMS.register("generator_upgrade", () -> new GeneratorUpgradeItem(uniqueStackItemProperties()));

    public static final RegistrySupplier<StorageUpgradeItem> STORAGE = ITEMS.register("storage_upgrade", () -> new StorageUpgradeItem(uniqueStackItemProperties()));


    public static void init() {}

}
