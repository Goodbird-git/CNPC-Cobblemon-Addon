package com.goodbird.cnpccobblemonaddon.client.gui.spawner;

import com.goodbird.cnpccobblemonaddon.block.tile.PokemonSpawnerTile;
import com.goodbird.cnpccobblemonaddon.client.gui.components.GuiFixedTextFieldNop;
import com.goodbird.cnpccobblemonaddon.client.gui.container.spawner.ContainerSpawnerEntry;
import com.goodbird.cnpccobblemonaddon.constants.CMEnumGuiType;
import com.goodbird.cnpccobblemonaddon.data.PokemonData;
import com.goodbird.cnpccobblemonaddon.network.SPacketRemotePokemonsDespawn;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketGuiOpen;
import noppes.npcs.packets.server.SPacketTileEntitySave;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiButtonYesNo;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.listeners.IGuiData;

public class GuiSpawnerEntry extends GuiContainerBasePokemon<ContainerSpawnerEntry> implements IGuiData {
    private PokemonSpawnerTile tile;
    private PokemonData data;
    private int row;

    public GuiSpawnerEntry(ContainerSpawnerEntry container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
        this.row = container.row;
        this.tile = (PokemonSpawnerTile)this.player.level().getBlockEntity(container.pos);
        this.data = tile.getData().getSpawnEntries().get(row).getPokemon();
    }

    public void init() {
        super.init();
        this.addLabel(new GuiLabel(0, "Name", this.guiLeft + 20, this.guiTop + 20, 0x854a13));
        this.addTextField(new GuiFixedTextFieldNop(0, this, this.guiLeft + 18, this.guiTop + 36, 160, 20, data.getSpecies().toString()));
        getTextField(0).listener = (f)-> data.setSpecies(new ResourceLocation(f.getValue()));
        getTextField(0).setFGColor(0x854a13);
        this.addLabel(new GuiLabel(1, "Level Range", this.guiLeft + 222, this.guiTop + 20, 0x854a13));
        this.addTextField(new GuiFixedTextFieldNop(1, this, this.guiLeft + 218, this.guiTop + 36, 30, 20, "" + data.getLevelMin()));
        this.addTextField(new GuiFixedTextFieldNop(2, this, this.guiLeft + 252, this.guiTop + 36, 30, 20, "" + data.getLevelMax()));
        this.getTextField(1).numbersOnly = true;
        this.getTextField(1).setMinMaxDefault(0, 100, 0);
        getTextField(1).listener = (f)-> data.setLevelMin(f.getInteger());
        getTextField(1).setFGColor(0x854a13);
        this.getTextField(2).numbersOnly = true;
        this.getTextField(2).setMinMaxDefault(0, 100, 100);
        getTextField(2).listener = (f)-> data.setLevelMax(f.getInteger());
        getTextField(2).setFGColor(0x854a13);
        this.addLabel(new GuiLabel(2, "Is Shiny?", this.guiLeft + 296, this.guiTop + 20, 0x854a13));
        this.addButton(new GuiButtonYesNo(this, 0, this.guiLeft + 298, this.guiTop + 36, 40, 20, data.isShiny(), (b)->{
            data.setShiny(((GuiButtonYesNo)b).getBoolean());
        }));
        this.getButton(0).setFGColor(0x854a13);


        this.addLabel(new GuiLabel(3, "EV (0-255)", this.guiLeft + 245, this.guiTop + 100, 0x854a13));
        this.addLabel(new GuiLabel(4, "IV (0-31)", this.guiLeft + 330, this.guiTop + 100, 0x854a13));

        this.addLabel(new GuiLabel(5, "HP", this.guiLeft + 190, this.guiTop + 116, 0x854a13));
        this.addLabel(new GuiLabel(6, "ATK", this.guiLeft + 190, this.guiTop + 116 + 20, 0x854a13));
        this.addLabel(new GuiLabel(7, "DEF", this.guiLeft + 190, this.guiTop + 116 + 2 * 20, 0x854a13));
        this.addLabel(new GuiLabel(8, "SPE ATK", this.guiLeft + 190, this.guiTop + 116 + 3 * 20, 0x854a13));
        this.addLabel(new GuiLabel(9, "SPE DEF", this.guiLeft + 190, this.guiTop + 116 + 4 * 20, 0x854a13));
        this.addLabel(new GuiLabel(10, "SPEED", this.guiLeft + 190, this.guiTop + 116 + 5 * 20, 0x854a13));

        int textFieldId = 2;
        for (int i = 0; i < 6; i++) {
            int finalI = i;
            GuiFixedTextFieldNop field = new GuiFixedTextFieldNop(++textFieldId, this, this.guiLeft + 242, this.guiTop + i * 20 + 110, 30, 20, "" + data.getEVMin(i));
            field.numbersOnly = true;
            field.setMinMaxDefault(0, 255, 0);
            field.listener = (f)-> data.setEVMin(finalI, f.getInteger());
            field.setFGColor(0x854a13);
            this.addTextField(field);
            field = new GuiFixedTextFieldNop(++textFieldId, this, this.guiLeft + 275, this.guiTop + i * 20 + 110, 30, 20, "" + data.getEVMax(i));
            field.numbersOnly = true;
            field.setMinMaxDefault(0, 255, 0);
            field.listener = (f)-> data.setEVMax(finalI, f.getInteger());
            field.setFGColor(0x854a13);
            this.addTextField(field);
        }

        for (int i = 0; i < 6; i++) {
            int finalI = i;
            GuiFixedTextFieldNop field = new GuiFixedTextFieldNop(++textFieldId, this, this.guiLeft + 325, this.guiTop + i * 20 + 110, 30, 20, "" + data.getIVMin(i));
            field.numbersOnly = true;
            field.setMinMaxDefault(-1, 31, -1);
            field.listener = (f)-> data.setIVMin(finalI, f.getInteger());
            field.setFGColor(0x854a13);
            this.addTextField(field);
            field = new GuiFixedTextFieldNop(++textFieldId, this, this.guiLeft + 358, this.guiTop + i * 20 + 110, 30, 20, "" + data.getIVMax(i));
            field.numbersOnly = true;
            field.setMinMaxDefault(-1, 31, -1);
            field.listener = (f)-> data.setIVMax(finalI, f.getInteger());
            field.setFGColor(0x854a13);
            this.addTextField(field);
        }

        for (int i = 0; i < 3; i++) {
            int finalI = i;
            this.addTextField(new GuiFixedTextFieldNop(27 + i, this, this.guiLeft + 184 + i * 75, this.guiTop + 70, 40, 20, "" + data.getHeldItem(i).getChance()));
            this.getTextField(27 + i).floatsOnly = true;
            this.getTextField(27 + i).setMinMaxDefault(0, 100, 0);
            this.getTextField(27 + i).listener = (f)-> data.getHeldItem(finalI).setChance(f.getFloat());
            this.getTextField(27 + i).setFGColor(0x854a13);
            this.addLabel(new GuiLabel(11+i, "%", this.guiLeft + 226 + i * 75, this.guiTop + 77, 0x854a13));
        }

        this.addLabel(new GuiLabel(14, "Uncatchable", this.guiLeft + 18, this.guiTop + 67, 0x854a13));
        this.addLabel(new GuiLabel(15, "Untradable", this.guiLeft + 18, this.guiTop + 87, 0x854a13));
        this.addLabel(new GuiLabel(16, "Unbattlable", this.guiLeft + 18, this.guiTop + 107, 0x854a13));

        this.addButton(new GuiButtonYesNo(this, 1, this.guiLeft + 85, this.guiTop + 60, 40, 20, data.isUncatchable(), (b)->{
            data.setUncatchable(((GuiButtonYesNo)b).getBoolean());
        }));
        this.getButton(1).setFGColor(0x854a13);
        this.addButton(new GuiButtonYesNo(this, 2, this.guiLeft + 85, this.guiTop + 80, 40, 20, data.isUntradable(), (b)->{
            data.setUntradable(((GuiButtonYesNo)b).getBoolean());
        }));
        this.getButton(2).setFGColor(0x854a13);
        this.addButton(new GuiButtonYesNo(this, 3, this.guiLeft + 85, this.guiTop + 100, 40, 20, data.isUnbattlable(), (b)->{
            data.setUnbattlable(((GuiButtonYesNo)b).getBoolean());
        }));
        this.getButton(3).setFGColor(0x854a13);

        GuiButtonNop btnBack = new GuiButtonNop(this, 4, this.guiLeft + 350, this.guiTop + 20, 40, 20, "Back", (b)->{
            save();
            Packets.sendServer(new SPacketGuiOpen(CMEnumGuiType.CobbleSpawnerMainSettings, tile.getBlockPos()));
        });
        btnBack.setFGColor(0x854a13);
        addButton(btnBack);
    }

    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 0) {
            this.close();
        }
    }

    public void save() {
        if (this.tile != null) {
            Packets.sendServer(new SPacketTileEntitySave(this.tile.saveWithFullMetadata()));
        }
    }

    public void setGuiData(CompoundTag compound) {
        this.tile.load(compound);
        this.init();
    }
}