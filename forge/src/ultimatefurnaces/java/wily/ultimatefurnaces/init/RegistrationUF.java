package wily.ultimatefurnaces.init;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistrySupplier;
import wily.betterfurnaces.items.OreProcessingUpgradeItem;
import wily.ultimatefurnaces.UltimateFurnaces;
import wily.ultimatefurnaces.blockentity.*;
import wily.ultimatefurnaces.blocks.*;
import wily.ultimatefurnaces.inventory.*;
import wily.ultimatefurnaces.items.*;

public class RegistrationUF {

    private static final DeferredRegister<Block> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.BLOCKS, UltimateFurnaces.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UltimateFurnaces.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, UltimateFurnaces.MOD_ID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, UltimateFurnaces.MOD_ID);

    public static void init() {
        BLOCK_ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public static void registerBlockItems(final RegisterEvent a){
        for (RegistrySupplier<Block> block : BLOCK_ITEMS.getEntries()) a.register(Registry.ITEM_REGISTRY, block.getId(),() -> new BlockItem(block.get(), defaultItemProperties()));

    }

    private static Item.Properties defaultItemProperties(){ return  new Item.Properties().tab(BetterFurnacesReforged.ITEM_GROUP);}
    public static final RegistrySupplier<CopperFurnaceBlock> COPPER_FURNACE = BLOCK_ITEMS.register(CopperFurnaceBlock.COPPER_FURNACE, () -> new CopperFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK).strength(10.0F, 20.0F)));
    public static final RegistrySupplier<BlockEntityType<CopperFurnaceBlockEntity>> COPPER_FURNACE_TILE = TILES.register(CopperFurnaceBlock.COPPER_FURNACE, () -> BlockEntityType.Builder.of(CopperFurnaceBlockEntity::new, COPPER_FURNACE.get()).build(null));

    public static final RegistrySupplier<MenuType<CopperFurnaceMenu>> COPPER_FURNACE_CONTAINER = CONTAINERS.register(CopperFurnaceBlock.COPPER_FURNACE, () -> MenuRegistry.ofExtended((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new CopperFurnaceMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistrySupplier<SteelFurnaceBlock> STEEL_FURNACE = BLOCK_ITEMS.register(SteelFurnaceBlock.STEEL_FURNACE, () -> new SteelFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(20.0F, 60.0F)));
    public static final RegistrySupplier<BlockEntityType<SteelFurnaceBlockEntity>> STEEL_FURNACE_TILE = TILES.register(SteelFurnaceBlock.STEEL_FURNACE, () -> BlockEntityType.Builder.of(SteelFurnaceBlockEntity::new, STEEL_FURNACE.get()).build(null));

    public static final RegistrySupplier<MenuType<SteelFurnaceMenu>> STEEL_FURNACE_CONTAINER = CONTAINERS.register(SteelFurnaceBlock.STEEL_FURNACE, () -> MenuRegistry.ofExtended((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new SteelFurnaceMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistrySupplier<AmethystFurnaceBlock> AMETHYST_FURNACE = BLOCK_ITEMS.register(AmethystFurnaceBlock.AMETHYST_FURNACE, () -> new AmethystFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK).strength(20.0F, 30.0F)));
    public static final RegistrySupplier<BlockEntityType<AmethystFurnaceBlockEntity>> AMETHYST_FURNACE_TILE = TILES.register(AmethystFurnaceBlock.AMETHYST_FURNACE, () -> BlockEntityType.Builder.of(AmethystFurnaceBlockEntity::new, AMETHYST_FURNACE.get()).build(null));

    public static final RegistrySupplier<MenuType<AmethystFurnaceMenu>> AMETHYST_FURNACE_CONTAINER = CONTAINERS.register(AmethystFurnaceBlock.AMETHYST_FURNACE, () -> MenuRegistry.ofExtended((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new AmethystFurnaceMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistrySupplier<PlatinumFurnaceBlock> PLATINUM_FURNACE = BLOCK_ITEMS.register(PlatinumFurnaceBlock.PLATINUM_FURNACE, () -> new PlatinumFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.RAW_GOLD_BLOCK).strength(30.0F, 60.0F)));
    public static final RegistrySupplier<BlockEntityType<PlatinumFurnaceBlockEntity>> PLATINUM_FURNACE_TILE = TILES.register(PlatinumFurnaceBlock.PLATINUM_FURNACE, () -> BlockEntityType.Builder.of(PlatinumFurnaceBlockEntity::new, PLATINUM_FURNACE.get()).build(null));

    public static final RegistrySupplier<MenuType<PlatinumFurnaceMenu>> PLATINUM_FURNACE_CONTAINER = CONTAINERS.register(PlatinumFurnaceBlock.PLATINUM_FURNACE, () -> MenuRegistry.ofExtended((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new PlatinumFurnaceMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistrySupplier<UltimateFurnaceBlock> ULTIMATE_FURNACE = BLOCK_ITEMS.register(UltimateFurnaceBlock.ULTIMATE_FURNACE, () -> new UltimateFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).strength(50.0F, 6000.0F)));
    public static final RegistrySupplier<BlockEntityType<UltimateFurnaceBlockEntity>> ULTIMATE_FURNACE_TILE = TILES.register(UltimateFurnaceBlock.ULTIMATE_FURNACE, () -> BlockEntityType.Builder.of(UltimateFurnaceBlockEntity::new, ULTIMATE_FURNACE.get()).build(null));

    public static final RegistrySupplier<MenuType<UltimateFurnaceMenu>> ULTIMATE_FURNACE_CONTAINER = CONTAINERS.register(UltimateFurnaceBlock.ULTIMATE_FURNACE, () -> MenuRegistry.ofExtended((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new UltimateFurnaceMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistrySupplier<CopperForgeBlock> COPPER_FORGE = BLOCK_ITEMS.register(CopperForgeBlock.COPPER_FORGE, () -> new CopperForgeBlock(BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK)));
    public static final RegistrySupplier<BlockEntityType<CopperForgeBlockEntity>> COPPER_FORGE_TILE = TILES.register(CopperForgeBlock.COPPER_FORGE, () -> BlockEntityType.Builder.of(CopperForgeBlockEntity::new, COPPER_FORGE.get()).build(null));

    public static final RegistrySupplier<MenuType<CopperForgeMenu>> COPPER_FORGE_CONTAINER = CONTAINERS.register(CopperForgeBlock.COPPER_FORGE, () -> MenuRegistry.ofExtended((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new CopperForgeMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistrySupplier<IronForgeBlock> IRON_FORGE = BLOCK_ITEMS.register(IronForgeBlock.IRON_FORGE, () -> new IronForgeBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistrySupplier<BlockEntityType<IronForgeBlockEntity>> IRON_FORGE_TILE = TILES.register(IronForgeBlock.IRON_FORGE, () -> BlockEntityType.Builder.of(IronForgeBlockEntity::new, IRON_FORGE.get()).build(null));

    public static final RegistrySupplier<MenuType<IronForgeMenu>> IRON_FORGE_CONTAINER = CONTAINERS.register(IronForgeBlock.IRON_FORGE, () -> MenuRegistry.ofExtended((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new IronForgeMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistrySupplier<GoldForgeBlock> GOLD_FORGE = BLOCK_ITEMS.register(GoldForgeBlock.GOLD_FORGE, () -> new GoldForgeBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)));
    public static final RegistrySupplier<BlockEntityType<GoldForgeBlockEntity>> GOLD_FORGE_TILE = TILES.register(GoldForgeBlock.GOLD_FORGE, () -> BlockEntityType.Builder.of(GoldForgeBlockEntity::new, GOLD_FORGE.get()).build(null));

    public static final RegistrySupplier<MenuType<GoldForgeMenu>> GOLD_FORGE_CONTAINER = CONTAINERS.register(GoldForgeBlock.GOLD_FORGE, () -> MenuRegistry.ofExtended((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new GoldForgeMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistrySupplier<DiamondForgeBlock> DIAMOND_FORGE = BLOCK_ITEMS.register(DiamondForgeBlock.DIAMOND_FORGE, () -> new DiamondForgeBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final RegistrySupplier<BlockEntityType<DiamondForgeBlockEntity>> DIAMOND_FORGE_TILE = TILES.register(DiamondForgeBlock.DIAMOND_FORGE, () -> BlockEntityType.Builder.of(DiamondForgeBlockEntity::new, DIAMOND_FORGE.get()).build(null));

    public static final RegistrySupplier<MenuType<DiamondForgeMenu>> DIAMOND_FORGE_CONTAINER = CONTAINERS.register(DiamondForgeBlock.DIAMOND_FORGE, () -> MenuRegistry.ofExtended((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new DiamondForgeMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistrySupplier<NetherhotForgeBlock> NETHERHOT_FORGE = BLOCK_ITEMS.register(NetherhotForgeBlock.NETHERHOT_FORGE, () -> new NetherhotForgeBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final RegistrySupplier<BlockEntityType<NetherhotForgeBlockEntity>> NETHERHOT_FORGE_TILE = TILES.register(NetherhotForgeBlock.NETHERHOT_FORGE, () -> BlockEntityType.Builder.of(NetherhotForgeBlockEntity::new, NETHERHOT_FORGE.get()).build(null));

    public static final RegistrySupplier<MenuType<NetherhotForgeMenu>> NETHERHOT_FORGE_CONTAINER = CONTAINERS.register(NetherhotForgeBlock.NETHERHOT_FORGE, () -> MenuRegistry.ofExtended((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new NetherhotForgeMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistrySupplier<UltimateForgeBlock> ULTIMATE_FORGE = BLOCK_ITEMS.register(UltimateForgeBlock.ULTIMATE_FORGE, () -> new UltimateForgeBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).strength(50.0F, 6000.0F)));
    public static final RegistrySupplier<BlockEntityType<UltimateForgeBlockEntity>> ULTIMATE_FORGE_TILE = TILES.register(UltimateForgeBlock.ULTIMATE_FORGE, () -> BlockEntityType.Builder.of(UltimateForgeBlockEntity::new, ULTIMATE_FORGE.get()).build(null));

    public static final RegistrySupplier<MenuType<UltimateForgeMenu>> ULTIMATE_FORGE_CONTAINER = CONTAINERS.register(UltimateForgeBlock.ULTIMATE_FORGE, () -> MenuRegistry.ofExtended((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new UltimateForgeMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistrySupplier<CopperUpgradeItem> COPPER_UPGRADE = ITEMS.register("copper_upgrade", () -> new CopperUpgradeItem(new Item.Properties().tab(BetterFurnacesReforged.ITEM_GROUP)));

    public static final RegistrySupplier<IronUpgradeItem> IRON_UPGRADE = ITEMS.register("iron_upgrade", () -> new IronUpgradeItem(new Item.Properties().tab(BetterFurnacesReforged.ITEM_GROUP)));

    public static final RegistrySupplier<SteelUpgradeItem> STEEL_UPGRADE = ITEMS.register("steel_upgrade", () -> new SteelUpgradeItem(new Item.Properties().tab(BetterFurnacesReforged.ITEM_GROUP)));

    public static final RegistrySupplier<GoldUpgradeItem> GOLD_UPGRADE = ITEMS.register("gold_upgrade", () -> new GoldUpgradeItem(new Item.Properties().tab(BetterFurnacesReforged.ITEM_GROUP)));

    public static final RegistrySupplier<AmethystUpgradeItem> AMETHYST_UPGRADE = ITEMS.register("amethyst_upgrade", () -> new AmethystUpgradeItem(new Item.Properties().tab(BetterFurnacesReforged.ITEM_GROUP)));

    public static final RegistrySupplier<DiamondUpgradeItem> DIAMOND_UPGRADE = ITEMS.register("diamond_upgrade", () -> new DiamondUpgradeItem(new Item.Properties().tab(BetterFurnacesReforged.ITEM_GROUP)));

    public static final RegistrySupplier<PlatinumUpgradeItem> PLATINUM_UPGRADE = ITEMS.register("platinum_upgrade", () -> new PlatinumUpgradeItem(new Item.Properties().tab(BetterFurnacesReforged.ITEM_GROUP)));

    public static final RegistrySupplier<NetherhotUpgradeItem> NETHERHOT_UPGRADE = ITEMS.register("netherhot_upgrade", () -> new NetherhotUpgradeItem(new Item.Properties().tab(BetterFurnacesReforged.ITEM_GROUP)));

    public static final RegistrySupplier<UltimateUpgradeItem> ULTIMATE_UPGRADE = ITEMS.register("ultimate_upgrade", () -> new UltimateUpgradeItem(new Item.Properties().tab(BetterFurnacesReforged.ITEM_GROUP)));

    public static final RegistrySupplier<OreProcessingUpgradeItem> UORE_PROCESSING = ITEMS.register("ultimate_ore_processing_upgrade", () -> new OreProcessingUpgradeItem(new Item.Properties().tab(BetterFurnacesReforged.ITEM_GROUP),4,true,true));

}
