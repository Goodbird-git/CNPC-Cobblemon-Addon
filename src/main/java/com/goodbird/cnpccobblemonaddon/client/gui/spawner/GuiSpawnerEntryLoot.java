package com.goodbird.cnpccobblemonaddon.client.gui.spawner;

import com.goodbird.cnpccobblemonaddon.block.tile.PokemonSpawnerTile;
import com.goodbird.cnpccobblemonaddon.client.gui.components.GuiFixedTextFieldNop;
import com.goodbird.cnpccobblemonaddon.client.gui.container.spawner.ContainerSpawnerEntry;
import com.goodbird.cnpccobblemonaddon.client.gui.container.spawner.ContainerSpawnerEntryLoot;
import com.goodbird.cnpccobblemonaddon.constants.CMEnumGuiType;
import com.goodbird.cnpccobblemonaddon.data.PokemonData;
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

public class GuiSpawnerEntryLoot extends GuiContainerBasePokemon<ContainerSpawnerEntryLoot> implements IGuiData {
    private PokemonSpawnerTile tile;
    private PokemonData data;
    private int row;

    public GuiSpawnerEntryLoot(ContainerSpawnerEntryLoot container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
        this.row = container.row;
        this.tile = (PokemonSpawnerTile)this.player.level().getBlockEntity(container.pos);
        this.data = tile.getData().getSpawnEntries().get(row).getPokemon();
    }

    public void init() {
        super.init();
        this.addLabel(new GuiLabel(0, "Item", this.guiLeft + 17, this.guiTop + 20, 0x854a13));
        this.addLabel(new GuiLabel(1, "Chance", this.guiLeft + 47, this.guiTop + 20, 0x854a13));
        this.addLabel(new GuiLabel(2, "Quantity Range", this.guiLeft + 105, this.guiTop + 20, 0x854a13));

        int textFieldId = 0;
        for(int i=0;i<9;i++){
            int finalI = i;
            GuiFixedTextFieldNop field = new GuiFixedTextFieldNop(++textFieldId, this, this.guiLeft + 42,
                    this.guiTop + i * 20 + 39, 50, 20, "" + data.getDropData().getDropItems()[i].getChance());
            field.floatsOnly = true;
            field.setMinMaxDefault(0, 100, 0);
            field.listener = (f)-> data.getDropData().getDropItems()[finalI].setChance(f.getFloat());
            field.setFGColor(0x854a13);
            this.addTextField(field);

            this.addLabel(new GuiLabel(5+i, "%", this.guiLeft + 94, this.guiTop + i * 20 + 45, 0x854a13));

            field = new GuiFixedTextFieldNop(++textFieldId, this, this.guiLeft + 110,
                    this.guiTop + i * 20 + 39, 30, 20, "" + data.getDropData().getDropItems()[i].getMinQuantity());
            field.numbersOnly = true;
            field.setMinMaxDefault(0, 64, 0);
            field.listener = (f)-> data.getDropData().getDropItems()[finalI].setMinQuantity(f.getInteger());
            field.setFGColor(0x854a13);
            this.addTextField(field);

            field = new GuiFixedTextFieldNop(++textFieldId, this, this.guiLeft + 142,
                    this.guiTop + i * 20 + 39, 30, 20, "" + data.getDropData().getDropItems()[i].getMaxQuantity());
            field.numbersOnly = true;
            field.setMinMaxDefault(0, 64, 0);
            field.listener = (f)-> data.getDropData().getDropItems()[finalI].setMaxQuantity(f.getInteger());
            field.setFGColor(0x854a13);
            this.addTextField(field);
        }
        this.addLabel(new GuiLabel(15, "Natural Loot", this.guiLeft + 195, this.guiTop + 20, 0x854a13));
        this.addButton(new GuiButtonYesNo(this, 1, this.guiLeft + 202, this.guiTop + 40, 40, 20, data.getDropData().isDropNaturalLoot(), (b)->{
            data.getDropData().setDropNaturalLoot(((GuiButtonYesNo)b).getBoolean());
        }));
        this.getButton(1).setFGColor(0x854a13);
        this.addLabel(new GuiLabel(16, "Total Quantity Range", this.guiLeft + 195, this.guiTop + 65, 0x854a13));

        GuiFixedTextFieldNop field = new GuiFixedTextFieldNop(++textFieldId, this, this.guiLeft + 195,
                this.guiTop + 80, 30, 20, "" + data.getDropData().getMinTotalLoot());
        field.numbersOnly = true;
        field.setMinMaxDefault(0, 1000, 1);
        field.listener = (f)-> data.getDropData().setMinTotalLoot(f.getInteger());
        field.setFGColor(0x854a13);
        this.addTextField(field);

        field = new GuiFixedTextFieldNop(++textFieldId, this, this.guiLeft + 235,
                this.guiTop + 80, 40, 20, "" + data.getDropData().getMaxTotalLoot());
        field.numbersOnly = true;
        field.setMinMaxDefault(0, 1000, 1);
        field.listener = (f)-> data.getDropData().setMaxTotalLoot(f.getInteger());
        field.setFGColor(0x854a13);
        this.addTextField(field);

        GuiButtonNop btnBack = new GuiButtonNop(this, 2, this.guiLeft + 330, this.guiTop + 20, 60, 20, "Back", (b)->{
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
