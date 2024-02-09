package wily.betterfurnaces.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.network.Messages;
import wily.betterfurnaces.network.PacketOrientationButton;
import wily.betterfurnaces.network.PacketSettingsButton;
import wily.factoryapi.base.BlockSide;
import wily.factoryapi.base.client.FactoryScreenWindow;
import wily.factoryapi.base.client.drawable.AbstractDrawableButton;
import wily.factoryapi.base.client.drawable.FactoryDrawableButton;
import wily.factoryapi.base.client.drawable.IFactoryDrawableType;
import wily.factoryapi.util.ScreenUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static wily.factoryapi.util.StorageStringUtil.isShiftKeyDown;

public class FactoryUpgradeWindow extends FactoryScreenWindow<SmeltingScreen<?>> {
    public FactoryUpgradeWindow(AbstractDrawableButton<?> button, int i, int j, SmeltingScreen<?> parent) {
        super(button, BetterFurnacesDrawables.FACTORY_UPGRADE_WINDOW.createStatic(i,j), parent);
    }

    @Override
    public List<? extends Renderable> getNestedRenderables() {
        ArrayList<Renderable> list = new ArrayList<>(nestedRenderables);
        if (parent.storedFactoryUpgradeType(1)) list.add(new FactoryDrawableButton(getX() + 6,getY() +6,BetterFurnacesDrawables.SURFACE_BUTTON).icon(BetterFurnacesDrawables.getButtonIcon(0)).tooltips(List.of(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_auto_input"), Component.translatable("options." + (parent.getMenu().getAutoInput() ? "on" : "off")))).select(parent.getMenu().getAutoInput()).onPress((b, i)-> Messages.INSTANCE.sendToServer(new PacketSettingsButton(parent.getMenu().getPos(), 6, parent.getMenu().getAutoInput() ? 0 : 1))));
        if (parent.storedFactoryUpgradeType(2)) list.add(new FactoryDrawableButton(getX() + 22,getY() + 6,BetterFurnacesDrawables.SURFACE_BUTTON).icon(BetterFurnacesDrawables.getButtonIcon(1)).tooltips(List.of(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_auto_output"), Component.translatable("options." + (parent.getMenu().getAutoOutput() ? "on" : "off")))).select(parent.getMenu().getAutoOutput()).onPress((b,i)-> Messages.INSTANCE.sendToServer(new PacketSettingsButton(parent.getMenu().getPos(), 7, parent.getMenu().getAutoOutput() ? 0 : 1))));
        if (parent.storedFactoryUpgradeType(3)) list.addAll(List.of(new FactoryDrawableButton(getX() + 38,getY() + 6,BetterFurnacesDrawables.SURFACE_BUTTON).icon(BetterFurnacesDrawables.getButtonIcon(2)).tooltips(List.of(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_show_orientation"), Component.translatable("options." + (parent.getMenu().be.showOrientation ? "on" : "off")))).select(parent.getMenu().be.showOrientation).onPress((b,i)-> Messages.INSTANCE.sendToServer(new PacketOrientationButton(parent.getMenu().getPos(), parent.getMenu().be.showOrientation = !parent.getMenu().be.showOrientation))),
                getSideButton(parent.getMenu().be.getSettingTop(),parent.getMenu().be.getIndexTop(),BlockSide.TOP),getSideButton(parent.getMenu().be.getSettingFront(),parent.getMenu().be.getIndexFront(),BlockSide.FRONT),getSideButton(parent.getMenu().be.getSettingBottom(),parent.getMenu().be.getIndexBottom(),BlockSide.BOTTOM),getSideButton(parent.getMenu().be.getSettingBack(),parent.getMenu().be.getIndexBack(),BlockSide.BACK),getSideButton(parent.getMenu().be.getSettingLeft(),parent.getMenu().be.getIndexLeft(),BlockSide.LEFT),getSideButton(parent.getMenu().be.getSettingRight(),parent.getMenu().be.getIndexRight(),BlockSide.RIGHT)));
        boolean disableSubButton = (parent.getMenu().getComSub() >= 15 && !isShiftKeyDown()) || (parent.getMenu().getComSub() == 0 && isShiftKeyDown());
        if (parent.storedFactoryUpgradeType(4)) list.addAll(List.of(
                new FactoryDrawableButton(getX() + 6,getY() + 66, BetterFurnacesDrawables.BUTTON).icon(BetterFurnacesDrawables.getButtonIcon(3)).select(parent.getMenu().be.getRedstoneSetting() == 0).tooltip(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_redstone_ignored")).onPress((b,i)-> Messages.INSTANCE.sendToServer(new PacketSettingsButton(parent.getMenu().getPos(), 8, 0))),
                new FactoryDrawableButton(getX() + 22,getY() + 66,BetterFurnacesDrawables.BUTTON).icon(BetterFurnacesDrawables.getButtonIcon(parent.getMenu().be.getRedstoneSetting() == (isShiftKeyDown() ? 1 : 2) ? 5 : 4)).select((parent.getMenu().be.getRedstoneSetting() == 1 || parent.getMenu().be.getRedstoneSetting() == 2) && !isShiftKeyDown()).tooltip(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_redstone_" + (isShiftKeyDown() && parent.getMenu().getRedstoneMode() == 1 ? "low" : "high"))).onPress((b,i)-> Messages.INSTANCE.sendToServer(new PacketSettingsButton(parent.getMenu().getPos(), 8, isShiftKeyDown() && parent.getMenu().getRedstoneMode() == 1 ? 2 : 1))),
                new FactoryDrawableButton(getX() + 38,getY() + 66,BetterFurnacesDrawables.BUTTON).icon(BetterFurnacesDrawables.getButtonIcon(7)).select(parent.getMenu().be.getRedstoneSetting() == 3).tooltip(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_redstone_comparator")).onPress((b,i)-> Messages.INSTANCE.sendToServer(new PacketSettingsButton(parent.getMenu().getPos(), 8, 3))),
                new FactoryDrawableButton(getX() + 6,getY() + 82,BetterFurnacesDrawables.BUTTON).icon(BetterFurnacesDrawables.getButtonIcon(6)).select(parent.getMenu().be.getRedstoneSetting() == 4).tooltip(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_redstone_comparator_sub")).onPress((b,i)-> Messages.INSTANCE.sendToServer(new PacketSettingsButton(parent.getMenu().getPos(), 8, 4)))));
        if (parent.getMenu().getRedstoneMode() == 4) list.add( new FactoryDrawableButton(getX() + 22,getY() + 82,BetterFurnacesDrawables.SURFACE_BUTTON).color(disableSubButton ? Color.WHITE : null).icon(BetterFurnacesDrawables.getButtonIcon( (isShiftKeyDown() ? 10 : 8) + (disableSubButton ? 1 : 0))).tooltip(Component.translatable("tooltip.betterfurnacesreforged.gui_"+ (isShiftKeyDown() ?"sub" : "add") +"_signal") ).onPress((b,i)-> {if (!disableSubButton) Messages.INSTANCE.sendToServer(new PacketSettingsButton(parent.getMenu().getPos(), 9, parent.getMenu().getComSub() + (isShiftKeyDown() ? -1 : 1)));}));
        list.addAll(nestedRenderables);
        return list;
    }
    public Component getTooltip(int index) {
        return Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_"+ (index == 1 ? "input" : index == 2 ? "output" : index == 3 ? "input_output" : index == 4 ?  "fuel" : "none"));
    }
    private IFactoryDrawableType getSideButtonDrawable(int index){
        int setting = parent.getMenu().be.furnaceSettings.get(index);
        return setting > 0 ? BetterFurnacesDrawables.getButton(setting - 1) : BetterFurnacesDrawables.BUTTON;
    }
    private int sidePositionIndex(BlockSide side){
        return List.of(-1,BlockSide.TOP,-1,BlockSide.LEFT,BlockSide.FRONT,BlockSide.RIGHT,BlockSide.BACK,BlockSide.BOTTOM,-1).indexOf(side);
    }
    private FactoryDrawableButton getSideButton(int setting, int index, BlockSide side){
        return new FactoryDrawableButton(getX() + 8 + 14* (sidePositionIndex(side) % 3),getY() + 22 + 14 * ((int)Math.ceil((double)(sidePositionIndex(side) + 1 )/ 3) - 1),getSideButtonDrawable(index)).tooltips(isShiftKeyDown() ? List.of(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".gui_reset")) : List.of(side.getComponent(),getTooltip(setting))).onPress((b, i)-> {
            if (isShiftKeyDown()) Messages.INSTANCE.sendToServer(new PacketSettingsButton(parent.getMenu().getPos(), IntStream.rangeClosed(0,5).toArray(),0));
            else Messages.INSTANCE.sendToServer(new PacketSettingsButton(parent.getMenu().getPos(), index, i == GLFW.GLFW_MOUSE_BUTTON_2 ? (setting <= 0 ? 4 : setting - 1) : (setting >= 4 ? 0 : setting + 1)));;
        });
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, int i, int j, float f) {
        super.renderBg(guiGraphics,i,j,f);
        if (parent.getMenu().getRedstoneMode() == 4)
            ScreenUtil.drawString(guiGraphics.pose(),parent.getMenu().getComSub() + "",getX() + (parent.getMenu().getComSub() > 9 ? 39 : 42),getY() + 86,0xFFFFFF,true);
    }
    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

}
