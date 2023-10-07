package wily.betterfurnaces.client.screen;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import wily.betterfurnaces.client.ItemColorsHandler;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ColorUpgradeItem.ContainerColorUpgrade;
import wily.betterfurnaces.network.Messages;
import wily.betterfurnaces.network.PacketColorSlider;
import wily.factoryapi.base.client.drawable.FactoryDrawableButton;
import wily.factoryapi.base.client.drawable.FactoryDrawableSlider;

import java.util.ArrayList;
import java.util.List;


public class ColorUpgradeScreen extends AbstractUpgradeScreen<ContainerColorUpgrade> {
    private int buttonstate = 0;
    public FactoryDrawableSlider red;
    public FactoryDrawableSlider green;
    public FactoryDrawableSlider blue;

    public ColorUpgradeScreen(ContainerColorUpgrade container, Inventory inv, Component name) {
        super(container, inv, name);
    }
    @Override
    protected void init() {
        super.init();
        CompoundTag nbt = this.getMenu().itemStackBeingHeld.getOrCreateTag();
        red = addNestedWidget(new FactoryDrawableSlider(leftPos + 3,topPos + 24, i->Component.translatable("gui.betterfurnacesreforged.color.red").append(""+i.value),BetterFurnacesDrawables.VANILLA_BUTTON,BetterFurnacesDrawables.VANILLA_BUTTON_BACKGROUND,6,imageWidth - 6,nbt.getInt("red"),255).onPress((s, b)-> sliderPacket(s,1)));
        green = addNestedWidget(new FactoryDrawableSlider(leftPos + 3,topPos + 46,i->Component.translatable("gui.betterfurnacesreforged.color.green").append(""+i.value),BetterFurnacesDrawables.VANILLA_BUTTON,BetterFurnacesDrawables.VANILLA_BUTTON_BACKGROUND,6,imageWidth - 6,nbt.getInt("green"),255).onPress((s,b)-> sliderPacket(s,2)));
        blue = addNestedWidget(new FactoryDrawableSlider(leftPos + 3,topPos + 68,i->Component.translatable("gui.betterfurnacesreforged.color.blue").append(""+i.value),BetterFurnacesDrawables.VANILLA_BUTTON,BetterFurnacesDrawables.VANILLA_BUTTON_BACKGROUND,6,imageWidth - 6,nbt.getInt("blue"),255).onPress((s,b)-> sliderPacket(s,3)));
    }
    protected void sliderPacket(FactoryDrawableSlider slider, int diff){
        Messages.INSTANCE.sendToServer(new PacketColorSlider(slider.getValue(), diff));
    }
    @Override
    public List<Widget> getNestedWidgets() {
        List<Widget> list = new ArrayList<>(ImmutableList.of(new FactoryDrawableButton(leftPos + 8,topPos + 8, BetterFurnacesDrawables.BUTTON).icon(BetterFurnacesDrawables.getButtonIcon(12 + buttonstate)).tooltip((buttonstate == 0 ?Blocks.FURNACE : Registration.EXTREME_FORGE.get()).getName()).onPress((b, i)-> buttonstate =(buttonstate == 1 ? 0 : 1))));
        list.addAll(nestedWidgets);
        return list;
    }

    @Override
    protected void renderColorFurnace(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
        ItemStack stack = buttonstate == 0 ? new ItemStack(Registration.EXTREME_FURNACE.get().asItem()) : new ItemStack(Registration.EXTREME_FORGE.get().asItem());
        Lighting.setupFor3DItems();
        CompoundTag tag = stack.getOrCreateTag();
        ItemColorsHandler.putColor(tag,red.getValue(),green.getValue(),blue.getValue());
        stack.setTag(tag);
        renderGuiItem(matrix,stack, (width / 2 - 8), (this.topPos - 48), 4,4);
    }

}
