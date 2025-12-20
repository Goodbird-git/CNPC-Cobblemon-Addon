package com.goodbird.cnpccobblemonaddon.data;

import com.cobblemon.mod.common.CobblemonEntities;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.properties.UncatchableProperty;
import com.goodbird.cnpccobblemonaddon.mixin.impl.PokemonEntityMixin;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.*;

public class PokemonData {
    private ResourceLocation species;
    private int levelMin, levelMax;
    private boolean isShiny;
    private boolean form;
    private int[] evMins = new int[6];
    private int[] ivMins = new int[6];
    private int[] evMaxs = new int[6];
    private int[] ivMaxs = new int[6];
    private boolean uncatchable;
    private boolean untradable;
    private boolean unbattlable;
    private HeldItemEntry[] heldItems = new HeldItemEntry[3];

    private PokemonDropData dropData = new PokemonDropData();

    public PokemonData(){
        species = new ResourceLocation("cobblemon", "stonjourner");
        levelMin = 0;
        levelMax = 100;
        isShiny = false;
        form = false;
        Arrays.fill(evMins, 0);
        Arrays.fill(ivMins, -1);
        Arrays.fill(evMaxs, 0);
        Arrays.fill(ivMaxs, -1);
        uncatchable = false;
        unbattlable = false;
        untradable = false;
        for(int i=0;i<heldItems.length;i++){
            heldItems[i] = new HeldItemEntry(ItemStack.EMPTY, 0);
        }
    }

    public PokemonData setSpecies(ResourceLocation species){
        this.species = species;
        return this;
    }

    public PokemonData setLevelMin(int levelMin){
        this.levelMin = levelMin;
        return this;
    }

    public PokemonData setLevelMax(int levelMax){
        this.levelMax = levelMax;
        return this;
    }

    public PokemonData setShiny(boolean shiny) {
        isShiny = shiny;
        return this;
    }

    public PokemonData setForm(boolean form) {
        this.form = form;
        return this;
    }

    public PokemonData setUncatchable(boolean uncatchable) {
        this.uncatchable = uncatchable;
        return this;
    }

    public PokemonData setUntradable(boolean untradable) {
        this.untradable = untradable;
        return this;
    }

    public PokemonData setUnbattlable(boolean unbattlable) {
        this.unbattlable = unbattlable;
        return this;
    }

    public PokemonData setHeldItem(int slot, ItemStack stack, double chance){
        if(slot>=0 && slot<heldItems.length)
            heldItems[slot] = new HeldItemEntry(stack, chance);
        return this;
    }

    public HeldItemEntry getHeldItem(int slot){
        if(slot>=0 && slot<heldItems.length)
            return heldItems[slot];
        return new HeldItemEntry();
    }

    public PokemonData setIVMin(int slot, int val){
        if(slot>=0 && slot<ivMins.length)
            ivMins[slot]=val;
        return this;
    }

    public PokemonData setEVMin(int slot, int val){
        if(slot>=0 && slot<evMins.length)
            evMins[slot]=val;
        return this;
    }

    public PokemonData setIVMax(int slot, int val){
        if(slot>=0 && slot<ivMaxs.length)
            ivMaxs[slot]=val;
        return this;
    }

    public PokemonData setEVMax(int slot, int val){
        if(slot>=0 && slot<evMaxs.length)
            evMaxs[slot]=val;
        return this;
    }

    public ResourceLocation getSpecies() {
        return species;
    }

    public int getLevelMin() {
        return levelMin;
    }

    public int getLevelMax() {
        return levelMax;
    }

    public boolean isShiny() {
        return isShiny;
    }

    public boolean isForm() {
        return form;
    }

    public boolean isUncatchable() {
        return uncatchable;
    }

    public boolean isUntradable() {
        return untradable;
    }

    public boolean isUnbattlable() {
        return unbattlable;
    }

    public int getIVMin(int slot){
        if(slot>=0 && slot<ivMins.length)
            return ivMins[slot];
        return -1;
    }

    public int getIVMax(int slot){
        if(slot>=0 && slot<ivMins.length)
            return ivMaxs[slot];
        return -1;
    }

    public int getEVMin(int slot){
        if(slot>=0 && slot<evMins.length)
            return evMins[slot];
        return 0;
    }

    public int getEVMax(int slot){
        if(slot>=0 && slot<evMaxs.length)
            return evMaxs[slot];
        return 0;
    }

    public PokemonDropData getDropData() {
        return dropData;
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider){
        CompoundTag tag = new CompoundTag();
        tag.putString("species", species.toString());
        tag.putInt("levelMin", levelMin);
        tag.putInt("levelMax", levelMax);
        tag.putBoolean("shiny", isShiny);
        tag.putBoolean("form", form);
        tag.putIntArray("ivMins", ivMins);
        tag.putIntArray("evMins", evMins);
        tag.putIntArray("ivMaxs", ivMaxs);
        tag.putIntArray("evMaxs", evMaxs);
        tag.putBoolean("unbattlable", unbattlable);
        tag.putBoolean("untradable", untradable);
        tag.putBoolean("uncatchable", uncatchable);

        ListTag heldItems = new ListTag();
        for(HeldItemEntry item : this.heldItems){
            heldItems.add(item.serializeNBT(provider));
        }
        tag.put("heldItems", heldItems);

        tag.put("dropItems", dropData.serializeNBT(provider));
        return tag;
    }

    public PokemonData parseNBT(HolderLookup.Provider provider, CompoundTag tag){
        species = ResourceLocation.parse(tag.getString("species"));
        levelMin = tag.getInt("levelMin");
        levelMax = tag.getInt("levelMax");
        isShiny = tag.getBoolean("shiny");
        form = tag.getBoolean("form");
        ivMins = tag.getIntArray("ivMins");
        evMins = tag.getIntArray("evMins");
        ivMaxs = tag.getIntArray("ivMaxs");
        evMaxs = tag.getIntArray("evMaxs");
        unbattlable = tag.getBoolean("unbattlable");
        untradable = tag.getBoolean("untradable");
        uncatchable = tag.getBoolean("uncatchable");

        ListTag heldItems = tag.getList("heldItems", 10);
        for(int i=0; i<this.heldItems.length; i++){
            this.heldItems[i] = new HeldItemEntry().parseNBT(provider, heldItems.getCompound(i));
        }

        dropData.parseNBT(provider, tag.getCompound("dropItems"));
        return this;
    }

    public PokemonEntity createPokemon(Level level, Random random){
        Pokemon pokemon = new Pokemon();
        pokemon.setSpecies(PokemonSpecies.INSTANCE.getByIdentifier(species));
        pokemon.setLevel(levelMin+random.nextInt(levelMax-levelMin+1));
        pokemon.setShiny(isShiny);
        for(int i=0;i<6;i++) {
            int randomEv = evMins[i]+ random.nextInt(evMaxs[i]-evMins[i]+1);
            pokemon.setEV(Stats.values()[i], randomEv);
        }
        for(int i=0;i<6;i++) {
            if(ivMins[i]!=-1 && ivMaxs[i]!=-1) {
                int randomIv = ivMins[i]+ random.nextInt(ivMaxs[i]-ivMins[i]+1);
                pokemon.setIV(Stats.values()[i], randomIv);
            }
        }
        pokemon.setTradeable(!untradable);
        if(uncatchable) {
            UncatchableProperty.INSTANCE.uncatchable().apply(pokemon);
        }
        PokemonEntity entity = new PokemonEntity(level, pokemon, CobblemonEntities.POKEMON);
        entity.getEntityData().set(PokemonEntityMixin.getUNBATTLEABLE(), unbattlable);

        ItemStack heldStack = ItemStack.EMPTY;
        List<Integer> indexes = new ArrayList<>(List.of(0,1,2));
        Collections.shuffle(indexes);
        for(int index : indexes){
            if(random.nextDouble(100) < heldItems[index].chance){
                heldStack = heldItems[index].heldStackEntry;
                break;
            }
        }
        pokemon.swapHeldItem(heldStack, false, false);

        entity.setDrops(dropData.getDropTable(pokemon));

        return entity;
    }

    public static class HeldItemEntry {
        private ItemStack heldStackEntry;
        private double chance;

        public HeldItemEntry(){
            heldStackEntry = ItemStack.EMPTY;
            chance = 0;
        }

        public HeldItemEntry(ItemStack heldStackEntry, double chance) {
            this.heldStackEntry = heldStackEntry;
            this.chance = chance;
        }

        public ItemStack getDropStackEntry() {
            return heldStackEntry;
        }

        public double getChance() {
            return chance;
        }

        public void setHeldStackEntry(ItemStack heldStackEntry) {
            this.heldStackEntry = heldStackEntry;
        }

        public void setChance(double chance) {
            this.chance = chance;
        }

        public CompoundTag serializeNBT(HolderLookup.Provider provider){
            CompoundTag tag = new CompoundTag();
            tag.put("stack", heldStackEntry.isEmpty()? new CompoundTag() : heldStackEntry.save(provider));
            tag.putDouble("chance", chance);
            return tag;
        }

        public HeldItemEntry parseNBT(HolderLookup.Provider provider, CompoundTag tag){
            heldStackEntry = ItemStack.parse(provider, tag.getCompound("stack")).orElse(ItemStack.EMPTY);
            chance = tag.getDouble("chance");
            return this;
        }
    }
}
