package com.goodbird.cnpccobblemonaddon.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.util.CustomNPCsScheduler;

public class SPacketRowEditGuiOpen extends PacketServerBasic {
    private EnumGuiType type;
    private BlockPos pos;
    private int row;

    public SPacketRowEditGuiOpen(EnumGuiType type, BlockPos pos, int row) {
        this.type = type;
        this.pos = pos;
        this.row = row;
    }

    public static void encode(SPacketRowEditGuiOpen msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.type);
        buf.writeBlockPos(msg.pos);
        buf.writeInt(msg.row);
    }

    public static SPacketRowEditGuiOpen decode(FriendlyByteBuf buf) {
        return new SPacketRowEditGuiOpen(buf.readEnum(EnumGuiType.class), buf.readBlockPos(), buf.readInt());
    }

    protected void handle() {
        sendOpenGui(this.player, this.type, this.pos, this.row);
    }

    public static void sendOpenGui(Player player, EnumGuiType gui, BlockPos pos, int row) {
        if (player instanceof ServerPlayer) {
            CustomNPCsScheduler.runTack(() -> {
                player.getServer().submit(() -> {
                    NoppesUtilServer.openContainerGui((ServerPlayer)player, gui, (buffer) -> {
                        buffer.writeLong(pos.asLong());
                        buffer.writeInt(row);
                    });
                });
            }, 200);
        }
    }

}
