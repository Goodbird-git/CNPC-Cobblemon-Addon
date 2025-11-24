package com.goodbird.cnpccobblemonaddon.client.gui.spawner;

import com.goodbird.cnpccobblemonaddon.client.gui.components.GuiListNop;
import com.goodbird.cnpccobblemonaddon.constants.CMEnumGuiType;
import com.goodbird.cnpccobblemonaddon.network.SPacketGetSpawnerList;
import com.goodbird.cnpccobblemonaddon.network.SPacketRemotePokemonTp;
import com.goodbird.cnpccobblemonaddon.network.SPacketRemotePokemonsDespawn;
import com.goodbird.cnpccobblemonaddon.network.SPacketRemoteSpawnerTp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketGuiOpen;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.listeners.IGuiData;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GuiSpawnerList extends GuiBasePokemon implements IGuiData {
    private GuiListNop guiListNop;
    private CompoundTag spawnerList;

    public GuiSpawnerList() {
        Packets.sendServer(new SPacketGetSpawnerList());
    }

    public void init() {
        super.init();

        this.addLabel(new GuiLabel(2, "Name", this.guiLeft + 40, this.guiTop + 76, 0x854a13));
        this.addLabel(new GuiLabel(3, "Distance", this.guiLeft + 110, this.guiTop + 76, 0x854a13));
        this.addLabel(new GuiLabel(5, "Position", this.guiLeft + 160, this.guiTop + 76, 0x854a13));

        guiListNop = new GuiListNop(Minecraft.getInstance(), width, 100, guiTop+90, guiTop+215, 25);
        guiListNop.setRenderBackground(false);
        guiListNop.setRenderTopAndBottom(false);
        fillList();
        this.addRenderableWidget(guiListNop);
    }

    public void fillList(){
        if(spawnerList ==null || !spawnerList.contains("data")) return;
        guiListNop.clearEntries();
        ListTag list = spawnerList.getList("data", 10);

        for(int i=0;i<list.size();i++){
            ArrayList<AbstractWidget> widgets = new ArrayList<>();
            CompoundTag spawner = list.getCompound(i);
            widgets.add(new GuiLabel(8, spawner.getString("Name"), this.guiLeft + 25, 0, 0x854a13));
            DecimalFormat df = new DecimalFormat("#.#");
            widgets.add(new GuiLabel(9, df.format(spawner.getDouble("Distance")), this.guiLeft + 115, 0, 0x854a13));
            BlockPos pos = BlockPos.of(spawner.getLong("Pos"));
            String posStr = "("+pos.getX()+", "+pos.getY()+", "+pos.getZ()+")";
            widgets.add(new GuiLabel(11, posStr, this.guiLeft + 155, 0, 0x854a13));
            GuiButtonNop btnTp = new GuiButtonNop(this, 5, this.guiLeft + 255, 0, 60, 20, "Teleport", (b)->{
                Packets.sendServer(new SPacketRemoteSpawnerTp(spawner.getLong("Pos")));
                close();
            });
            btnTp.setFGColor(0x854a13);
            widgets.add(btnTp);

            GuiButtonNop btnEdit = new GuiButtonNop(this, 5, this.guiLeft + 325, 0, 40, 20, "Edit", (b)->{
                Packets.sendServer(new SPacketGuiOpen(CMEnumGuiType.CobbleSpawnerMainSettings, pos));
            });
            btnEdit.setFGColor(0x854a13);
            widgets.add(btnEdit);
            guiListNop.addEntry(new GuiListNop.Entry(null, widgets));
        }
    }

    @Override
    public boolean charTyped(char c, int i) {
        return super.charTyped(c, i);
    }

    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 0) {
            this.close();
        }
    }

    public void save() {
    }

    public void setGuiData(CompoundTag compound) {
        this.spawnerList = compound;
        this.init();
    }
}