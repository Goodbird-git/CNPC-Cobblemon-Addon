package com.goodbird.cnpccobblemonaddon;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.goodbird.cnpccobblemonaddon.handler.ServerEventHandler;
import net.minecraftforge.fml.common.Mod;

@Mod("cnpccobblemonaddon")
public class CNPCCobblemonAddon {
    public CNPCCobblemonAddon(){
        CobblemonEvents.BATTLE_FAINTED.subscribe(Priority.NORMAL, ServerEventHandler::onPokemonFainted);
        CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL, ServerEventHandler::onPokemonCaught);
    }
}
