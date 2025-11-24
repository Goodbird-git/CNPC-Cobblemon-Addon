package com.goodbird.cnpccobblemonaddon.mixin.impl;

import com.goodbird.cnpccobblemonaddon.client.gui.spawner.*;
import com.goodbird.cnpccobblemonaddon.constants.CMEnumGuiType;
import com.goodbird.cnpccobblemonaddon.registry.ModContainerRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import noppes.npcs.CustomContainer;
import noppes.npcs.client.ClientProxy;
import noppes.npcs.client.gui.player.GuiNPCBankChest;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.entity.EntityNPCInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientProxy.class)
public class ClientProxyMixin {

    @Inject(method = "getGui", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getGui(EnumGuiType guiType, EntityNPCInterface npc, FriendlyByteBuf buf, CallbackInfoReturnable<Screen> cir) {
        if (guiType == CMEnumGuiType.CobbleSpawnerMainSettings) {
            GuiMainSettings gui = new GuiMainSettings(buf.readBlockPos());
            cir.setReturnValue(gui);
        }
        if (guiType == CMEnumGuiType.CobbleSpawnerSpawnSettings) {
            GuiSpawnSettings gui = new GuiSpawnSettings(buf.readBlockPos());
            cir.setReturnValue(gui);
        }
        if (guiType == CMEnumGuiType.CobbleSpawnerPokemonList) {
            GuiSpawnerPokemons gui = new GuiSpawnerPokemons(buf.readBlockPos());
            cir.setReturnValue(gui);
        }
        if (guiType == CMEnumGuiType.CobbleSpawnerSpawnerList) {
            GuiSpawnerList gui = new GuiSpawnerList();
            cir.setReturnValue(gui);
        }
    }

    @Inject(method = "load", at=@At("TAIL"), remap = false)
    public void load(CallbackInfo ci) {
        MenuScreens.register(ModContainerRegistry.EDIT_ENTRY_MENU.get(), GuiSpawnerEntry::new);
        MenuScreens.register(ModContainerRegistry.EDIT_ENTRY_LOOT_MENU.get(), GuiSpawnerEntryLoot::new);
    }
}