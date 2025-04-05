package wily.betterfurnaces.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.ForgeBlockEntity;
import wily.betterfurnaces.blocks.SmeltingBlock;
import wily.factoryapi.FactoryAPIClient;
import wily.factoryapi.util.DirectionUtil;

public class ForgeRenderer implements BlockEntityRenderer<ForgeBlockEntity> {
    BlockEntityRendererProvider.Context context;

    public ForgeRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(ForgeBlockEntity be, float f, PoseStack stack, MultiBufferSource multiBufferSource, int i, int j/*? if >=1.21.5 {*/, Vec3 vec3/*?}*/) {
        BlockRenderDispatcher dispatcher = context.getBlockRenderDispatcher();

        var model = be.getBlockState().getValue(SmeltingBlock.COLORED) ? FactoryAPIClient.getExtraModel(BetterFurnacesReforged.createModLocation("colored_forge" + (be.getBlockState().getValue(BlockStateProperties.LIT) ?"_on" : ""))): dispatcher.getBlockModel(be.getBlockState());
        stack.pushPose();
        stack.translate(0.5,0.5,0.5);
        stack.mulPose(DirectionUtil.getRotation(be.getBlockState().getValue(BlockStateProperties.FACING)));
        stack.translate(-0.5,-0.5,-0.5);
        float[] color = be.getColor();
        dispatcher.getModelRenderer().renderModel(stack.last(), multiBufferSource.getBuffer(Sheets.cutoutBlockSheet())/*? if <1.21.5 {*//*, be.getBlockState()*//*?}*/, model, color[0], color[1], color[2], i, j);
        if (be.showOrientation) dispatcher.getModelRenderer().renderModel(stack.last(), multiBufferSource.getBuffer(RenderType.cutoutMipped())/*? if <1.21.5 {*//*, be.getBlockState()*//*?}*/, FactoryAPIClient.getExtraModel(BetterFurnacesReforged.createModLocation("nsweud")),1F, 1F,1F,i,j);

        stack.popPose();
    }
    public int getViewDistance() {
        return 256;
    }

    @Override
    public boolean shouldRender(ForgeBlockEntity be, Vec3 vec) {
        return be.getBlockPos().getCenter().multiply(1.0, 0.0, 1.0).closerThan(vec.multiply(1.0, 0.0, 1.0), this.getViewDistance());
    }
}
