package com.goodbird.cnpccobblemonaddon.registry;

import com.goodbird.cnpccobblemonaddon.item.ItemSpawnerWand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemRegistry {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "cnpccobblemonaddon");

    public static final RegistryObject<BlockItem> POKE_SPAWNER_ITEM = ITEMS.register("pokespawner", ()->new BlockItem(ModBlockRegistry.POKE_SPAWNER.get(), new Item.Properties()));

    public static final RegistryObject<ItemSpawnerWand> SPAWNER_WAND_ITEM = ITEMS.register("spawnerwand", ItemSpawnerWand::new);
}
