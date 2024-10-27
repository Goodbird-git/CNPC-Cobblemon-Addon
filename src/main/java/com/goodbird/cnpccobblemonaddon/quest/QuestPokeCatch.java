package com.goodbird.cnpccobblemonaddon.quest;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import com.goodbird.cnpccobblemonaddon.constants.PokeQuestType;
import com.goodbird.cnpccobblemonaddon.util.NBTUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerQuestData;
import noppes.npcs.controllers.data.QuestData;
import noppes.npcs.quests.QuestInterface;

import java.util.*;

public class QuestPokeCatch extends QuestInterface {
    public TreeMap<PokemonEntry,Integer> targets = new TreeMap<>();

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        targets = new TreeMap(NBTUtils.getNBTIntegerMap(PokemonEntry.class, compound.getList("QuestCatchTargets", 10)));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.put("QuestCatchTargets", NBTUtils.nbtNBTIntegerMap(targets));
    }

    @Override
    public boolean isCompleted(Player player) {
        PlayerQuestData playerdata = PlayerData.get(player).questData;
        QuestData data = playerdata.activeQuests.get(questId);
        if(data == null)
            return false;
        HashMap<PokemonEntry,Integer> killed = getCaught(data);
        if(killed.size() != targets.size())
            return false;
        for(PokemonEntry entity : killed.keySet()){
            if(!targets.containsKey(entity) || targets.get(entity) > killed.get(entity))
                return false;
        }

        return true;
    }

    @Override
    public void handleComplete(Player player) {
    }

    public HashMap<PokemonEntry, Integer> getCaught(QuestData data) {
        return NBTUtils.getNBTIntegerMap(PokemonEntry.class, data.extraData.getList("Caught", 10));
    }
    public void setCaught(QuestData data, HashMap<PokemonEntry, Integer> killed) {
        data.extraData.put("Caught", NBTUtils.nbtNBTIntegerMap(killed));
    }

    @Override
    public IQuestObjective[] getObjectives(Player player) {
        List<IQuestObjective> list = new ArrayList<>();
        for(Map.Entry<PokemonEntry,Integer> entry : targets.entrySet()){
            list.add(new QuestPokeCatchObjective(player, entry.getKey(), entry.getValue()));
        }
        return list.toArray(new IQuestObjective[list.size()]);
    }

    class QuestPokeCatchObjective implements IQuestObjective{
        private final Player player;
        private final PokemonEntry pokemonEntry;
        private final int amount;
        public QuestPokeCatchObjective(Player player, PokemonEntry pokemonEntry, int amount) {
            this.player = player;
            this.pokemonEntry = pokemonEntry;
            this.amount = amount;
        }

        @Override
        public int getProgress() {
            PlayerData data = PlayerData.get(player);
            PlayerQuestData playerdata = data.questData;
            QuestData questdata = playerdata.activeQuests.get(questId);
            HashMap<PokemonEntry,Integer> caught = getCaught(questdata);
            if(!caught.containsKey(pokemonEntry))
                return 0;
            return caught.get(pokemonEntry);
        }

        @Override
        public void setProgress(int progress) {
            if(progress < 0 || progress > amount) {
                throw new CustomNPCsException("Progress has to be between 0 and " + amount);
            }
            PlayerData data = PlayerData.get(player);
            PlayerQuestData playerdata = data.questData;
            QuestData questdata = playerdata.activeQuests.get(questId);
            HashMap<PokemonEntry,Integer> caught = getCaught(questdata);

            if(caught.containsKey(pokemonEntry) && caught.get(pokemonEntry) == progress) {
                return;
            }
            caught.put(pokemonEntry, progress);
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
            MutableComponent text = Component.translatable("objective.pokecatch").append(" ");
            if(pokemonEntry.isShiny()){
                text.append(Component.translatable("poketype.shiny").append(" "));
            }
            Species species = PokemonSpecies.INSTANCE.getByIdentifier(new ResourceLocation(pokemonEntry.getType()));
            text.append(species==null?Component.translatable(pokemonEntry.getType()):species.getTranslatedName());
            return text.append(": " + getProgress() + "/" + getMaxProgress());}
    }

}
