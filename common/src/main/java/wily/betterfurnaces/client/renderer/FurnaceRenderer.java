package wily.betterfurnaces.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.blocks.SmeltingBlock;
import wily.factoryapi.util.DirectionUtil;

public class FurnaceRenderer implements BlockEntityRenderer<SmeltingBlockEntity> {
    BlockEntityRendererProvider.Context context;

    public FurnaceRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    public static ModelResourceLocation getFront(int type, boolean active){
        return new ModelResourceLocation(new ResourceLocation("betterfurnacesreforged:" + (type == 3 ? "generator" : type == 1 ? "blast" : type == 2 ? "smoker" : "furnace") + (active ? "_on":"") +"_overlay"),   "");
    }
    @Override
    public void render(SmeltingBlockEntity be, float f, PoseStack stack, MultiBufferSource multiBufferSource, int i, int j) {
        BlockRenderDispatcher dispatcher = context.getBlockRenderDispatcher();
        ModelManager modelManager = dispatcher.getBlockModelShaper().getModelManager();
        BakedModel furnace = be.getBlockState().getValue(SmeltingBlock.COLORED) ? modelManager.getModel(new ModelResourceLocation( new ResourceLocation( "betterfurnacesreforged:colored_furnace"),"")): dispatcher.getBlockModel(be.getBlockState());
        BakedModel front = modelManager.getModel(getFront(be.getBlockState().getValue(SmeltingBlock.TYPE),be.getBlockState().getValue(BlockStateProperties.LIT)));
        stack.pushPose();
        stack.translate(0.5,0.5,0.5);
        stack.mulPose(DirectionUtil.getHorizontalRotation(be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)));
        stack.translate(-0.5,-0.5,-0.5);
        dispatcher.getModelRenderer().renderModel(stack.last(), multiBufferSource.getBuffer(Sheets.solidBlockSheet()),be.getBlockState(),furnace,be.getColor().getRed() / 255F, be.getColor().getGreen() / 255F,be.getColor().getBlue() / 255F,i,j);
        if (be.showOrientation)dispatcher.getModelRenderer().renderModel(stack.last(), multiBufferSource.getBuffer(Sheets.translucentCullBlockSheet()),be.getBlockState(),modelManager.getModel( new ModelResourceLocation( new ResourceLocation( "betterfurnacesreforged:nsweud"),"")),1F, 1F,1F,i,j);
        stack.translate(0.5,0.5,0.5);
        stack.scale(1.001F,1.001F,1.001F);
        stack.translate(-0.5,-0.5,-0.5);
        dispatcher.getModelRenderer().renderModel(stack.last(), multiBufferSource.getBuffer(Sheets.cutoutBlockSheet()),be.getBlockState(),front,1,1,1,i,j);
        stack.popPose();
    }
    public int getViewDistance() {
        return 256;
    }

    @Override
    public boolean shouldRender(SmeltingBlockEntity be, Vec3 vec) {
        return Vec3.atCenterOf(be.getBlockPos()).multiply(1.0, 0.0, 1.0).closerThan(vec.multiply(1.0, 0.0, 1.0), this.getViewDistance());
    }

}
