package com.goodbird.cnpccobblemonaddon.network;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketRemotePokemonTp extends PacketServerBasic {
    private int entityId;

    public SPacketRemotePokemonTp(int entityId) {
        this.entityId = entityId;
    }

    public static void encode(SPacketRemotePokemonTp msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
    }

    public static SPacketRemotePokemonTp decode(FriendlyByteBuf buf) {
        return new SPacketRemotePokemonTp(buf.readInt());
    }

    protected void handle() {
        Entity entity = this.player.level().getEntity(this.entityId);
        if (entity instanceof PokemonEntity pokemon) {
            this.player.connection.teleport(pokemon.getX(), pokemon.getY(), pokemon.getZ(), 0.0F, 0.0F);
        }
    }
}
