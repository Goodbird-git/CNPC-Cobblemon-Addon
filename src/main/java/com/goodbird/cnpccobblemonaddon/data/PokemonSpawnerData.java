package com.goodbird.cnpccobblemonaddon.data;

import com.goodbird.cnpccobblemonaddon.constants.EnumSpawnTime;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Random;

public class PokemonSpawnerData {
    private boolean redstoneControl;
    private int spawnRadius;
    private int maxSpawnsPerRound;
    private int maxSpawnsPerSpawner;
    private int tickRangeMin, tickRangeMax;
    private int playerDetectionRange;
    private boolean safeSpawn;
    private boolean allowWater;

    private boolean canSpawnUnder;
    private ArrayList<PokemonSpawnEntry> spawnEntries;

    private String name;

    public PokemonSpawnerData(){
        redstoneControl = false;
        spawnRadius = 16;
        maxSpawnsPerRound = 2;
        maxSpawnsPerSpawner = 4;
        tickRangeMin = 60;
        tickRangeMax = 120;
        playerDetectionRange = 16;
        safeSpawn = true;
        allowWater = false;
        canSpawnUnder = false;
        spawnEntries = new ArrayList<>();
        name = "Spawner";
    }

    public boolean isRedstoneControl() {
        return redstoneControl;
    }

    public int getSpawnRadius() {
        return spawnRadius;
    }

    public int getMaxSpawnsPerRound() {
        return maxSpawnsPerRound;
    }

    public int getMaxSpawnsPerSpawner() {
        return maxSpawnsPerSpawner;
    }

    public int getTickRangeMin() {
        return tickRangeMin;
    }

    public int getTickRangeMax() {
        return tickRangeMax;
    }

    public int getPlayerDetectionRange() {
        return playerDetectionRange;
    }

    public boolean isSafeSpawn() {
        return safeSpawn;
    }

    public boolean isAllowWater() {
        return allowWater;
    }

    public ArrayList<PokemonSpawnEntry> getSpawnEntries() {
        return spawnEntries;
    }

    public boolean isCanSpawnUnder() {
        return canSpawnUnder;
    }

    public String getName() {
        return name;
    }

    public PokemonSpawnEntry getRandomEntry(Level level){
        Random random = new Random();
        double totalWeight = 0;
        for (PokemonSpawnEntry entry : spawnEntries) {
            if (!entry.isActive) continue;
            if(!entry.timeMatches(level)) continue;
            totalWeight += entry.weight;
        }
        double rnd = random.nextDouble(totalWeight);
        double curSum = 0;
        for(PokemonSpawnEntry entry : spawnEntries){
            if (!entry.isActive) continue;
            if(!entry.timeMatches(level)) continue;
            if(rnd >= curSum && rnd <= curSum+entry.weight){
                return entry;
            }
            curSum+=entry.weight;
        }
        return null;
    }

    public CompoundTag serializeNBT(){
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("redstoneControl", redstoneControl);
        tag.putInt("spawnRadius", spawnRadius);
        tag.putInt("maxSpawnsPerRound", maxSpawnsPerRound);
        tag.putInt("maxSpawnsPerSpawner", maxSpawnsPerSpawner);
        tag.putInt("tickRangeMin", tickRangeMin);
        tag.putInt("tickRangeMax", tickRangeMax);
        tag.putInt("playerDetectionRange", playerDetectionRange);
        tag.putBoolean("safeSpawn", safeSpawn);
        tag.putBoolean("allowWater", allowWater);
        tag.putBoolean("spawnUnder", canSpawnUnder);
        ListTag entries = new ListTag();
        for(PokemonSpawnEntry entry: spawnEntries){
            entries.add(entry.serializeNBT());
        }
        tag.put("spawnEntries", entries);
        tag.putString("name", name);
        return tag;
    }

    public PokemonSpawnerData parseNBT(CompoundTag tag){
        redstoneControl = tag.getBoolean("redstoneControl");
        spawnRadius = tag.getInt("spawnRadius");
        maxSpawnsPerRound = tag.getInt("maxSpawnsPerRound");
        maxSpawnsPerSpawner = tag.getInt("maxSpawnsPerSpawner");
        tickRangeMin = tag.getInt("tickRangeMin");
        tickRangeMax = tag.getInt("tickRangeMax");
        playerDetectionRange = tag.getInt("playerDetectionRange");
        safeSpawn = tag.getBoolean("safeSpawn");
        allowWater = tag.getBoolean("allowWater");
        canSpawnUnder = tag.getBoolean("spawnUnder");
        ListTag entries = tag.getList("spawnEntries", 10);
        spawnEntries.clear();
        for(int i=0;i<entries.size();i++){
            spawnEntries.add(new PokemonSpawnEntry().parseNBT(entries.getCompound(i)));
        }
        name = tag.getString("name");
        return this;
    }

    public void setRedstoneControl(boolean redstoneControl) {
        this.redstoneControl = redstoneControl;
    }

    public void setSpawnRadius(int spawnRadius) {
        this.spawnRadius = spawnRadius;
    }

    public void setMaxSpawnsPerRound(int maxSpawnsPerRound) {
        this.maxSpawnsPerRound = maxSpawnsPerRound;
    }

    public void setMaxSpawnsPerSpawner(int maxSpawnsPerSpawner) {
        this.maxSpawnsPerSpawner = maxSpawnsPerSpawner;
    }

    public void setTickRangeMin(int tickRangeMin) {
        this.tickRangeMin = tickRangeMin;
    }

    public void setTickRangeMax(int tickRangeMax) {
        this.tickRangeMax = tickRangeMax;
    }

    public void setPlayerDetectionRange(int playerDetectionRange) {
        this.playerDetectionRange = playerDetectionRange;
    }

    public void setSafeSpawn(boolean safeSpawn) {
        this.safeSpawn = safeSpawn;
    }

    public void setAllowWater(boolean allowWater) {
        this.allowWater = allowWater;
    }

    public void setCanSpawnUnder(boolean canSpawnUnder) {
        this.canSpawnUnder = canSpawnUnder;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class PokemonSpawnEntry {
        private String entryName;
        private PokemonData pokemon;
        private double weight;
        private EnumSpawnTime spawnTime;
        private boolean isActive;

        public PokemonSpawnEntry(){
            entryName = "";
            pokemon = new PokemonData();
            weight = 1;
            spawnTime = EnumSpawnTime.ALL;
            isActive = true;
        }

        public PokemonSpawnEntry(String entryName) {
            this();
            this.entryName = entryName;
        }

        public CompoundTag serializeNBT(){
            CompoundTag tag = new CompoundTag();
            tag.putString("entryName", entryName);
            tag.put("pokemon", pokemon.serializeNBT());
            tag.putDouble("weight", weight);
            tag.putInt("spawnTime", spawnTime.ordinal());
            tag.putBoolean("isActive", isActive);
            return tag;
        }

        public PokemonSpawnEntry parseNBT(CompoundTag tag){
            entryName = tag.getString("entryName");
            pokemon.parseNBT(tag.getCompound("pokemon"));
            weight = tag.getInt("weight");
            spawnTime = EnumSpawnTime.values()[tag.getInt("spawnTime")];
            isActive = tag.getBoolean("isActive");
            return this;
        }

        public String getEntryName() {
            return entryName;
        }

        public PokemonSpawnEntry setEntryName(String entryName) {
            this.entryName = entryName;
            return this;
        }

        public PokemonData getPokemon() {
            return pokemon;
        }

        public PokemonSpawnEntry setPokemon(PokemonData pokemon) {
            this.pokemon = pokemon;
            return this;
        }

        public double getWeight() {
            return weight;
        }

        public PokemonSpawnEntry setWeight(double weight) {
            this.weight = weight;
            return this;
        }

        public EnumSpawnTime getSpawnTime() {
            return spawnTime;
        }

        public PokemonSpawnEntry setSpawnTime(EnumSpawnTime spawnTime) {
            this.spawnTime = spawnTime;
            return this;
        }

        public boolean timeMatches(Level level){
            long daytime = level.getDayTime()%24000;
            if((daytime>=23000 || daytime<=6000) && spawnTime==EnumSpawnTime.MORNING) return true;
            if((daytime>=6000 && daytime<=12000) && spawnTime==EnumSpawnTime.DAY) return true;
            if((daytime>=12000 && daytime<=13000) && spawnTime==EnumSpawnTime.EVENING) return true;
            if((daytime>=13000 && daytime<=23000) && spawnTime==EnumSpawnTime.NIGHT) return true;
            return spawnTime==EnumSpawnTime.ALL;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }
    }
}
