package com.goodbird.cnpccobblemonaddon.mixin.impl;

import com.goodbird.cnpccobblemonaddon.registry.ModItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import noppes.npcs.packets.server.SPacketTileEntitySave;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SPacketTileEntitySave.class)
public class SPacketTileEntitySaveMixin {
    @Inject(method = "toolAllowed", at=@At("HEAD"), cancellable = true, remap = false)
    public void toolAllowed(ItemStack item, CallbackInfoReturnable<Boolean> cir) {
        if(item.getItem() == ModItemRegistry.POKE_SPAWNER_ITEM.get() || item.getItem() == ModItemRegistry.SPAWNER_WAND_ITEM.get()) cir.setReturnValue(true);
    }

    @Inject(method = "saveTileEntity", at = @At("TAIL"), remap = false)
    private static void saveTileEntity(ServerPlayer player, CompoundTag compound, CallbackInfoReturnable<BlockEntity> cir){
        int x = compound.getInt("x");
        int y = compound.getInt("y");
        int z = compound.getInt("z");
        player.level().blockEntityChanged(new BlockPos(x, y, z));
    }
}
