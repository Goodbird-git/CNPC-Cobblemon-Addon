package com.goodbird.cnpccobblemonaddon.handler;

import com.cobblemon.mod.common.api.battles.model.actor.ActorType;
import com.cobblemon.mod.common.api.events.battles.BattleFaintedEvent;
import com.cobblemon.mod.common.api.events.pokemon.PokemonCapturedEvent;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import com.goodbird.cnpccobblemonaddon.EventHooks;
import com.goodbird.cnpccobblemonaddon.api.event.PokemonCatchEvent;
import com.goodbird.cnpccobblemonaddon.constants.PokeQuestType;
import com.goodbird.cnpccobblemonaddon.quest.PokemonEntry;
import com.goodbird.cnpccobblemonaddon.quest.QuestPokeCatch;
import com.goodbird.cnpccobblemonaddon.quest.QuestPokeKill;
import kotlin.Unit;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.common.Mod;
import noppes.npcs.api.wrapper.WrapperNpcAPI;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerQuestData;
import noppes.npcs.controllers.data.PlayerScriptData;
import noppes.npcs.controllers.data.QuestData;

import java.util.HashMap;
import java.util.UUID;

@Mod.EventBusSubscriber(modid="cnpccobblemonaddon")
public class ServerEventHandler {

    public static Unit onPokemonFainted(BattleFaintedEvent event){
        for(BattlePokemon opponent : event.getKilled().getFacedOpponents()){
            if(event.getKilled().getEntity() == null || opponent.actor.getType() != ActorType.PLAYER) continue;
            for(UUID playerId: opponent.actor.getPlayerUUIDs()){
                Player player = PlayerExtensionsKt.getPlayer(playerId);
                if(player==null) continue;
                doDefeatQuest(player, event.getKilled().getEntity());
            }
        }
        return Unit.INSTANCE;
    }
    private static void doDefeatQuest(Player player, PokemonEntity entity) {
        PlayerData pdata = PlayerData.get(player);
        PlayerQuestData playerdata = pdata.questData;
        String pokemonType = entity.getPokemon().getSpecies().resourceIdentifier.toString();
        PokemonEntry entry = new PokemonEntry(pokemonType, entity.getPokemon().getShiny());
        for(QuestData data : playerdata.activeQuests.values()){
            if(data.quest.type != PokeQuestType.POKE_DEFEAT)
                continue;

            QuestPokeKill quest = (QuestPokeKill) data.quest.questInterface;
            if(!quest.targets.containsKey(entry))
                continue;
            HashMap<PokemonEntry, Integer> killed = quest.getKilled(data);
            if(killed.containsKey(entry) && killed.get(entry) >= quest.targets.get(entry))
                continue;
            int amount = 0;
            if(killed.containsKey(entry))
                amount = killed.get(entry);
            killed.put(entry, amount + 1);
            quest.setKilled(data, killed);
            pdata.updateClient = true;
        }
        playerdata.checkQuestCompletion(player, PokeQuestType.POKE_DEFEAT);
    }

    public static Unit onPokemonCaught(PokemonCapturedEvent event){
        EventHooks.onPlayerCaughtPokemon(PlayerData.get(event.getPlayer()).scriptData, event);
        doCatchQuest(event.getPlayer(), event.getPokemon());
        return Unit.INSTANCE;
    }
    private static void doCatchQuest(Player player, Pokemon entity) {
        PlayerData pdata = PlayerData.get(player);
        PlayerQuestData playerdata = pdata.questData;
        String pokemonType = entity.getSpecies().resourceIdentifier.toString();
        PokemonEntry entry = new PokemonEntry(pokemonType, entity.getShiny());

        for(QuestData data : playerdata.activeQuests.values()){
            if(data.quest.type != PokeQuestType.POKE_CATCH)
                continue;

            QuestPokeCatch quest = (QuestPokeCatch) data.quest.questInterface;
            if(!quest.targets.containsKey(entry))
                continue;
            HashMap<PokemonEntry, Integer> caught = quest.getCaught(data);
            if(caught.containsKey(entry) && caught.get(entry) >= quest.targets.get(entry))
                continue;
            int amount = 0;
            if(caught.containsKey(entry))
                amount = caught.get(entry);
            caught.put(entry, amount + 1);
            quest.setCaught(data, caught);
            pdata.updateClient = true;
        }
        playerdata.checkQuestCompletion(player, PokeQuestType.POKE_CATCH);
    }
}
