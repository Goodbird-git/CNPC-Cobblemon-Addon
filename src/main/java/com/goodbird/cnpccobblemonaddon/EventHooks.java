package com.goodbird.cnpccobblemonaddon;

import com.cobblemon.mod.common.api.events.pokemon.PokemonCapturedEvent;
import com.goodbird.cnpccobblemonaddon.api.event.PokemonCatchEvent;
import com.goodbird.cnpccobblemonaddon.constants.CMEnumScriptType;
import noppes.npcs.api.wrapper.WrapperNpcAPI;
import noppes.npcs.controllers.data.PlayerScriptData;

public class EventHooks {
    public static boolean onPlayerCaughtPokemon(PlayerScriptData handler, PokemonCapturedEvent cobblemonEvent) {
        PokemonCatchEvent event = new PokemonCatchEvent(handler.getPlayer(), cobblemonEvent.getPokemon(), cobblemonEvent.getPokeBallEntity());
        handler.runScript(CMEnumScriptType.POKEMON_CATCH, event);
        return WrapperNpcAPI.EVENT_BUS.post(event);
    }
}
