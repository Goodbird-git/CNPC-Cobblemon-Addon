package com.goodbird.cnpccobblemonaddon.handler;

import com.cobblemon.mod.common.CobblemonEntities;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.goodbird.cnpccobblemonaddon.constants.PokeQuestType;
import com.goodbird.cnpccobblemonaddon.quest.QuestPokeKill;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerQuestData;
import noppes.npcs.controllers.data.QuestData;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid="cnpccobblemonaddon")
public class ServerEventHandler {

    @SubscribeEvent
    public static void invoke(LivingDeathEvent event) {
        if(event.getEntity().level().isClientSide) return;
        if(event.getEntity().getType()!= CobblemonEntities.POKEMON) return;
        Entity source = NoppesUtilServer.GetDamageSourcee(event.getSource());
        if(source != null){
            Player player = null;
            if(source instanceof Player)
                player = (Player) source;
            else if(source instanceof EntityNPCInterface && ((EntityNPCInterface)source).getOwner() instanceof Player)
                player = (Player) ((EntityNPCInterface)source).getOwner();

            if(player != null){
                doQuest(player, (PokemonEntity) event.getEntity(), true);
            }
        }
        if(event.getEntity() instanceof Player){
            PlayerData data = PlayerData.get((Player)event.getEntity());
            data.save(false);
        }
    }

    private static void doQuest(Player player, PokemonEntity entity, boolean all) {
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
