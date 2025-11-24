package com.goodbird.cnpccobblemonaddon.network;

import com.goodbird.cnpccobblemonaddon.block.tile.PokemonSpawnerTile;
import com.goodbird.cnpccobblemonaddon.mixin.impl.ChunkMapMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiData;

public class SPacketGetSpawnerList extends PacketServerBasic {
    public SPacketGetSpawnerList() {
    }

    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_GUI;
    }

    public static void encode(SPacketGetSpawnerList msg, FriendlyByteBuf buf) {
    }

    public static SPacketGetSpawnerList decode(FriendlyByteBuf buf) {
        return new SPacketGetSpawnerList();
    }

    protected void handle() {
        sendSpawnerList(this.player);
    }

    public void sendSpawnerList(ServerPlayer player) {
        CompoundTag tagToSend = new CompoundTag();
        ListTag list = new ListTag();
        for(ChunkHolder chunkHolder : ((ChunkMapMixin)((ServerLevel) player.level()).getChunkSource().chunkMap).invokeGetChunks()) {
            if(chunkHolder.getLastAvailable()==null || chunkHolder.getLastAvailable().getBlockEntitiesPos().size()==0) continue;
            for (BlockPos pos : chunkHolder.getLastAvailable().getBlockEntitiesPos()){
                BlockEntity entity = chunkHolder.getLastAvailable().getBlockEntity(pos);
                if (entity instanceof PokemonSpawnerTile spawnerTile) {
                    double distance = player.distanceToSqr(spawnerTile.getBlockPos().getCenter());
                    CompoundTag spawnerData = new CompoundTag();
                    spawnerData.putString("Name", spawnerTile.getData().getName());
                    spawnerData.putDouble("Distance", distance);
                    spawnerData.putLong("Pos", spawnerTile.getBlockPos().asLong());
                    list.add(spawnerData);
                }
            }
        }

        tagToSend.put("data", list);
        Packets.send(player, new PacketGuiData(tagToSend));
    }
}
