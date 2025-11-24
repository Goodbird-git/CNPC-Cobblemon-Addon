package com.goodbird.cnpccobblemonaddon.client.gui.spawner;

import com.goodbird.cnpccobblemonaddon.block.tile.PokemonSpawnerTile;
import com.goodbird.cnpccobblemonaddon.client.gui.components.GuiFixedTextFieldNop;
import com.goodbird.cnpccobblemonaddon.client.gui.components.GuiListNop;
import com.goodbird.cnpccobblemonaddon.constants.CMEnumGuiType;
import com.goodbird.cnpccobblemonaddon.constants.EnumSpawnTime;
import com.goodbird.cnpccobblemonaddon.data.PokemonSpawnerData;
import com.goodbird.cnpccobblemonaddon.network.SPacketRemotePokemonsDespawn;
import com.goodbird.cnpccobblemonaddon.network.SPacketRowEditGuiOpen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketGuiOpen;
import noppes.npcs.packets.server.SPacketTileEntityGet;
import noppes.npcs.packets.server.SPacketTileEntitySave;
import noppes.npcs.shared.client.gui.components.*;
import noppes.npcs.shared.client.gui.listeners.IGuiData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GuiMainSettings extends GuiBasePokemon implements IGuiData {
    private final PokemonSpawnerTile tile;
    private GuiListNop list;

    public GuiMainSettings(BlockPos pos) {
        this.tile = (PokemonSpawnerTile)this.player.level().getBlockEntity(pos);
        Packets.sendServer(new SPacketTileEntityGet(pos));
    }

    public void init() {
        super.init();
        this.addLabel(new GuiLabel(1, "Spawner name:", this.guiLeft + 30, this.guiTop + 26, 0x854a13));

        addTextField(new GuiFixedTextFieldNop(1, this, this.guiLeft + 120, this.guiTop + 20, 150, 20, tile.getData().getName()));
        getTextField(1).setFGColor(0x854a13);
        getTextField(1).listener = (f)-> tile.getData().setName(f.getValue());

        addButton(getAddEntryButton());

        addTextField(new GuiFixedTextFieldNop(2, this, this.guiLeft + 30, this.guiTop + 50, 150, 20, ""));
        getTextField(2).setFGColor(0x854a13);
        addButton(getAddEntryButton());

        GuiButtonNop btnSettingsEdit = new GuiButtonNop(this, 3, this.guiLeft + 255, this.guiTop + 50, 50, 20, "Settings", (b)->{
            save();
            Packets.sendServer(new SPacketGuiOpen(CMEnumGuiType.CobbleSpawnerSpawnSettings, tile.getBlockPos()));
        });
        btnSettingsEdit.setFGColor(0x854a13);
        addButton(btnSettingsEdit);

        GuiButtonNop btnPokemons = new GuiButtonNop(this, 4, this.guiLeft + 315, this.guiTop + 50, 50, 20, "Pokemons", (b)->{
            save();
            Packets.sendServer(new SPacketGuiOpen(CMEnumGuiType.CobbleSpawnerPokemonList, tile.getBlockPos()));
        });
        btnPokemons.setFGColor(0x854a13);
        addButton(btnPokemons);

        this.addLabel(new GuiLabel(2, "Name", this.guiLeft + 40, this.guiTop + 76, 0x854a13));
        this.addLabel(new GuiLabel(3, "Level", this.guiLeft + 110, this.guiTop + 76, 0x854a13));
        this.addLabel(new GuiLabel(4, "Time", this.guiLeft + 155, this.guiTop + 76, 0x854a13));
        this.addLabel(new GuiLabel(5, "Weight", this.guiLeft + 205, this.guiTop + 76, 0x854a13));
        this.addLabel(new GuiLabel(6, "Active", this.guiLeft + 255, this.guiTop + 76, 0x854a13));

        list = new GuiListNop(Minecraft.getInstance(), width, 100, guiTop+90, guiTop+215, 25);
        list.setRenderBackground(false);
        list.setRenderTopAndBottom(false);
        fillList();
        this.addRenderableWidget(list);
    }

    @NotNull
    private GuiButtonNop getAddEntryButton() {
        GuiButtonNop btnAdd = new GuiButtonNop(this, 2, this.guiLeft + 190, this.guiTop + 50, 40, 20, "Add", (b)->{
            String entryName = getTextField(2).getValue();
            ArrayList<PokemonSpawnerData.PokemonSpawnEntry> entries = tile.getData().getSpawnEntries();
            for (PokemonSpawnerData.PokemonSpawnEntry entry : entries) {
                if (entry.getEntryName().equals(entryName)) {
                    return;
                }
            }
            entries.add(new PokemonSpawnerData.PokemonSpawnEntry(entryName));
            fillList();
        });
        btnAdd.setFGColor(0x854a13);
        return btnAdd;
    }

    public void fillList(){
        list.clearEntries();
        ArrayList<PokemonSpawnerData.PokemonSpawnEntry> entries = tile.getData().getSpawnEntries();
        for(int i=0;i<entries.size();i++){
            PokemonSpawnerData.PokemonSpawnEntry entry = entries.get(i);
            ArrayList<AbstractWidget> widgets = new ArrayList<>();
            widgets.add(new GuiLabel(8, entry.getEntryName(), this.guiLeft + 25, 0, 0x854a13));
            String range = entry.getPokemon().getLevelMin()+"-"+entry.getPokemon().getLevelMax();
            widgets.add(new GuiLabel(9, range, this.guiLeft + 105, 0, 0x854a13));
            GuiButtonNop btnTime = new GuiButtonNop(this, 3, this.guiLeft + 147, 0, 44, 20, (b)->{
                entry.setSpawnTime(EnumSpawnTime.values()[((GuiButtonNop)b).getValue()]);
            }, entry.getSpawnTime().ordinal(), "All","Morning", "Day", "Evening", "Night");
            btnTime.setFGColor(0x854a13);
            widgets.add(btnTime);
            GuiTextFieldNop chanceField = new GuiFixedTextFieldNop(3, this, this.guiLeft + 202, 0, 40, 20, ""+entry.getWeight());
            chanceField.setFloatsOnly();
            chanceField.setFGColor(0x854a13);
            chanceField.listener = (f)-> entry.setWeight(f.getFloat());
            addTextField(chanceField);
            widgets.add(chanceField);
            GuiButtonNop btnActive = new GuiButtonYesNo(this, 4, this.guiLeft + 255, 0, 30, 20, entry.isActive(), (b)->{
                entry.setActive(((GuiButtonYesNo)b).getBoolean());
            });
            btnActive.setFGColor(0x854a13);
            widgets.add(btnActive);
            final int rowNumber = i;
            GuiButtonNop btnLootEdit = new GuiButtonNop(this, 5, this.guiLeft + 295, 0, 30, 20, "Loot", (b)->{
                save();
                Packets.sendServer(new SPacketRowEditGuiOpen(CMEnumGuiType.CobbleSpawnerEntryLootSettings, tile.getBlockPos(), rowNumber));
            });
            btnLootEdit.setFGColor(0x854a13);
            widgets.add(btnLootEdit);
            GuiButtonNop btnEdit = new GuiButtonNop(this, 6, this.guiLeft + 330, 0, 30, 20, "Edit", (b)->{
                save();
                Packets.sendServer(new SPacketRowEditGuiOpen(CMEnumGuiType.CobbleSpawnerEntrySettings, tile.getBlockPos(), rowNumber));
            });
            btnEdit.setFGColor(0x854a13);
            widgets.add(btnEdit);
            list.addEntry(new GuiListNop.Entry(null, widgets));
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
        if (this.tile != null) {
            Packets.sendServer(new SPacketTileEntitySave(this.tile.saveWithFullMetadata()));
        }
    }

    public void setGuiData(CompoundTag compound) {
        this.tile.load(compound);
        this.init();
    }
}