package com.goodbird.cnpccobblemonaddon.mixin.impl;

import com.goodbird.cnpccobblemonaddon.registry.ModItemRegistry;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomItems;
import noppes.npcs.packets.PacketServerBasic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PacketServerBasic.class)
public class PacketServerBasicMixin {
    @Inject(method = "toolAllowed", at=@At("RETURN"), cancellable = true, remap = false)
    public void toolAllowed(ItemStack item, CallbackInfoReturnable<Boolean> cir) {
        if(item.getItem()== ModItemRegistry.SPAWNER_WAND_ITEM.get()){
            cir.setReturnValue(true);
        }
    }
}
