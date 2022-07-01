package wily.ultimatefurnaces.init;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wily.betterfurnaces.items.OreProcessingUpgradeItem;
import wily.ultimatefurnaces.UltimateFurnaces;
import wily.ultimatefurnaces.blockentity.*;
import wily.ultimatefurnaces.blocks.*;
import wily.ultimatefurnaces.inventory.*;
import wily.ultimatefurnaces.items.*;

public class RegistrationUF {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, UltimateFurnaces.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UltimateFurnaces.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, UltimateFurnaces.MOD_ID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, UltimateFurnaces.MOD_ID);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<CopperFurnaceBlock> COPPER_FURNACE = BLOCKS.register(CopperFurnaceBlock.COPPER_FURNACE, () -> new CopperFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK).strength(10.0F, 20.0F)));
    public static final RegistryObject<Item> COPPER_FURNACE_ITEM = ITEMS.register(CopperFurnaceBlock.COPPER_FURNACE, () -> new BlockItem(COPPER_FURNACE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<CopperFurnaceBlockEntity>> COPPER_FURNACE_TILE = TILES.register(CopperFurnaceBlock.COPPER_FURNACE, () -> BlockEntityType.Builder.of(CopperFurnaceBlockEntity::new, COPPER_FURNACE.get()).build(null));

    public static final RegistryObject<MenuType<CopperFurnaceMenu>> COPPER_FURNACE_CONTAINER = CONTAINERS.register(CopperFurnaceBlock.COPPER_FURNACE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new CopperFurnaceMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<SteelFurnaceBlock> STEEL_FURNACE = BLOCKS.register(SteelFurnaceBlock.STEEL_FURNACE, () -> new SteelFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(20.0F, 60.0F)));
    public static final RegistryObject<Item> STEEL_FURNACE_ITEM = ITEMS.register(SteelFurnaceBlock.STEEL_FURNACE, () -> new BlockItem(STEEL_FURNACE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<SteelFurnaceBlockEntity>> STEEL_FURNACE_TILE = TILES.register(SteelFurnaceBlock.STEEL_FURNACE, () -> BlockEntityType.Builder.of(SteelFurnaceBlockEntity::new, STEEL_FURNACE.get()).build(null));

    public static final RegistryObject<MenuType<SteelFurnaceMenu>> STEEL_FURNACE_CONTAINER = CONTAINERS.register(SteelFurnaceBlock.STEEL_FURNACE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new SteelFurnaceMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<AmethystFurnaceBlock> AMETHYST_FURNACE = BLOCKS.register(AmethystFurnaceBlock.AMETHYST_FURNACE, () -> new AmethystFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK).strength(20.0F, 30.0F)));
    public static final RegistryObject<Item> AMETHYST_FURNACE_ITEM = ITEMS.register(AmethystFurnaceBlock.AMETHYST_FURNACE, () -> new BlockItem(AMETHYST_FURNACE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<AmethystFurnaceBlockEntity>> AMETHYST_FURNACE_TILE = TILES.register(AmethystFurnaceBlock.AMETHYST_FURNACE, () -> BlockEntityType.Builder.of(AmethystFurnaceBlockEntity::new, AMETHYST_FURNACE.get()).build(null));

    public static final RegistryObject<MenuType<AmethystFurnaceMenu>> AMETHYST_FURNACE_CONTAINER = CONTAINERS.register(AmethystFurnaceBlock.AMETHYST_FURNACE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new AmethystFurnaceMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<PlatinumFurnaceBlock> PLATINUM_FURNACE = BLOCKS.register(PlatinumFurnaceBlock.PLATINUM_FURNACE, () -> new PlatinumFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.RAW_GOLD_BLOCK).strength(30.0F, 60.0F)));
    public static final RegistryObject<Item> PLATINUM_FURNACE_ITEM = ITEMS.register(PlatinumFurnaceBlock.PLATINUM_FURNACE, () -> new BlockItem(PLATINUM_FURNACE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<PlatinumFurnaceBlockEntity>> PLATINUM_FURNACE_TILE = TILES.register(PlatinumFurnaceBlock.PLATINUM_FURNACE, () -> BlockEntityType.Builder.of(PlatinumFurnaceBlockEntity::new, PLATINUM_FURNACE.get()).build(null));

    public static final RegistryObject<MenuType<PlatinumFurnaceMenu>> PLATINUM_FURNACE_CONTAINER = CONTAINERS.register(PlatinumFurnaceBlock.PLATINUM_FURNACE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new PlatinumFurnaceMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<UltimateFurnaceBlock> ULTIMATE_FURNACE = BLOCKS.register(UltimateFurnaceBlock.ULTIMATE_FURNACE, () -> new UltimateFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).strength(50.0F, 6000.0F)));
    public static final RegistryObject<Item> ULTIMATE_FURNACE_ITEM = ITEMS.register(UltimateFurnaceBlock.ULTIMATE_FURNACE, () -> new BlockItem(ULTIMATE_FURNACE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<UltimateFurnaceBlockEntity>> ULTIMATE_FURNACE_TILE = TILES.register(UltimateFurnaceBlock.ULTIMATE_FURNACE, () -> BlockEntityType.Builder.of(UltimateFurnaceBlockEntity::new, ULTIMATE_FURNACE.get()).build(null));

    public static final RegistryObject<MenuType<UltimateFurnaceMenu>> ULTIMATE_FURNACE_CONTAINER = CONTAINERS.register(UltimateFurnaceBlock.ULTIMATE_FURNACE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new UltimateFurnaceMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<CopperForgeBlock> COPPER_FORGE = BLOCKS.register(CopperForgeBlock.COPPER_FORGE, () -> new CopperForgeBlock(BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK)));
    public static final RegistryObject<Item> COPPER_FORGE_ITEM = ITEMS.register(CopperForgeBlock.COPPER_FORGE, () -> new BlockItem(COPPER_FORGE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<CopperForgeBlockEntity>> COPPER_FORGE_TILE = TILES.register(CopperForgeBlock.COPPER_FORGE, () -> BlockEntityType.Builder.of(CopperForgeBlockEntity::new, COPPER_FORGE.get()).build(null));

    public static final RegistryObject<MenuType<CopperForgeMenu>> COPPER_FORGE_CONTAINER = CONTAINERS.register(CopperForgeBlock.COPPER_FORGE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new CopperForgeMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<IronForgeBlock> IRON_FORGE = BLOCKS.register(IronForgeBlock.IRON_FORGE, () -> new IronForgeBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Item> IRON_FORGE_ITEM = ITEMS.register(IronForgeBlock.IRON_FORGE, () -> new BlockItem(IRON_FORGE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<IronForgeBlockEntity>> IRON_FORGE_TILE = TILES.register(IronForgeBlock.IRON_FORGE, () -> BlockEntityType.Builder.of(IronForgeBlockEntity::new, IRON_FORGE.get()).build(null));

    public static final RegistryObject<MenuType<IronForgeMenu>> IRON_FORGE_CONTAINER = CONTAINERS.register(IronForgeBlock.IRON_FORGE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new IronForgeMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<GoldForgeBlock> GOLD_FORGE = BLOCKS.register(GoldForgeBlock.GOLD_FORGE, () -> new GoldForgeBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)));
    public static final RegistryObject<Item> GOLD_FORGE_ITEM = ITEMS.register(GoldForgeBlock.GOLD_FORGE, () -> new BlockItem(GOLD_FORGE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<GoldForgeBlockEntity>> GOLD_FORGE_TILE = TILES.register(GoldForgeBlock.GOLD_FORGE, () -> BlockEntityType.Builder.of(GoldForgeBlockEntity::new, GOLD_FORGE.get()).build(null));

    public static final RegistryObject<MenuType<GoldForgeMenu>> GOLD_FORGE_CONTAINER = CONTAINERS.register(GoldForgeBlock.GOLD_FORGE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new GoldForgeMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<DiamondForgeBlock> DIAMOND_FORGE = BLOCKS.register(DiamondForgeBlock.DIAMOND_FORGE, () -> new DiamondForgeBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<Item> DIAMOND_FORGE_ITEM = ITEMS.register(DiamondForgeBlock.DIAMOND_FORGE, () -> new BlockItem(DIAMOND_FORGE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<DiamondForgeBlockEntity>> DIAMOND_FORGE_TILE = TILES.register(DiamondForgeBlock.DIAMOND_FORGE, () -> BlockEntityType.Builder.of(DiamondForgeBlockEntity::new, DIAMOND_FORGE.get()).build(null));

    public static final RegistryObject<MenuType<DiamondForgeMenu>> DIAMOND_FORGE_CONTAINER = CONTAINERS.register(DiamondForgeBlock.DIAMOND_FORGE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new DiamondForgeMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<NetherhotForgeBlock> NETHERHOT_FORGE = BLOCKS.register(NetherhotForgeBlock.NETHERHOT_FORGE, () -> new NetherhotForgeBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<Item> NETHERHOT_FORGE_ITEM = ITEMS.register(NetherhotForgeBlock.NETHERHOT_FORGE, () -> new BlockItem(NETHERHOT_FORGE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<NetherhotForgeBlockEntity>> NETHERHOT_FORGE_TILE = TILES.register(NetherhotForgeBlock.NETHERHOT_FORGE, () -> BlockEntityType.Builder.of(NetherhotForgeBlockEntity::new, NETHERHOT_FORGE.get()).build(null));

    public static final RegistryObject<MenuType<NetherhotForgeMenu>> NETHERHOT_FORGE_CONTAINER = CONTAINERS.register(NetherhotForgeBlock.NETHERHOT_FORGE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new NetherhotForgeMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<UltimateForgeBlock> ULTIMATE_FORGE = BLOCKS.register(UltimateForgeBlock.ULTIMATE_FORGE, () -> new UltimateForgeBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).strength(50.0F, 6000.0F)));
    public static final RegistryObject<Item> ULTIMATE_FORGE_ITEM = ITEMS.register(UltimateForgeBlock.ULTIMATE_FORGE, () -> new BlockItem(ULTIMATE_FORGE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<UltimateForgeBlockEntity>> ULTIMATE_FORGE_TILE = TILES.register(UltimateForgeBlock.ULTIMATE_FORGE, () -> BlockEntityType.Builder.of(UltimateForgeBlockEntity::new, ULTIMATE_FORGE.get()).build(null));

    public static final RegistryObject<MenuType<UltimateForgeMenu>> ULTIMATE_FORGE_CONTAINER = CONTAINERS.register(UltimateForgeBlock.ULTIMATE_FORGE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new UltimateForgeMenu(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<CopperUpgradeItem> COPPER_UPGRADE = ITEMS.register("copper_upgrade", () -> new CopperUpgradeItem(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<IronUpgradeItem> IRON_UPGRADE = ITEMS.register("iron_upgrade", () -> new IronUpgradeItem(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<SteelUpgradeItem> STEEL_UPGRADE = ITEMS.register("steel_upgrade", () -> new SteelUpgradeItem(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<GoldUpgradeItem> GOLD_UPGRADE = ITEMS.register("gold_upgrade", () -> new GoldUpgradeItem(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<AmethystUpgradeItem> AMETHYST_UPGRADE = ITEMS.register("amethyst_upgrade", () -> new AmethystUpgradeItem(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<DiamondUpgradeItem> DIAMOND_UPGRADE = ITEMS.register("diamond_upgrade", () -> new DiamondUpgradeItem(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<PlatinumUpgradeItem> PLATINUM_UPGRADE = ITEMS.register("platinum_upgrade", () -> new PlatinumUpgradeItem(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<NetherhotUpgradeItem> NETHERHOT_UPGRADE = ITEMS.register("netherhot_upgrade", () -> new NetherhotUpgradeItem(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<UltimateUpgradeItem> ULTIMATE_UPGRADE = ITEMS.register("ultimate_upgrade", () -> new UltimateUpgradeItem(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<OreProcessingUpgradeItem> UORE_PROCESSING = ITEMS.register("ultimate_ore_processing_upgrade", () -> new OreProcessingUpgradeItem(new Item.Properties().tab(ModObjects.ITEM_GROUP),4,true,true));

}
