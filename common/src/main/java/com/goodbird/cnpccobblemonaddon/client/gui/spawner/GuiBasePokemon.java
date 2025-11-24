package com.goodbird.cnpccobblemonaddon.client.gui.spawner;

import com.goodbird.cnpccobblemonaddon.mixin.impl.ScreenMixin;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.IGui;

import java.util.ArrayList;
import java.util.Iterator;

public class GuiBasePokemon extends GuiBasic {
    public GuiBasePokemon(){
        background = new ResourceLocation("cnpccobblemonaddon", "textures/gui/pokebg.jpg");
        imageWidth = 414;
        imageHeight = 256;
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = graphics.pose();
        this.wrapper.mouseX = mouseX;
        this.wrapper.mouseY = mouseY;
        int x = mouseX;
        int y = mouseY;
        if (this.wrapper.subgui != null) {
            y = 0;
            x = 0;
        }

        if (this.drawDefaultBackground && this.wrapper.subgui == null) {
            this.renderBackground(graphics, mouseX, mouseY, partialTicks);
        }

        if (this.background != null) {
            matrixStack.pushPose();
            matrixStack.translate((float)this.guiLeft, (float)this.guiTop, 0.0F);
            matrixStack.scale(this.bgScale, this.bgScale, this.bgScale);
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
        }

        graphics.drawCenteredString(this.font, this.title, this.width / 2, 8, 16777215);
        Iterator var8 = (new ArrayList(this.wrapper.labels.values())).iterator();

        while(var8.hasNext()) {
            GuiLabel label = (GuiLabel)var8.next();
            label.render(graphics, mouseX, mouseY, partialTicks);
        }

        var8 = (new ArrayList(this.wrapper.textfields.values())).iterator();

        while(var8.hasNext()) {
            GuiTextFieldNop tf = (GuiTextFieldNop)var8.next();
            tf.renderWidget(graphics, x, y, partialTicks);
        }

        var8 = (new ArrayList(this.wrapper.scrolls.values())).iterator();

        while(var8.hasNext()) {
            GuiCustomScrollNop scroll = (GuiCustomScrollNop)var8.next();
            scroll.render(graphics, x, y, partialTicks);
        }

        var8 = (new ArrayList(this.wrapper.components)).iterator();

        while(var8.hasNext()) {
            IGui comp = (IGui)var8.next();
            comp.render(graphics, x, y);
        }

        var8 = (new ArrayList(this.wrapper.extra.values())).iterator();

        while(var8.hasNext()) {
            Screen gui = (Screen)var8.next();
            gui.render(graphics, x, y, partialTicks);
        }

        for(Renderable renderable : ((ScreenMixin)this).getRenderables()) {
            renderable.render(graphics, mouseX, mouseY, partialTicks);
        }
        if (this.wrapper.subgui != null) {
            matrixStack.translate(0.0F, 0.0F, 60.0F);
            this.wrapper.subgui.render(graphics, mouseX, mouseY, partialTicks);
            matrixStack.translate(0.0F, 0.0F, -60.0F);
        }

    }
}
