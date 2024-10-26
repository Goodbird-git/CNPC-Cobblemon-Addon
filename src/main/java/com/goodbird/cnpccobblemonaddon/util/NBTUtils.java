package com.goodbird.cnpccobblemonaddon.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class NBTUtils {
    public static <T extends INBTSerializable<CompoundTag>> HashMap<T, Integer> getNBTIntegerMap(Class<T> clazz, ListTag tagList) {
        HashMap<T, Integer> list = new HashMap();
        try {
            for (int i = 0; i < tagList.size(); ++i) {
                CompoundTag nbttagcompound = tagList.getCompound(i);
                T t = clazz.getConstructor().newInstance();
                t.deserializeNBT(nbttagcompound.getCompound("Slot"));
                list.put(t, nbttagcompound.getInt("Value"));
            }
        }catch (Exception ignored){

        }
        return list;
    }

    public static <T extends INBTSerializable<CompoundTag>> ListTag nbtNBTIntegerMap(Map<T, Integer> map) {
        ListTag nbttaglist = new ListTag();
        if (map != null) {
            for (T t : map.keySet()) {
                CompoundTag nbttagcompound = new CompoundTag();
                nbttagcompound.put("Slot", t.serializeNBT());
                nbttagcompound.putInt("Value", map.get(t));
                nbttaglist.add(nbttagcompound);
            }
        }
        return nbttaglist;
    }

}
