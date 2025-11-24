package com.goodbird.cnpccobblemonaddon;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.goodbird.cnpccobblemonaddon.handler.ServerEventHandler;
import com.goodbird.cnpccobblemonaddon.registry.ModBlockRegistry;
import com.goodbird.cnpccobblemonaddon.registry.ModContainerRegistry;
import com.goodbird.cnpccobblemonaddon.registry.ModItemRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("cnpccobblemonaddon")
public class CNPCCobblemonAddon {
    public CNPCCobblemonAddon(){
        CobblemonEvents.BATTLE_FAINTED.subscribe(Priority.NORMAL, ServerEventHandler::onPokemonFainted);
        CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL, ServerEventHandler::onPokemonCaught);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlockRegistry.BLOCKS.register(modEventBus);
        ModBlockRegistry.TILES.register(modEventBus);
        ModItemRegistry.ITEMS.register(modEventBus);
        ModContainerRegistry.MENUS.register(modEventBus);
    }
}
