package com.goodbird.cnpccobblemonaddon.mixin.impl;

import noppes.npcs.constants.EnumGuiType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EnumGuiType.class)
public class EnumGuiTypeMixin {
    @Shadow(remap=false)
    @Final
    @Mutable
    private static EnumGuiType[] $VALUES;

    private static final EnumGuiType CobbleSpawnerMainSettings = cnpcCobblemonAddoncnpcCobblemonAddon$addVariant("CobbleSpawnerMainSettings", false);
    private static final EnumGuiType CobbleSpawnerEntrySettings = cnpcCobblemonAddoncnpcCobblemonAddon$addVariant("CobbleSpawnerEntrySettings", true);
    private static final EnumGuiType CobbleSpawnerEntryLootSettings = cnpcCobblemonAddoncnpcCobblemonAddon$addVariant("CobbleSpawnerEntryLootSettings", true);
    private static final EnumGuiType CobbleSpawnerSpawnSettings = cnpcCobblemonAddoncnpcCobblemonAddon$addVariant("CobbleSpawnerSpawnSettings", false);
    private static final EnumGuiType CobbleSpawnerPokemonList = cnpcCobblemonAddoncnpcCobblemonAddon$addVariant("CobbleSpawnerPokemonList", false);
    private static final EnumGuiType CobbleSpawnerSpawnerList = cnpcCobblemonAddoncnpcCobblemonAddon$addVariant("CobbleSpawnerSpawnerList", false);

    @Invoker("<init>")
    private static EnumGuiType cnpcCobblemonAddon$invokeInit(String internalName, int internalId, boolean hasContainer) {
        throw new AssertionError();
    }

    private static EnumGuiType cnpcCobblemonAddoncnpcCobblemonAddon$addVariant(String internalName, boolean hasContainer) {
        EnumGuiType instrument = cnpcCobblemonAddon$invokeInit(internalName, EnumGuiTypeMixin.$VALUES[EnumGuiTypeMixin.$VALUES.length - 1].ordinal() + 1, hasContainer);
        EnumGuiTypeMixin.$VALUES = addToArray(EnumGuiTypeMixin.$VALUES, instrument);
        return instrument;
    }

    private static EnumGuiType[] addToArray(EnumGuiType[] array, EnumGuiType type){
        EnumGuiType[] newArray = new EnumGuiType[array.length+1];
        newArray[array.length] = type;
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }
}