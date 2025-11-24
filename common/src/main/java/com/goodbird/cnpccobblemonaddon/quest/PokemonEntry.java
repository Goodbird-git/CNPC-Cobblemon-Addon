package com.goodbird.cnpccobblemonaddon.quest;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.goodbird.cnpccobblemonaddon.util.INBTSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PokemonEntry implements Comparable<PokemonEntry>, INBTSerializable<CompoundTag> {
    private String type;

    private boolean isShiny;

    private int minLevel = 0;
    public PokemonEntry(){

    }

    public PokemonEntry(String type, boolean isShiny) {
        this(type, isShiny, 0);
    }

    public PokemonEntry(String type, boolean isShiny, int minLevel) {
        this.type = type;
        this.isShiny = isShiny;
        this.minLevel = minLevel;
    }

    public PokemonEntry(String type) {
        this(type, false);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isShiny() {
        return isShiny;
    }

    public void setShiny(boolean shiny) {
        isShiny = shiny;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(int minLevel) {
        this.minLevel = minLevel;
    }

    @Override
    public String toString() {
        return "PokemonEntry{" +
                "type='" + type + '\'' +
                ", isShiny=" + isShiny +
                ", minLevel=" + minLevel +
                '}';
    }

    @Override
    public int compareTo(@NotNull PokemonEntry o) {
        if(type.compareTo(o.type)!=0) return type.compareTo(o.type);
        if (Boolean.compare(isShiny, o.isShiny)!=0) return Boolean.compare(isShiny, o.isShiny);
        if (Integer.compare(minLevel, o.minLevel)!=0) return Integer.compare(minLevel, o.minLevel);
        return 0;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", type);
        tag.putBoolean("isShiny", isShiny);
        tag.putInt("minLevel", minLevel);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.type = nbt.getString("type");
        this.isShiny = nbt.getBoolean("isShiny");
        this.minLevel = nbt.getInt("minLevel");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PokemonEntry that)) return false;
        return isShiny == that.isShiny && Objects.equals(type, that.type) && minLevel == that.minLevel;
    }

    public boolean matches(Pokemon pokemon){
        if(!pokemon.getSpecies().resourceIdentifier.toString().equals(type)) return false;
        if(pokemon.getShiny()!=isShiny) return false;
        if(pokemon.getLevel()<minLevel) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, isShiny, minLevel);
    }
}
