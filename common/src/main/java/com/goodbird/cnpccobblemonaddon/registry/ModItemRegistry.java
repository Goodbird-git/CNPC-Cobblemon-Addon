package com.goodbird.cnpccobblemonaddon.registry;

import com.goodbird.cnpccobblemonaddon.item.ItemSpawnerWand;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;

public class ModItemRegistry {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create("cnpccobblemonaddon", Registries.ITEM);

    public static final RegistrySupplier<BlockItem> POKE_SPAWNER_ITEM = ITEMS.register("pokespawner", ()->new BlockItem(ModBlockRegistry.POKE_SPAWNER.get(), new Item.Properties()));

    public static final RegistrySupplier<ItemSpawnerWand> SPAWNER_WAND_ITEM = ITEMS.register("spawnerwand", ItemSpawnerWand::new);
}
