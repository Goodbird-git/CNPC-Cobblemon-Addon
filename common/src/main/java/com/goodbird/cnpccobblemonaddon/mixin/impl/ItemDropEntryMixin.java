package com.goodbird.cnpccobblemonaddon.mixin.impl;

import com.cobblemon.mod.common.api.drop.ItemDropEntry;
import kotlin.ranges.IntRange;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemDropEntry.class)
public interface ItemDropEntryMixin {
    @Accessor(remap=false)
    @Mutable
    void setPercentage(float f);
    @Accessor(remap=false)
    @Mutable
    void setQuantityRange(IntRange range);
    @Accessor(remap=false)
    @Mutable
    void setItem(ResourceLocation resourceLocation);
    @Accessor(remap=false)
    @Mutable
    void setComponents(DataComponentMap tag);
}
