package wily.betterfurnaces.blocks;


import wily.betterfurnaces.BetterFurnacesReforged;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class BlockConductor extends Block {



    public BlockConductor(String name) {
        super(Material.IRON);
        this.setTranslationKey(BetterFurnacesReforged.MODID + "." + name);
        this.setRegistryName(BetterFurnacesReforged.MODID, name);
        this.setCreativeTab(BetterFurnacesReforged.BF_TAB);
        this.setHardness(10.0F);
        this.setResistance(8.0F);
        this.setHarvestLevel("pickaxe", 1);
        this.setLightOpacity(0);
        this.setDefaultState(this.blockState.getBaseState());
    }


    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

}
