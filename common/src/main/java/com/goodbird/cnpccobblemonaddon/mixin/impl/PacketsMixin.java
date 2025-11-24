package com.goodbird.cnpccobblemonaddon.mixin.impl;

import com.goodbird.cnpccobblemonaddon.network.*;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static noppes.npcs.packets.Packets.registerPacket;

@Mixin(Packets.class)
public class PacketsMixin {

    @Inject(method = "register", at=@At("TAIL"), remap = false)
    private static void register(CallbackInfo ci) {
        registerPacket(SPacketRowEditGuiOpen.class, SPacketRowEditGuiOpen::encode, SPacketRowEditGuiOpen::decode, PacketServerBasic::handle);
        registerPacket(SPacketGetPokemonList.class, SPacketGetPokemonList::encode, SPacketGetPokemonList::decode, PacketServerBasic::handle);
        registerPacket(SPacketRemotePokemonTp.class, SPacketRemotePokemonTp::encode, SPacketRemotePokemonTp::decode, PacketServerBasic::handle);
        registerPacket(SPacketRemotePokemonsDespawn.class, SPacketRemotePokemonsDespawn::encode, SPacketRemotePokemonsDespawn::decode, PacketServerBasic::handle);
        registerPacket(SPacketGetSpawnerList.class, SPacketGetSpawnerList::encode, SPacketGetSpawnerList::decode, PacketServerBasic::handle);
        registerPacket(SPacketRemoteSpawnerTp.class, SPacketRemoteSpawnerTp::encode, SPacketRemoteSpawnerTp::decode, PacketServerBasic::handle);
    }
}
