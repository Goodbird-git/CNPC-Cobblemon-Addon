package com.goodbird.cnpccobblemonaddon.quest;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PokemonEntry implements Comparable<PokemonEntry>, INBTSerializable<CompoundTag> {
    private String type;

    private boolean isShiny;

    public PokemonEntry(){

    }

    public PokemonEntry(String type, boolean isShiny) {
        this.type = type;
        this.isShiny = isShiny;
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

    @Override
    public String toString() {
        return "PokemonEntry{" +
                "type='" + type + '\'' +
                ", isShiny=" + isShiny +
                '}';
    }

    @Override
    public int compareTo(@NotNull PokemonEntry o) {
        if(type.compareTo(o.type)!=0) return type.compareTo(o.type);
        if (Boolean.compare(isShiny, o.isShiny)!=0) return Boolean.compare(isShiny, o.isShiny);
        return 0;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", type);
        tag.putBoolean("isShiny", isShiny);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.type = nbt.getString("type");
        this.isShiny = nbt.getBoolean("isShiny");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PokemonEntry that)) return false;
        return isShiny == that.isShiny && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, isShiny);
    }
}
