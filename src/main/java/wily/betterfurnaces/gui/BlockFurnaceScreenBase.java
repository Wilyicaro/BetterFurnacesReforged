package wily.betterfurnaces.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.glfw.GLFW;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.container.BlockFurnaceContainerBase;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.FactoryUpgradeItem;
import wily.betterfurnaces.network.Messages;
import wily.betterfurnaces.network.PacketSettingsButton;
import wily.betterfurnaces.network.PacketShowSettingsButton;
import wily.betterfurnaces.util.FluidRenderUtil;
import wily.betterfurnaces.util.StringHelper;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class BlockFurnaceScreenBase<T extends BlockFurnaceContainerBase> extends AbstractSmeltingScreen<T> {

    public BlockFurnaceScreenBase(T t, Inventory inv, Component name) {
        super(t, inv, name);
    }
}
