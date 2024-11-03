package com.goodbird.cnpccobblemonaddon.mixin.impl;

import com.goodbird.cnpccobblemonaddon.quest.QuestPokeTeam;
import net.minecraft.client.gui.GuiGraphics;
import noppes.npcs.client.gui.player.GuiQuestLog;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiQuestLog.class)
public class GuiQuestLogMixin extends GuiBasic {

    @Shadow(remap = false) public Quest selectedQuest;

    @Inject(method = "drawProgress", at=@At("HEAD"), remap = false)
    private void drawProgressBeg(GuiGraphics graphics, CallbackInfo ci) {
        if(selectedQuest.questInterface instanceof QuestPokeTeam){
            guiTop-=30;
        }
    }

    @Inject(method = "drawProgress", at=@At(value="INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;hLine(IIII)V", shift = At.Shift.BY, by=-10, ordinal = 1, remap = true), remap = false)
    private void drawProgressEnd(GuiGraphics graphics, CallbackInfo ci) {
        if(selectedQuest.questInterface instanceof QuestPokeTeam){
            guiTop+=30;
        }
    }
}
