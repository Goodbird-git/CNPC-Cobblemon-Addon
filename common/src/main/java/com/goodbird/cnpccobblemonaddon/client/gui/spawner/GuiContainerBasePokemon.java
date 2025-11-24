package com.goodbird.cnpccobblemonaddon.client.gui.spawner;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import noppes.npcs.shared.client.gui.components.GuiBasicContainer;

public class GuiContainerBasePokemon<T extends AbstractContainerMenu> extends GuiBasicContainer<T> {
    ResourceLocation background = new ResourceLocation("cnpccobblemonaddon", "textures/gui/pokebg.jpg");
    public GuiContainerBasePokemon(T cont, Inventory inv, Component titleIn) {
        super(cont, inv, titleIn);
        imageWidth = 414;
        imageHeight = 256;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y){
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate((float)this.guiLeft, (float)this.guiTop, 0.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.background);
        if (this.imageWidth > 256) {
            graphics.blit(this.background, 0, 0, 0, 0, 235, this.imageHeight);
            graphics.blit(this.background, 235, 0, 84, 0, 172, this.imageHeight);
        } else {
            graphics.blit(this.background, 0, 0, 0, 0, this.imageWidth, this.imageHeight);
        }

        matrixStack.popPose();
        super.renderBg(graphics, partialTicks, x, y);
        ResourceLocation slotTexture = new ResourceLocation("customnpcs", "textures/gui/slot.png");
        for(int id = 0; id < menu.slots.size(); ++id) {
            Slot slot = menu.getSlot(id);
            graphics.blit(slotTexture, this.guiLeft + slot.x - 1, this.guiTop + slot.y - 1, 0, 0, 18, 18);
        }
    }
}
