package com.goodbird.cnpccobblemonaddon.mixin.impl;

import com.goodbird.cnpccobblemonaddon.constants.CMEnumGuiType;
import com.goodbird.cnpccobblemonaddon.registry.ModContainerRegistry;
import net.minecraft.world.inventory.MenuType;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumGuiType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoppesUtilServer.class)
public class NoppesUtilServerMixin {
    @Inject(method = "getType", at=@At("HEAD"), cancellable = true, remap = false)
    private static void getType(EnumGuiType gui, CallbackInfoReturnable<MenuType> cir) {
        if (gui == CMEnumGuiType.CobbleSpawnerEntrySettings) {
            cir.setReturnValue(ModContainerRegistry.EDIT_ENTRY_MENU.get());
        }
        if (gui == CMEnumGuiType.CobbleSpawnerEntryLootSettings) {
            cir.setReturnValue(ModContainerRegistry.EDIT_ENTRY_LOOT_MENU.get());
        }
    }
}
