package com.goodbird.cnpccobblemonaddon.quest;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
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

public class QuestPokeTeam extends QuestInterface {
    public List<PokemonEntry> targets = new ArrayList<>();

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        targets = NBTUtils.getNBTList(PokemonEntry.class, compound.getList("QuestTeamTargets", 10));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.put("QuestTeamTargets", NBTUtils.tagListToNBT(targets));
    }

    @Override
    public boolean isCompleted(Player player) {
        try {
            PlayerPartyStore store = Cobblemon.INSTANCE.getStorage().getParty(player.getUUID());
            for(PokemonEntry entry : targets) {
                boolean hasInTeam = false;
                for (Pokemon pokemon : store) {
                    if(entry.matches(pokemon)) {
                        hasInTeam = true;
                    }
                }
                if(!hasInTeam) {
                    return false;
                }
            }
            return true;
        } catch (NoPokemonStoreException ignored) {
        }

        return false;
    }

    @Override
    public void handleComplete(Player player) {
    }

    @Override
    public IQuestObjective[] getObjectives(Player player) {
        List<IQuestObjective> list = new ArrayList<>();
        for(PokemonEntry entry : targets){
            list.add(new QuestPokeTeamObjective(player, entry));
        }
        return list.toArray(new IQuestObjective[list.size()]);
    }

    class QuestPokeTeamObjective implements IQuestObjective{
        private final Player player;
        private final PokemonEntry pokemonEntry;
        public QuestPokeTeamObjective(Player player, PokemonEntry pokemonEntry) {
            this.player = player;
            this.pokemonEntry = pokemonEntry;
        }

        @Override
        public int getProgress() {
            try {
                PlayerPartyStore store = Cobblemon.INSTANCE.getStorage().getParty(player.getUUID());
                for(Pokemon pokemon : store){
                    if(pokemonEntry.matches(pokemon)) return 1;
                }
            } catch (NoPokemonStoreException ignored) {
            }
            return 0;
        }

        @Override
        public void setProgress(int progress) {
            throw new CustomNPCsException("Cant set the progress of PokeTeamQuests");
        }

        @Override
        public int getMaxProgress() {
            return 1;
        }

        @Override
        public boolean isCompleted() {
            return getProgress() == 1;
        }

        @Override
        public String getText() {
            return getMCText().getString();
        }

        @Override
        public Component getMCText() {
            MutableComponent text = Component.translatable("objective.poketeam").append(" ");
            if(pokemonEntry.isShiny()){
                text.append(Component.translatable("poketype.shiny").append(" "));
            }
            if(pokemonEntry.getMinLevel()!=0){
                text.append(Component.translatable("poketype.minlevel", pokemonEntry.getMinLevel()).append(" "));
            }
            Species species = PokemonSpecies.INSTANCE.getByIdentifier(new ResourceLocation(pokemonEntry.getType()));
            text.append(species==null?Component.translatable(pokemonEntry.getType()):species.getTranslatedName());
            return text.append(": " + getProgress() + "/" + getMaxProgress());
        }
    }

}
