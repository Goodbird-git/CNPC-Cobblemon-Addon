package com.goodbird.cnpccobblemonaddon.network;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.goodbird.cnpccobblemonaddon.block.tile.PokemonSpawnerTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketRemoteSpawnerTp extends PacketServerBasic {
    private long spawnerPos;

    public SPacketRemoteSpawnerTp(long spawnerPos) {
        this.spawnerPos = spawnerPos;
    }

    public static void encode(SPacketRemoteSpawnerTp msg, FriendlyByteBuf buf) {
        buf.writeLong(msg.spawnerPos);
    }

    public static SPacketRemoteSpawnerTp decode(FriendlyByteBuf buf) {
        return new SPacketRemoteSpawnerTp(buf.readLong());
    }

    protected void handle() {
        BlockPos pos = BlockPos.of(spawnerPos);
        BlockEntity entity = this.player.level().getBlockEntity(pos);
        if (entity instanceof PokemonSpawnerTile) {
            this.player.connection.teleport(pos.getX(), pos.getY(), pos.getZ(), 0.0F, 0.0F);
        }
    }
}
