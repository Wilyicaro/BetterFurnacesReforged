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
import wily.betterfurnaces.blockentity.ForgeBlockEntity;
import wily.betterfurnaces.blocks.SmeltingBlock;
import wily.betterfurnaces.util.DirectionUtil;

public class ForgeRenderer implements BlockEntityRenderer<ForgeBlockEntity> {
    BlockEntityRendererProvider.Context context;

    public ForgeRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(ForgeBlockEntity be, float f, PoseStack stack, MultiBufferSource multiBufferSource, int i, int j) {
        BlockRenderDispatcher dispatcher = context.getBlockRenderDispatcher();
        ModelManager modelManager = dispatcher.getBlockModelShaper().getModelManager();
        BakedModel forge = be.getBlockState().getValue(SmeltingBlock.COLORED) ? modelManager.getModel(new ModelResourceLocation( new ResourceLocation( "betterfurnacesreforged:colored_forge" + (be.getBlockState().getValue(BlockStateProperties.LIT) ?"_on" : "")),"")): dispatcher.getBlockModel(be.getBlockState());
        stack.pushPose();
        stack.translate(0.5,0.5,0.5);
        stack.mulPose(DirectionUtil.getRotation(be.getBlockState().getValue(BlockStateProperties.FACING)));
        stack.translate(-0.5,-0.5,-0.5);
        dispatcher.getModelRenderer().renderModel(stack.last(), multiBufferSource.getBuffer(Sheets.cutoutBlockSheet()),be.getBlockState(),forge,be.getColor().getRed() / 255F, be.getColor().getGreen() / 255F,be.getColor().getBlue() / 255F,i,j);
        if (be.showOrientation)dispatcher.getModelRenderer().renderModel(stack.last(), multiBufferSource.getBuffer(Sheets.translucentCullBlockSheet()),be.getBlockState(),modelManager.getModel( new ModelResourceLocation( new ResourceLocation( "betterfurnacesreforged:nsweud"),"")),1F, 1F,1F,i,j);

        stack.popPose();
    }
    public int getViewDistance() {
        return 256;
    }

    @Override
    public boolean shouldRender(ForgeBlockEntity be, Vec3 vec) {
        return Vec3.atCenterOf(be.getBlockPos()).multiply(1.0, 0.0, 1.0).closerThan(vec.multiply(1.0, 0.0, 1.0), this.getViewDistance());
    }
}
