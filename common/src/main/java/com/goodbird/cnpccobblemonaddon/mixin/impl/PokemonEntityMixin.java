package com.goodbird.cnpccobblemonaddon.mixin.impl;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PokemonEntity.class)
public interface PokemonEntityMixin {
    @Accessor(remap=false)
    static EntityDataAccessor<Boolean> getUNBATTLEABLE() {
        return null;
    }
}
