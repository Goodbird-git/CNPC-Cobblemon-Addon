package com.goodbird.cnpccobblemonaddon.api.event;

import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.event.PlayerEvent;

public class PokemonCatchEvent extends PlayerEvent {
    public final Pokemon pokemon;
    public final EmptyPokeBallEntity pokeBallEntity;
    public PokemonCatchEvent(IPlayer player, Pokemon pokemon, EmptyPokeBallEntity pokeBallEntity) {
        super(player);
        this.pokemon = pokemon;
        this.pokeBallEntity = pokeBallEntity;
    }
}
