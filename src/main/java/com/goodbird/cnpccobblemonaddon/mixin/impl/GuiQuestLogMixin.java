package com.goodbird.cnpccobblemonaddon.mixin.impl;

import com.goodbird.cnpccobblemonaddon.quest.QuestPokeTeam;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.client.gui.player.GuiQuestLog;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonBiDirectional;
import noppes.npcs.shared.client.gui.listeners.IGuiInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiQuestLog.class)
public class GuiQuestLogMixin extends GuiBasic {

    @Shadow(remap = false) public Quest selectedQuest;

    @Inject(method = "drawProgress", at=@At("HEAD"), remap = false)
    private void drawProgressBeg(GuiGraphics graphics, CallbackInfo ci) {
        if(selectedQuest!=null && selectedQuest.questInterface instanceof QuestPokeTeam){
            guiTop-=30;
        }
    }

    @Inject(method = "drawProgress", at=@At(value="INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;hLine(IIII)V", shift = At.Shift.BY, by=-10, ordinal = 1, remap = true), remap = false)
    private void drawProgressEnd(GuiGraphics graphics, CallbackInfo ci) {
        if(selectedQuest!=null && selectedQuest.questInterface instanceof QuestPokeTeam){
            guiTop+=30;
        }
    }

    @Redirect(method = "render", at=@At(value="INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"))
    public void renderBackground(GuiGraphics instance, ResourceLocation rl, int l, int t, int dx, int dy, int w, int h){
        if(selectedQuest!=null && selectedQuest.questInterface instanceof QuestPokeTeam){
            if(dx==188)
                instance.blit(rl, l-10, t, 88, dy, w+100, h);
            else
                instance.blit(rl, l, t, dx, dy, w-10, h);
            return;
        }
        instance.blit(rl, l, t, dx, dy, w, h);
    }

}
