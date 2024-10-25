package com.goodbird.cnpccobblemonaddon.mixin.impl;

import com.goodbird.cnpccobblemonaddon.constants.PokeQuestType;
import com.goodbird.cnpccobblemonaddon.quest.QuestPokeCatch;
import com.goodbird.cnpccobblemonaddon.quest.QuestPokeKill;
import noppes.npcs.api.constants.QuestType;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.quests.QuestInterface;
import noppes.npcs.quests.QuestItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Quest.class)
public class QuestMixin {
    @Shadow(remap = false) public int type;

    @Shadow(remap = false) public QuestInterface questInterface;

    @Inject(method = "setType", at=@At("HEAD"), remap = false)
    public void setType(int questType, CallbackInfo ci) {
        type = questType;
        if(type == PokeQuestType.POKE_DEFEAT)
            questInterface = new QuestPokeKill();
        if(type == PokeQuestType.POKE_CATCH)
            questInterface = new QuestPokeCatch();
    }
}
