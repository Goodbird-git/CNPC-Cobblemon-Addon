package com.goodbird.cnpccobblemonaddon.mixin.impl;

import com.goodbird.cnpccobblemonaddon.client.gui.spawner.GuiSpawnerEntry;
import com.goodbird.cnpccobblemonaddon.client.gui.spawner.GuiSpawnerEntryLoot;
import com.goodbird.cnpccobblemonaddon.registry.ModContainerRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import noppes.npcs.CustomNpcsClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CustomNpcsClient.class)
public class MixinCNPCClient {
    @Inject(method = "onInitializeClient", at=@At("TAIL"), remap = false)
    public void load(CallbackInfo ci) {
        MenuScreens.register(ModContainerRegistry.EDIT_ENTRY_MENU.get(), GuiSpawnerEntry::new);
        MenuScreens.register(ModContainerRegistry.EDIT_ENTRY_LOOT_MENU.get(), GuiSpawnerEntryLoot::new);
    }
}
