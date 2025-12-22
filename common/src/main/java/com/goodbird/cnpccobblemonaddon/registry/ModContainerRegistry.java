package com.goodbird.cnpccobblemonaddon.registry;

import com.goodbird.cnpccobblemonaddon.client.gui.container.spawner.ContainerSpawnerEntry;
import com.goodbird.cnpccobblemonaddon.client.gui.container.spawner.ContainerSpawnerEntryLoot;
import dev.architectury.registry.menu.MenuRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Inventory;
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


    private static <T extends AbstractContainerMenu> MenuType<T> createContainer(ExtendedFactory<T, FriendlyByteBuf> factoryIn){
        try {
            return (MenuType<T>) Class.forName("com.goodbird.cnpccobblemonaddon.fabric.client.MenuUtils").getMethod("ofExtended", ExtendedFactory.class).invoke(null, factoryIn);
        }catch (Exception e){
            return null;
        }
    }

    @FunctionalInterface
    public interface ExtendedFactory<T extends AbstractContainerMenu, D> {
        /**
         * Creates a new screen handler with additional screen opening data.
         *
         * @param syncId    the synchronization ID
         * @param inventory the player inventory
         * @param data      the synced data
         * @return the created screen handler
         */
        T create(int syncId, Inventory inventory, D data);
    }
}
