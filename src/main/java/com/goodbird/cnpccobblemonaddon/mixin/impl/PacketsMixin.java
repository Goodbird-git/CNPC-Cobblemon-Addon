package com.goodbird.cnpccobblemonaddon.mixin.impl;

import com.goodbird.cnpccobblemonaddon.network.*;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Packets.class)
public class PacketsMixin {

    @Inject(method = "register", at=@At("TAIL"), remap = false)
    private static void register(CallbackInfo ci) {
        Packets.Channel.registerMessage(Packets.index++, SPacketRowEditGuiOpen.class, SPacketRowEditGuiOpen::encode, SPacketRowEditGuiOpen::decode, PacketServerBasic::handle);
        Packets.Channel.registerMessage(Packets.index++, SPacketGetPokemonList.class, SPacketGetPokemonList::encode, SPacketGetPokemonList::decode, PacketServerBasic::handle);
        Packets.Channel.registerMessage(Packets.index++, SPacketRemotePokemonTp.class, SPacketRemotePokemonTp::encode, SPacketRemotePokemonTp::decode, PacketServerBasic::handle);
        Packets.Channel.registerMessage(Packets.index++, SPacketRemotePokemonsDespawn.class, SPacketRemotePokemonsDespawn::encode, SPacketRemotePokemonsDespawn::decode, PacketServerBasic::handle);
        Packets.Channel.registerMessage(Packets.index++, SPacketGetSpawnerList.class, SPacketGetSpawnerList::encode, SPacketGetSpawnerList::decode, PacketServerBasic::handle);
        Packets.Channel.registerMessage(Packets.index++, SPacketRemoteSpawnerTp.class, SPacketRemoteSpawnerTp::encode, SPacketRemoteSpawnerTp::decode, PacketServerBasic::handle);
    }
}
