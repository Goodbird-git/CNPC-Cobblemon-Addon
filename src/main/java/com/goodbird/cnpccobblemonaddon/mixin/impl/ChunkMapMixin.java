package com.goodbird.cnpccobblemonaddon.mixin.impl;

import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkMap.class)
public interface ChunkMapMixin {
    @Invoker
    Iterable<ChunkHolder> invokeGetChunks();
}
