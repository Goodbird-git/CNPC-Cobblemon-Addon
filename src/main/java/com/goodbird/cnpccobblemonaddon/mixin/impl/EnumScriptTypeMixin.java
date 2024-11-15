package com.goodbird.cnpccobblemonaddon.mixin.impl;

import noppes.npcs.constants.EnumScriptType;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(EnumScriptType.class)
public class EnumScriptTypeMixin {
    @Shadow
    @Final
    @Mutable
    private static EnumScriptType[] $VALUES;

    private static final EnumScriptType POKEMON_CATCH = cnpcCobblemonAddoncnpcCobblemonAddon$addVariant("POKEMON_CATCH", "pokemonCatch");
    private static final EnumScriptType POKEMON_FAINT = cnpcCobblemonAddoncnpcCobblemonAddon$addVariant("POKEMON_FAINT", "pokemonFaint");
    private static final EnumScriptType POKEMON_BATTLE_START = cnpcCobblemonAddoncnpcCobblemonAddon$addVariant("POKEMON_BATTLE_START", "pokemonBattleStart");
    private static final EnumScriptType POKEMON_BATTLE_END = cnpcCobblemonAddoncnpcCobblemonAddon$addVariant("POKEMON_BATTLE_END", "pokemonBattleEnd");

    @Invoker("<init>")
    private static EnumScriptType cnpcCobblemonAddon$invokeInit(String internalName, int internalId, String name) {
        throw new AssertionError();
    }

    @Inject(method = "<clinit>", at=@At("TAIL"))
    private static void onClInit(CallbackInfo ci){
        EnumScriptType.playerScripts = addToArray(EnumScriptType.playerScripts, POKEMON_CATCH);
        //EnumScriptType.playerScripts = addToArray(EnumScriptType.playerScripts, POKEMON_FAINT);
        //EnumScriptType.playerScripts = addToArray(EnumScriptType.playerScripts, POKEMON_BATTLE_START);
        //EnumScriptType.playerScripts = addToArray(EnumScriptType.playerScripts, POKEMON_BATTLE_END);
    }

    private static EnumScriptType cnpcCobblemonAddoncnpcCobblemonAddon$addVariant(String internalName, String name) {
        EnumScriptType instrument = cnpcCobblemonAddon$invokeInit(internalName, EnumScriptTypeMixin.$VALUES[EnumScriptTypeMixin.$VALUES.length - 1].ordinal() + 1, name);
        EnumScriptTypeMixin.$VALUES = addToArray(EnumScriptTypeMixin.$VALUES, instrument);
        return instrument;
    }

    private static EnumScriptType[] addToArray(EnumScriptType[] array, EnumScriptType type){
        EnumScriptType[] newArray = new EnumScriptType[array.length+1];
        newArray[array.length] = type;
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }
}
