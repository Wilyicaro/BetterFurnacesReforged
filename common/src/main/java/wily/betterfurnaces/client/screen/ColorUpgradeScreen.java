package wily.betterfurnaces.client.screen;

import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.client.ItemColorsHandler;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.items.ColorUpgradeItem.ContainerColorUpgrade;
import wily.betterfurnaces.network.Messages;
import wily.betterfurnaces.network.PacketColorSlider;
import wily.factoryapi.base.client.drawable.FactoryDrawableButton;
import wily.factoryapi.base.client.drawable.FactoryDrawableSlider;

import java.util.ArrayList;
import java.util.List;


public class ColorUpgradeScreen extends AbstractUpgradeScreen<ContainerColorUpgrade> {
    public static final ResourceLocation WIDGETS = new ResourceLocation(BetterFurnacesReforged.MOD_ID + ":" + "textures/container/widgets.png");
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
        red = addNestedRenderable(new FactoryDrawableSlider(leftPos + 3,topPos + 24, i->Component.translatable("gui.betterfurnacesreforged.color.red").append(""+i.value),BetterFurnacesDrawables.VANILLA_BUTTON,BetterFurnacesDrawables.VANILLA_BUTTON_BACKGROUND,6,imageWidth - 6,nbt.getInt("red"),255).onPress((s, b)-> sliderPacket(s,1)));
        green = addNestedRenderable(new FactoryDrawableSlider(leftPos + 3,topPos + 46,i->Component.translatable("gui.betterfurnacesreforged.color.green").append(""+i.value),BetterFurnacesDrawables.VANILLA_BUTTON,BetterFurnacesDrawables.VANILLA_BUTTON_BACKGROUND,6,imageWidth - 6,nbt.getInt("green"),255).onPress((s,b)-> sliderPacket(s,2)));
        blue = addNestedRenderable(new FactoryDrawableSlider(leftPos + 3,topPos + 68,i->Component.translatable("gui.betterfurnacesreforged.color.blue").append(""+i.value),BetterFurnacesDrawables.VANILLA_BUTTON,BetterFurnacesDrawables.VANILLA_BUTTON_BACKGROUND,6,imageWidth - 6,nbt.getInt("blue"),255).onPress((s,b)-> sliderPacket(s,3)));
    }

    protected void sliderPacket(FactoryDrawableSlider slider, int diff){
        Messages.INSTANCE.sendToServer(new PacketColorSlider(slider.getValue(), diff));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        renderColorFurnace(graphics);
    }

    @Override
    public List<? extends Renderable> getNestedRenderables() {
        List<Renderable> list = new ArrayList<>(List.of(new FactoryDrawableButton(leftPos + 8,topPos + 8, BetterFurnacesDrawables.BUTTON).icon(BetterFurnacesDrawables.getButtonIcon(12 + buttonstate)).tooltip((buttonstate == 0 ?Blocks.FURNACE : ModObjects.EXTREME_FORGE.get()).getName()).onPress((b, i)-> buttonstate =(buttonstate == 1 ? 0 : 1))));
        list.addAll(super.getNestedRenderables());
        return list;
    }

    protected void renderColorFurnace(GuiGraphics graphics) {
        ItemStack stack =  new ItemStack(buttonstate == 0 ? ModObjects.EXTREME_FURNACE.get() : ModObjects.EXTREME_FORGE.get().asItem());
        Lighting.setupFor3DItems();
        CompoundTag tag = stack.getOrCreateTag();
        ItemColorsHandler.putColor(tag,red.getValue(),green.getValue(),blue.getValue());
        stack.setTag(tag);
        graphics.pose().pushPose();
        graphics.pose().translate(((float) width / 2 ), (this.topPos - 40),0);
        graphics.pose().scale(4F,4F,1F);
        graphics.pose().translate(-8, -8,0);
        graphics.renderItem(stack, 0, 0);
        graphics.pose().popPose();
    }

}
