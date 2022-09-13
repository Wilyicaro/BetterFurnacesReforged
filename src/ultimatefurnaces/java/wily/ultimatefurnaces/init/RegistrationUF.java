package wily.ultimatefurnaces.init;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import wily.betterfurnaces.items.OreProcessingUpgradeItem;
import wily.ultimatefurnaces.UltimateFurnaces;
import wily.ultimatefurnaces.blocks.*;
import wily.ultimatefurnaces.inventory.*;
import wily.ultimatefurnaces.items.*;
import wily.ultimatefurnaces.tileentity.*;


public class RegistrationUF {

    private static final DeferredRegister<Block> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.BLOCKS, UltimateFurnaces.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UltimateFurnaces.MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, UltimateFurnaces.MOD_ID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, UltimateFurnaces.MOD_ID);

    public static void init() {
        BLOCK_ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    @SubscribeEvent
    public static void registerBlockItems(final RegistryEvent.Register<Item> a){
        for (RegistryObject<Block> block : BLOCK_ITEMS.getEntries()){
            a.getRegistry().register(new BlockItem(block.get(), defaultItemProperties()).setRegistryName(block.getId()));
        }

    }

    private static Item.Properties defaultItemProperties(){ return new Item.Properties().tab(ModObjects.ITEM_GROUP);}
    public static final RegistryObject<CopperFurnaceBlock> COPPER_FURNACE = BLOCK_ITEMS.register(CopperFurnaceBlock.COPPER_FURNACE, () -> new CopperFurnaceBlock(AbstractBlock.Properties.copy(Blocks.COAL_BLOCK).strength(4.0F, 20.0F)));

    public static final RegistryObject<TileEntityType<CopperFurnaceTileEntity>> COPPER_FURNACE_TILE = TILES.register(CopperFurnaceBlock.COPPER_FURNACE, () -> TileEntityType.Builder.of(CopperFurnaceTileEntity::new, COPPER_FURNACE.get()).build(null));

    public static final RegistryObject<ContainerType<CopperFurnaceContainer>> COPPER_FURNACE_CONTAINER = CONTAINERS.register(CopperFurnaceBlock.COPPER_FURNACE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new CopperFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));


    public static final RegistryObject<SteelFurnaceBlock> STEEL_FURNACE = BLOCK_ITEMS.register(SteelFurnaceBlock.STEEL_FURNACE, () -> new SteelFurnaceBlock(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK).strength(8.0F, 30.0F)));
    public static final RegistryObject<TileEntityType<SteelFurnaceTileEntity>> STEEL_FURNACE_TILE = TILES.register(SteelFurnaceBlock.STEEL_FURNACE, () -> TileEntityType.Builder.of(SteelFurnaceTileEntity::new, STEEL_FURNACE.get()).build(null));

    public static final RegistryObject<ContainerType<SteelFurnaceContainer>> STEEL_FURNACE_CONTAINER = CONTAINERS.register(SteelFurnaceBlock.STEEL_FURNACE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new SteelFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<AmethystFurnaceBlock> AMETHYST_FURNACE = BLOCK_ITEMS.register(AmethystFurnaceBlock.AMETHYST_FURNACE, () -> new AmethystFurnaceBlock(AbstractBlock.Properties.copy(Blocks.END_GATEWAY).strength(6.0F, 20.0F)));
    public static final RegistryObject<TileEntityType<AmethystFurnaceTileEntity>> AMETHYST_FURNACE_TILE = TILES.register(AmethystFurnaceBlock.AMETHYST_FURNACE, () -> TileEntityType.Builder.of(AmethystFurnaceTileEntity::new, AMETHYST_FURNACE.get()).build(null));

    public static final RegistryObject<ContainerType<AmethystFurnaceContainer>> AMETHYST_FURNACE_CONTAINER = CONTAINERS.register(AmethystFurnaceBlock.AMETHYST_FURNACE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new AmethystFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<PlatinumFurnaceBlock> PLATINUM_FURNACE = BLOCK_ITEMS.register(PlatinumFurnaceBlock.PLATINUM_FURNACE, () -> new PlatinumFurnaceBlock(AbstractBlock.Properties.copy(Blocks.GOLD_BLOCK).strength(10.0F, 35.0F)));
    public static final RegistryObject<TileEntityType<PlatinumFurnaceTileEntity>> PLATINUM_FURNACE_TILE = TILES.register(PlatinumFurnaceBlock.PLATINUM_FURNACE, () -> TileEntityType.Builder.of(PlatinumFurnaceTileEntity::new, PLATINUM_FURNACE.get()).build(null));

    public static final RegistryObject<ContainerType<PlatinumFurnaceContainer>> PLATINUM_FURNACE_CONTAINER = CONTAINERS.register(PlatinumFurnaceBlock.PLATINUM_FURNACE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new PlatinumFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockUltimateFurnace> ULTIMATE_FURNACE = BLOCK_ITEMS.register(BlockUltimateFurnace.ULTIMATE_FURNACE, () -> new BlockUltimateFurnace(AbstractBlock.Properties.copy(Blocks.NETHERITE_BLOCK).strength(15.0F, 6000.0F)));
    public static final RegistryObject<TileEntityType<UltimateFurnaceTileEntity>> ULTIMATE_FURNACE_TILE = TILES.register(BlockUltimateFurnace.ULTIMATE_FURNACE, () -> TileEntityType.Builder.of(UltimateFurnaceTileEntity::new, ULTIMATE_FURNACE.get()).build(null));

    public static final RegistryObject<ContainerType<UltimateFurnaceContainer>> ULTIMATE_FURNACE_CONTAINER = CONTAINERS.register(BlockUltimateFurnace.ULTIMATE_FURNACE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new UltimateFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<CopperForgeBlock> COPPER_FORGE = BLOCK_ITEMS.register(CopperForgeBlock.COPPER_FORGE, () -> new CopperForgeBlock(AbstractBlock.Properties.copy(Blocks.COAL_BLOCK)));
    public static final RegistryObject<TileEntityType<CopperForgeTileEntity>> COPPER_FORGE_TILE = TILES.register(CopperForgeBlock.COPPER_FORGE, () -> TileEntityType.Builder.of(CopperForgeTileEntity::new, COPPER_FORGE.get()).build(null));

    public static final RegistryObject<ContainerType<CopperForgeContainer>> COPPER_FORGE_CONTAINER = CONTAINERS.register(CopperForgeBlock.COPPER_FORGE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new CopperForgeContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<IronForgeBlock> IRON_FORGE = BLOCK_ITEMS.register(IronForgeBlock.IRON_FORGE, () -> new IronForgeBlock(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<TileEntityType<IronForgeTileEntity>> IRON_FORGE_TILE = TILES.register(IronForgeBlock.IRON_FORGE, () -> TileEntityType.Builder.of(IronForgeTileEntity::new, IRON_FORGE.get()).build(null));

    public static final RegistryObject<ContainerType<IronForgeContainer>> IRON_FORGE_CONTAINER = CONTAINERS.register(IronForgeBlock.IRON_FORGE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new IronForgeContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<GoldForgeBlock> GOLD_FORGE = BLOCK_ITEMS.register(GoldForgeBlock.GOLD_FORGE, () -> new GoldForgeBlock(AbstractBlock.Properties.copy(Blocks.GOLD_BLOCK)));
    public static final RegistryObject<TileEntityType<GoldForgeTileEntity>> GOLD_FORGE_TILE = TILES.register(GoldForgeBlock.GOLD_FORGE, () -> TileEntityType.Builder.of(GoldForgeTileEntity::new, GOLD_FORGE.get()).build(null));

    public static final RegistryObject<ContainerType<GoldForgeContainer>> GOLD_FORGE_CONTAINER = CONTAINERS.register(GoldForgeBlock.GOLD_FORGE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new GoldForgeContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<DiamondForgeBlock> DIAMOND_FORGE = BLOCK_ITEMS.register(DiamondForgeBlock.DIAMOND_FORGE, () -> new DiamondForgeBlock(AbstractBlock.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<TileEntityType<DiamondForgeTileEntity>> DIAMOND_FORGE_TILE = TILES.register(DiamondForgeBlock.DIAMOND_FORGE, () -> TileEntityType.Builder.of(DiamondForgeTileEntity::new, DIAMOND_FORGE.get()).build(null));

    public static final RegistryObject<ContainerType<DiamondForgeContainer>> DIAMOND_FORGE_CONTAINER = CONTAINERS.register(DiamondForgeBlock.DIAMOND_FORGE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new DiamondForgeContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<NetherhotForgeBlock> NETHERHOT_FORGE = BLOCK_ITEMS.register(NetherhotForgeBlock.NETHERHOT_FORGE, () -> new NetherhotForgeBlock(AbstractBlock.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<TileEntityType<NetherhotForgeTileEntity>> NETHERHOT_FORGE_TILE = TILES.register(NetherhotForgeBlock.NETHERHOT_FORGE, () -> TileEntityType.Builder.of(NetherhotForgeTileEntity::new, NETHERHOT_FORGE.get()).build(null));

    public static final RegistryObject<ContainerType<NetherhotForgeContainer>> NETHERHOT_FORGE_CONTAINER = CONTAINERS.register(NetherhotForgeBlock.NETHERHOT_FORGE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new NetherhotForgeContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<UltimateForgeBlock> ULTIMATE_FORGE = BLOCK_ITEMS.register(UltimateForgeBlock.ULTIMATE_FORGE, () -> new UltimateForgeBlock(AbstractBlock.Properties.copy(Blocks.NETHERITE_BLOCK).strength(50.0F, 6000.0F)));
    public static final RegistryObject<TileEntityType<UltimateForgeTileEntity>> ULTIMATE_FORGE_TILE = TILES.register(UltimateForgeBlock.ULTIMATE_FORGE, () -> TileEntityType.Builder.of(UltimateForgeTileEntity::new, ULTIMATE_FORGE.get()).build(null));

    public static final RegistryObject<ContainerType<UltimateForgeContainer>> ULTIMATE_FORGE_CONTAINER = CONTAINERS.register(UltimateForgeBlock.ULTIMATE_FORGE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new UltimateForgeContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<wily.ultimatefurnaces.items.CopperUpgradeItem> COPPER_UPGRADE = ITEMS.register("copper_upgrade", () -> new CopperUpgradeItem(defaultItemProperties()));

    public static final RegistryObject<wily.ultimatefurnaces.items.IronUpgradeItem> IRON_UPGRADE = ITEMS.register("iron_upgrade", () -> new IronUpgradeItem(defaultItemProperties()));

    public static final RegistryObject<SteelUpgradeItem> STEEL_UPGRADE = ITEMS.register("steel_upgrade", () -> new SteelUpgradeItem(defaultItemProperties()));

    public static final RegistryObject<wily.ultimatefurnaces.items.GoldUpgradeItem> GOLD_UPGRADE = ITEMS.register("gold_upgrade", () -> new GoldUpgradeItem(defaultItemProperties()));

    public static final RegistryObject<wily.ultimatefurnaces.items.AmethystUpgradeItem> AMETHYST_UPGRADE = ITEMS.register("amethyst_upgrade", () -> new AmethystUpgradeItem(defaultItemProperties()));

    public static final RegistryObject<wily.ultimatefurnaces.items.DiamondUpgradeItem> DIAMOND_UPGRADE = ITEMS.register("diamond_upgrade", () -> new DiamondUpgradeItem(defaultItemProperties()));

    public static final RegistryObject<wily.ultimatefurnaces.items.PlatinumUpgradeItem> PLATINUM_UPGRADE = ITEMS.register("platinum_upgrade", () -> new PlatinumUpgradeItem(defaultItemProperties()));

    public static final RegistryObject<wily.ultimatefurnaces.items.NetherhotUpgradeItem> NETHERHOT_UPGRADE = ITEMS.register("netherhot_upgrade", () -> new NetherhotUpgradeItem(defaultItemProperties()));

    public static final RegistryObject<wily.ultimatefurnaces.items.UltimateUpgradeItem> ULTIMATE_UPGRADE = ITEMS.register("ultimate_upgrade", () -> new UltimateUpgradeItem(defaultItemProperties()));

    public static final RegistryObject<OreProcessingUpgradeItem> UORE = ITEMS.register("ultimate_ore_processing_upgrade", () -> new OreProcessingUpgradeItem(defaultItemProperties(),4));

}
