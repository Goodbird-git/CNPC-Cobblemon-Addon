package com.goodbird.cnpccobblemonaddon.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static <T extends INBTSerializable<CompoundTag>> ListTag tagIntegerMapToNBT(Map<T, Integer> map) {
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

    public static <T extends INBTSerializable<CompoundTag>> ArrayList<T> getNBTList(Class<T> clazz, ListTag tagList) {
        ArrayList<T> list = new ArrayList();
        try {
            for (int i = 0; i < tagList.size(); ++i) {
                T t = clazz.getConstructor().newInstance();
                t.deserializeNBT(tagList.getCompound(i));
                list.add(t);
            }
        }catch (Exception ignored){

        }
        return list;
    }

    public static <T extends INBTSerializable<CompoundTag>> ListTag tagListToNBT(List<T> arr) {
        ListTag nbttaglist = new ListTag();
        if (arr != null) {
            for (T t : arr) {
                nbttaglist.add(t.serializeNBT());
            }
        }
        return nbttaglist;
    }
}
