package com.goodbird.cnpccobblemonaddon.data;

import com.cobblemon.mod.common.api.drop.DropEntry;
import com.cobblemon.mod.common.api.drop.DropTable;
import com.cobblemon.mod.common.api.drop.ItemDropEntry;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.goodbird.cnpccobblemonaddon.mixin.impl.DropTableMixin;
import com.goodbird.cnpccobblemonaddon.mixin.impl.ItemDropEntryMixin;
import kotlin.ranges.IntRange;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

public class PokemonDropData {
    private boolean dropNaturalLoot;

    private int minTotalLoot, maxTotalLoot;
    private final DropItemEntry[] dropItems = new DropItemEntry[9];

    public PokemonDropData(){
        dropNaturalLoot = true;
        minTotalLoot = 1;
        maxTotalLoot = 1;
        for(int i=0;i<9;i++){
            dropItems[i]=new DropItemEntry();
        }
    }

    public boolean isDropNaturalLoot() {
        return dropNaturalLoot;
    }

    public void setDropNaturalLoot(boolean dropNaturalLoot) {
        this.dropNaturalLoot = dropNaturalLoot;
    }

    public DropItemEntry[] getDropItems() {
        return dropItems;
    }

    public int getMinTotalLoot() {
        return minTotalLoot;
    }

    public void setMinTotalLoot(int minTotalLoot) {
        this.minTotalLoot = minTotalLoot;
    }

    public int getMaxTotalLoot() {
        return maxTotalLoot;
    }

    public void setMaxTotalLoot(int maxTotalLoot) {
        this.maxTotalLoot = maxTotalLoot;
    }

    public DropTable getDropTable(Pokemon pokemon){
        DropTable table = new DropTable();
        if(dropNaturalLoot){
            for(DropEntry entry: ((DropTableMixin)(Object)pokemon.getSpecies().getDrops()).getEntries()){
                ((DropTableMixin)(Object)table).getEntries().add(entry);
            }
        }
        for(DropItemEntry item : this.dropItems){
            ((DropTableMixin)(Object)table).getEntries().add(item.getDropEntry());
        }
        if(minTotalLoot<=maxTotalLoot) {
            ((DropTableMixin) (Object) table).setAmount(new IntRange(minTotalLoot, maxTotalLoot));
        }
        return table;
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider){
        CompoundTag tag = new CompoundTag();

        tag.putBoolean("naturalDrop", dropNaturalLoot);
        tag.putInt("minTotalQuantity", minTotalLoot);
        tag.putInt("maxTotalQuantity", maxTotalLoot);

        ListTag dropItems = new ListTag();
        for(DropItemEntry item : this.dropItems){
            dropItems.add(item.serializeNBT(provider));
        }
        tag.put("dropItems", dropItems);
        return tag;
    }

    public PokemonDropData parseNBT(HolderLookup.Provider provider, CompoundTag tag){
        dropNaturalLoot = tag.getBoolean("naturalDrop");
        minTotalLoot = tag.getInt("minTotalQuantity");
        maxTotalLoot = tag.getInt("maxTotalQuantity");

        ListTag dropItems = tag.getList("dropItems", 10);
        for(int i=0; i<this.dropItems.length; i++){
            this.dropItems[i] = new DropItemEntry().parseNBT(provider, dropItems.getCompound(i));
        }
        return this;
    }

    public static class DropItemEntry {
        private ItemStack dropStackEntry;
        private float chance;
        private int minQuantity;
        private int maxQuantity;

        public DropItemEntry(){
            dropStackEntry = ItemStack.EMPTY;
            chance = 0;
            minQuantity = 0;
            maxQuantity = 1;
        }

        public DropItemEntry(ItemStack heldStackEntry, float chance, int minQuantity, int maxQuantity) {
            this.dropStackEntry = heldStackEntry;
            this.chance = chance;
            this.minQuantity = minQuantity;
            this.maxQuantity = maxQuantity;
        }

        public ItemStack getDropStackEntry() {
            return dropStackEntry;
        }

        public float getChance() {
            return chance;
        }

        public int getMinQuantity() {
            return minQuantity;
        }

        public int getMaxQuantity() {
            return maxQuantity;
        }

        public void setDropStackEntry(ItemStack dropStackEntry) {
            this.dropStackEntry = dropStackEntry;
        }

        public void setChance(float chance) {
            this.chance = chance;
        }

        public void setMinQuantity(int minQuantity) {
            this.minQuantity = minQuantity;
        }

        public void setMaxQuantity(int maxQuantity) {
            this.maxQuantity = maxQuantity;
        }

        public DropEntry getDropEntry(){
            DropEntry dropEntry = new ItemDropEntry();
            ItemDropEntryMixin entry = (ItemDropEntryMixin)dropEntry;

            entry.setPercentage(chance);
            entry.setItem(BuiltInRegistries.ITEM.getKey(dropStackEntry.getItem()));
            if(minQuantity>maxQuantity){
                entry.setQuantityRange(new IntRange(0, 0));
            }else {
                entry.setQuantityRange(new IntRange(minQuantity, maxQuantity));
            }

            if(!dropStackEntry.getComponents().isEmpty()){
                entry.setComponents(dropStackEntry.getComponents());
            }
            return dropEntry;
        }

        public CompoundTag serializeNBT(HolderLookup.Provider provider){
            CompoundTag tag = new CompoundTag();
            tag.put("stack", dropStackEntry.isEmpty()? new CompoundTag() : dropStackEntry.save(provider));
            tag.putFloat("chance", chance);
            tag.putInt("minQuantity", minQuantity);
            tag.putInt("maxQuantity", maxQuantity);
            return tag;
        }

        public PokemonDropData.DropItemEntry parseNBT(HolderLookup.Provider provider, CompoundTag tag){
            dropStackEntry = ItemStack.parse(provider, tag.getCompound("stack")).orElse(ItemStack.EMPTY);
            chance = tag.getFloat("chance");
            minQuantity = tag.getInt("minQuantity");
            maxQuantity = tag.getInt("maxQuantity");
            return this;
        }
    }
}
