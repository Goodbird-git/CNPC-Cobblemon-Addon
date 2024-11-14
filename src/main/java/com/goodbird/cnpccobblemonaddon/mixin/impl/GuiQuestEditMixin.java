package com.goodbird.cnpccobblemonaddon.mixin.impl;

import com.goodbird.cnpccobblemonaddon.client.gui.quest.GuiQuestTypePokeCatch;
import com.goodbird.cnpccobblemonaddon.client.gui.quest.GuiQuestTypePokeKill;
import com.goodbird.cnpccobblemonaddon.client.gui.quest.GuiQuestTypePokeTeam;
import com.goodbird.cnpccobblemonaddon.constants.PokeQuestType;
import noppes.npcs.client.gui.global.GuiQuestEdit;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.shared.client.gui.components.GuiButtonBiDirectional;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.listeners.IGuiInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiQuestEdit.class)
public class GuiQuestEditMixin extends GuiNPCInterface {
    @Shadow(remap = false) private Quest quest;

    @Redirect(method = "init", at=@At(value="NEW", target = "(Lnoppes/npcs/shared/client/gui/listeners/IGuiInterface;IIIII[Ljava/lang/String;I)Lnoppes/npcs/shared/client/gui/components/GuiButtonBiDirectional;"), remap = false)
    public GuiButtonBiDirectional init(IGuiInterface gui, int id, int x, int y, int width, int height, String[] arr, int current){
        if(id==6) return new GuiButtonBiDirectional(gui,id,x,y,width,height,new String[]{"quest.item", "quest.dialog", "quest.kill", "quest.location", "quest.areakill", "quest.manual", "quest.pokekill", "quest.pokecatch", "quest.poketeam"},current);
        return new GuiButtonBiDirectional(gui,id,x,y,width,height,arr,current);
    }

    @Inject(method = "buttonEvent", at=@At("TAIL"), remap = false)
    public void buttonEvent(GuiButtonNop guibutton, CallbackInfo ci) {
        if(guibutton.id != 7) return;
        if (quest.type == PokeQuestType.POKE_DEFEAT){
            setSubGui(new GuiQuestTypePokeKill(npc, quest, wrapper.parent));
        }
        if (quest.type == PokeQuestType.POKE_CATCH){
            setSubGui(new GuiQuestTypePokeCatch(npc, quest, wrapper.parent));
        }
        if (quest.type == PokeQuestType.POKE_TEAM){
            setSubGui(new GuiQuestTypePokeTeam(npc, quest, wrapper.parent));
        }
    }
}
