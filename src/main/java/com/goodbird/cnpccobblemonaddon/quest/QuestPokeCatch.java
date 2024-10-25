package com.goodbird.cnpccobblemonaddon.quest;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.goodbird.cnpccobblemonaddon.constants.PokeQuestType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.NBTTags;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.constants.QuestType;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerQuestData;
import noppes.npcs.controllers.data.QuestData;
import noppes.npcs.quests.QuestInterface;

import java.util.*;

public class QuestPokeCatch extends QuestInterface {
    public TreeMap<String,Integer> targets = new TreeMap<>();

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        targets = new TreeMap(NBTTags.getStringIntegerMap(compound.getList("QuestCatchTargets", 10)));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.put("QuestCatchTargets", NBTTags.nbtStringIntegerMap(targets));
    }

    @Override
    public boolean isCompleted(Player player) {
        PlayerQuestData playerdata = PlayerData.get(player).questData;
        QuestData data = playerdata.activeQuests.get(questId);
        if(data == null)
            return false;
        HashMap<String,Integer> killed = getCaught(data);
        if(killed.size() != targets.size())
            return false;
        for(String entity : killed.keySet()){
            if(!targets.containsKey(entity) || targets.get(entity) > killed.get(entity))
                return false;
        }

        return true;
    }

    @Override
    public void handleComplete(Player player) {
    }

    public HashMap<String, Integer> getCaught(QuestData data) {
        return NBTTags.getStringIntegerMap(data.extraData.getList("Caught", 10));
    }
    public void setCaught(QuestData data, HashMap<String, Integer> killed) {
        data.extraData.put("Caught", NBTTags.nbtStringIntegerMap(killed));
    }

    @Override
    public IQuestObjective[] getObjectives(Player player) {
        List<IQuestObjective> list = new ArrayList<>();
        for(Map.Entry<String,Integer> entry : targets.entrySet()){
            list.add(new QuestPokeCatchObjective(player, entry.getKey(), entry.getValue()));
        }
        return list.toArray(new IQuestObjective[list.size()]);
    }

    class QuestPokeCatchObjective implements IQuestObjective{
        private final Player player;
        private final String type;
        private final int amount;
        public QuestPokeCatchObjective(Player player, String type, int amount) {
            this.player = player;
            this.type = type;
            this.amount = amount;
        }

        @Override
        public int getProgress() {
            PlayerData data = PlayerData.get(player);
            PlayerQuestData playerdata = data.questData;
            QuestData questdata = playerdata.activeQuests.get(questId);
            HashMap<String,Integer> caught = getCaught(questdata);
            if(!caught.containsKey(type))
                return 0;
            return caught.get(type);
        }

        @Override
        public void setProgress(int progress) {
            if(progress < 0 || progress > amount) {
                throw new CustomNPCsException("Progress has to be between 0 and " + amount);
            }
            PlayerData data = PlayerData.get(player);
            PlayerQuestData playerdata = data.questData;
            QuestData questdata = playerdata.activeQuests.get(questId);
            HashMap<String,Integer> caught = getCaught(questdata);

            if(caught.containsKey(type) && caught.get(type) == progress) {
                return;
            }
            caught.put(type, progress);
            setCaught(questdata, caught);
            data.questData.checkQuestCompletion(player, PokeQuestType.POKE_CATCH);
            data.updateClient = true;
        }

        @Override
        public int getMaxProgress() {
            return amount;
        }

        @Override
        public boolean isCompleted() {
            return getProgress() >= amount;
        }

        @Override
        public String getText() {
            return getMCText().getString();
        }

        @Override
        public Component getMCText() {
            if(PokemonSpecies.INSTANCE.getByIdentifier(new ResourceLocation(type))!=null) {
                return PokemonSpecies.INSTANCE.getByIdentifier(new ResourceLocation(type)).getTranslatedName().append(": " + getProgress() + "/" + getMaxProgress());
            }
            return Component.translatable(type).append(": " + getProgress() + "/" + getMaxProgress());
        }
    }

}
