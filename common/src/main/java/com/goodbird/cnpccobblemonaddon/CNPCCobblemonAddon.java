package com.goodbird.cnpccobblemonaddon;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.battles.BattleFaintedEvent;
import com.cobblemon.mod.common.api.events.pokemon.PokemonCapturedEvent;
import com.goodbird.cnpccobblemonaddon.handler.ServerEventHandler;
import com.goodbird.cnpccobblemonaddon.registry.ModBlockRegistry;
import com.goodbird.cnpccobblemonaddon.registry.ModContainerRegistry;
import com.goodbird.cnpccobblemonaddon.registry.ModItemRegistry;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class CNPCCobblemonAddon {
    public static final String MOD_ID = "cnpccobblemonaddon";
    public static void init(){
        CobblemonEvents.BATTLE_FAINTED.subscribe(Priority.NORMAL, (Function1<? super BattleFaintedEvent, Unit>) ServerEventHandler::onPokemonFainted);
        CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL, (Function1<? super PokemonCapturedEvent, Unit>) ServerEventHandler::onPokemonCaught);
        ModBlockRegistry.BLOCKS.register();
        ModBlockRegistry.TILES.register();
        ModItemRegistry.ITEMS.register();
        ModContainerRegistry.MENUS.register();
    }
}
