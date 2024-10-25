package com.goodbird.cnpccobblemonaddon.handler;

import com.cobblemon.mod.common.api.battles.model.actor.ActorType;
import com.cobblemon.mod.common.api.events.battles.BattleFaintedEvent;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import com.goodbird.cnpccobblemonaddon.constants.PokeQuestType;
import com.goodbird.cnpccobblemonaddon.quest.QuestPokeKill;
import kotlin.Unit;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.common.Mod;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerQuestData;
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
                doQuest(player, event.getKilled().getEntity());
            }
        }
        return Unit.INSTANCE;
    }
    private static void doQuest(Player player, PokemonEntity entity) {
        PlayerData pdata = PlayerData.get(player);
        PlayerQuestData playerdata = pdata.questData;
        String pokemonType = entity.getPokemon().getSpecies().resourceIdentifier.toString();

        for(QuestData data : playerdata.activeQuests.values()){
            if(data.quest.type != PokeQuestType.POKE_DEFEAT)
                continue;

            String name = pokemonType;
            QuestPokeKill quest = (QuestPokeKill) data.quest.questInterface;
            if(!quest.targets.containsKey(name))
                continue;
            HashMap<String, Integer> killed = quest.getKilled(data);
            if(killed.containsKey(name) && killed.get(name) >= quest.targets.get(name))
                continue;
            int amount = 0;
            if(killed.containsKey(name))
                amount = killed.get(name);
            killed.put(name, amount + 1);
            quest.setKilled(data, killed);
            pdata.updateClient = true;
        }
        playerdata.checkQuestCompletion(player, PokeQuestType.POKE_DEFEAT);
    }
}
