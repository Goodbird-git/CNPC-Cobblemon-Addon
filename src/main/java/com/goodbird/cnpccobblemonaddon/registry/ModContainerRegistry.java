package com.goodbird.cnpccobblemonaddon.registry;

import com.goodbird.cnpccobblemonaddon.client.gui.container.spawner.ContainerSpawnerEntry;
import com.goodbird.cnpccobblemonaddon.client.gui.container.spawner.ContainerSpawnerEntryLoot;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModContainerRegistry {
    public static DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, "cnpccobblemonaddon");

    public static final RegistryObject<MenuType<ContainerSpawnerEntry>> EDIT_ENTRY_MENU = MENUS.register("edit_entry", ()->
            createContainer((IContainerFactory<ContainerSpawnerEntry>) (containerId, inv, data) -> new ContainerSpawnerEntry(containerId, inv, data.readBlockPos(), data.readInt()))
    );

    public static final RegistryObject<MenuType<ContainerSpawnerEntryLoot>> EDIT_ENTRY_LOOT_MENU = MENUS.register("edit_entry_loot", ()->
            createContainer((IContainerFactory<ContainerSpawnerEntryLoot>) (containerId, inv, data) -> new ContainerSpawnerEntryLoot(containerId, inv, data.readBlockPos(), data.readInt()))
    );


    private static <T extends AbstractContainerMenu> MenuType<T> createContainer(MenuType.MenuSupplier<T> factoryIn){
        return new MenuType<>(factoryIn, FeatureFlags.VANILLA_SET);
    }
}
