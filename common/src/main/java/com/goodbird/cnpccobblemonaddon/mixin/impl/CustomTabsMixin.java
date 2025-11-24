package com.goodbird.cnpccobblemonaddon.mixin.impl;

import com.goodbird.cnpccobblemonaddon.registry.ModItemRegistry;
import net.minecraft.world.item.CreativeModeTab;
import noppes.npcs.CustomTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CustomTabs.class)
public class CustomTabsMixin {
    @Inject(method = "lambda$registerCreativeTab$1", at=@At(value = "TAIL"), remap = false)
    private static void clinit(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output, CallbackInfo ci){
        output.accept(ModItemRegistry.POKE_SPAWNER_ITEM.get().getDefaultInstance());
        output.accept(ModItemRegistry.SPAWNER_WAND_ITEM.get().getDefaultInstance());
    }
}
