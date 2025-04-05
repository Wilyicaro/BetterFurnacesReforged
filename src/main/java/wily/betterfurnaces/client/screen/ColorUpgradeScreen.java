package wily.betterfurnaces.client.screen;

import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.items.ColorUpgradeItem;import wily.betterfurnaces.items.ColorUpgradeItem.ColorUpgradeMenu;
import wily.betterfurnaces.network.SliderColorSyncPayload;
import wily.factoryapi.base.client.drawable.FactoryDrawableButton;
import wily.factoryapi.base.client.drawable.FactoryDrawableSlider;
import wily.factoryapi.base.network.CommonNetwork;


public class ColorUpgradeScreen extends AbstractUpgradeScreen<ColorUpgradeMenu> {
    private int buttonState = 0;
    protected final FactoryDrawableSlider redSlider = createChannelSlider(SliderColorSyncPayload.Channel.R);
    protected final FactoryDrawableSlider greenSlider = createChannelSlider(SliderColorSyncPayload.Channel.G);
    protected final FactoryDrawableSlider blueSlider = createChannelSlider(SliderColorSyncPayload.Channel.B);
    private ItemStack displayStack = ItemStack.EMPTY;

    public ColorUpgradeScreen(ColorUpgradeMenu container, Inventory inv, Component name) {
        super(container, inv, name);
    }

    @Override
    protected void init() {
        super.init();
        addNestedRenderable(redSlider).setPosition(leftPos + 5,topPos + 24);
        addNestedRenderable(greenSlider).setPosition(leftPos + 5,topPos + 46);
        addNestedRenderable(blueSlider).setPosition(leftPos + 5,topPos + 68);
        updateDisplayStack();
        addNestedRenderable(new FactoryDrawableButton(leftPos + 8,topPos + 8, BetterFurnacesDrawables.BUTTON).icon(BetterFurnacesDrawables.getButtonIcon(12 + buttonState)).tooltip((buttonState == 0 ? Blocks.FURNACE : ModObjects.EXTREME_FORGE.get()).getName()).onPress((b, i)-> {
            buttonState = (buttonState == 1 ? 0 : 1);
            updateDisplayStack();
            b.clearTooltips().tooltip((buttonState == 0 ? Blocks.FURNACE : ModObjects.EXTREME_FORGE.get()).getName()).icon(BetterFurnacesDrawables.getButtonIcon(12 + buttonState));
        }));
    }

    protected FactoryDrawableSlider createChannelSlider(SliderColorSyncPayload.Channel channel){
        return new FactoryDrawableSlider(0,0, i->Component.translatable("gui.betterfurnacesreforged.color." + channel.id).append(""+i.value),BetterFurnacesDrawables.VANILLA_BUTTON,BetterFurnacesDrawables.VANILLA_BUTTON_BACKGROUND,8,imageWidth - 10, /*? if <1.20.5 {*//*menu.itemStackBeingHeld.getOrCreateTag().getInt(channel.id)*//*?} else {*/menu.itemStackBeingHeld.getOrDefault(ModObjects.BLOCK_TINT.get(), ModObjects.BlockTint.WHITE).getChannel(channel)/*?}*/,255).grave(1).onPress((s, b)-> onSliderChange(s, channel));
    }

    protected void onSliderChange(FactoryDrawableSlider slider, SliderColorSyncPayload.Channel channel){
        updateDisplayStack();
        CommonNetwork.sendToServer(new SliderColorSyncPayload(channel, slider.getValue()));
    }

    protected void updateDisplayStack(){
        displayStack = new ItemStack(buttonState == 0 ? ModObjects.EXTREME_FURNACE.get() : ModObjects.EXTREME_FORGE.get().asItem());
        //? if <1.20.5 {
        /*CompoundTag tag = displayStack.getOrCreateTag();
        ColorUpgradeItem.putColor(tag, redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue());
        *///?} else {
        displayStack.set(ModObjects.BLOCK_TINT.get(), new ModObjects.BlockTint(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()));
        //?}
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        renderColorFurnace(graphics);
    }

    protected void renderColorFurnace(GuiGraphics graphics) {
        Lighting.setupFor3DItems();
        graphics.pose().pushPose();
        graphics.pose().translate(((float) width / 2 ), (this.topPos - 40),0);
        graphics.pose().scale(4F,4F,1F);
        graphics.pose().translate(-8, -8,0);
        graphics.renderItem(displayStack, 0, 0);
        graphics.pose().popPose();
    }

}
