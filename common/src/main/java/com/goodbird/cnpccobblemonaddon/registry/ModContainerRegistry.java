package com.goodbird.cnpccobblemonaddon.registry;

import com.goodbird.cnpccobblemonaddon.client.gui.container.spawner.ContainerSpawnerEntry;
import com.goodbird.cnpccobblemonaddon.client.gui.container.spawner.ContainerSpawnerEntryLoot;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;

public class ModContainerRegistry {
    public static DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create("cnpccobblemonaddon", Registries.MENU);

    public static final RegistrySupplier<MenuType<ContainerSpawnerEntry>> EDIT_ENTRY_MENU = MENUS.register("edit_entry", ()->
            createContainer((containerId, inv, data) -> new ContainerSpawnerEntry(containerId, inv, data.readBlockPos(), data.readInt()))
    );

    public static final RegistrySupplier<MenuType<ContainerSpawnerEntryLoot>> EDIT_ENTRY_LOOT_MENU = MENUS.register("edit_entry_loot", ()->
            createContainer((containerId, inv, data) -> new ContainerSpawnerEntryLoot(containerId, inv, data.readBlockPos(), data.readInt()))
    );


    private static <T extends AbstractContainerMenu> MenuType<T> createContainer(MenuRegistry.ExtendedMenuTypeFactory<T> factoryIn){
        return MenuRegistry.ofExtended(factoryIn);
    }
}
