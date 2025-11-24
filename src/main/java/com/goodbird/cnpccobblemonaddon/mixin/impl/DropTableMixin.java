package com.goodbird.cnpccobblemonaddon.mixin.impl;

import com.cobblemon.mod.common.api.drop.DropEntry;
import com.cobblemon.mod.common.api.drop.DropTable;
import kotlin.ranges.IntRange;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(DropTable.class)
public interface DropTableMixin {
    @Accessor(remap=false)
    List<DropEntry> getEntries();

    @Accessor(remap=false)
    @Mutable
    void setAmount(IntRange range);
}
