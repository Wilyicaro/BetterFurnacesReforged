package wily.betterfurnaces.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.blocks.SmeltingBlock;
import wily.factoryapi.FactoryAPIClient;
import wily.factoryapi.util.DirectionUtil;

public class FurnaceRenderer implements BlockEntityRenderer<SmeltingBlockEntity> {
    BlockEntityRendererProvider.Context context;

    public FurnaceRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    public enum FurnaceOverlay {
        FURNACE("furnace"),BLAST("blast"),SMOKER("smoker"),GENERATOR("generator");
        public final ResourceLocation id;
        public final ResourceLocation activeId;
        FurnaceOverlay(String id){
            this.id = BetterFurnacesReforged.createModLocation(id+"_overlay");
            this.activeId = BetterFurnacesReforged.createModLocation(id+"_on_overlay");
        }
        public ResourceLocation getId(boolean active){
            return active ? activeId : id;
        }
    }

    public static ResourceLocation getFront(int type, boolean active){
        return FurnaceOverlay.values()[type].getId(active);
    }
    @Override
    public void render(SmeltingBlockEntity be, float f, PoseStack stack, MultiBufferSource multiBufferSource, int i, int j/*? if >=1.21.5 {*/, Vec3 vec3/*?}*/) {
        BlockRenderDispatcher dispatcher = context.getBlockRenderDispatcher();
        var furnace = be.getBlockState().getValue(SmeltingBlock.COLORED) ? FactoryAPIClient.getExtraModel(BetterFurnacesReforged.createModLocation("colored_furnace")) : dispatcher.getBlockModel(be.getBlockState());
        var front = FactoryAPIClient.getExtraModel(getFront(be.getBlockState().getValue(SmeltingBlock.TYPE),be.getBlockState().getValue(BlockStateProperties.LIT)));
        stack.pushPose();
        stack.translate(0.5,0.5,0.5);
        stack.mulPose(DirectionUtil.getHorizontalRotation(be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)));
        stack.translate(-0.5,-0.5,-0.5);
        float[] color = be.getColor();
        dispatcher.getModelRenderer().renderModel(stack.last(), multiBufferSource.getBuffer(Sheets.solidBlockSheet())/*? if <1.21.5 {*//*, be.getBlockState()*//*?}*/, furnace, color[0], color[1], color[2], i, j);
        if (be.showOrientation) dispatcher.getModelRenderer().renderModel(stack.last(), multiBufferSource.getBuffer(RenderType.cutoutMipped())/*? if <1.21.5 {*//*, be.getBlockState()*//*?}*/, FactoryAPIClient.getExtraModel(BetterFurnacesReforged.createModLocation( "nsweud")),1F, 1F,1F,i,j);
        stack.translate(0.5,0.5,0.5);
        stack.scale(1.001F,1.001F,1.001F);
        stack.translate(-0.5,-0.5,-0.5);
        dispatcher.getModelRenderer().renderModel(stack.last(), multiBufferSource.getBuffer(Sheets.cutoutBlockSheet())/*? if <1.21.5 {*//*, be.getBlockState()*//*?}*/,front,1,1,1,i,j);
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
