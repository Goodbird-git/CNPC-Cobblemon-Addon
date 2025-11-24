package com.goodbird.cnpccobblemonaddon.client.gui.spawner;

import com.goodbird.cnpccobblemonaddon.block.tile.PokemonSpawnerTile;
import com.goodbird.cnpccobblemonaddon.client.gui.components.GuiFixedTextFieldNop;
import com.goodbird.cnpccobblemonaddon.constants.CMEnumGuiType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketGuiOpen;
import noppes.npcs.packets.server.SPacketTileEntityGet;
import noppes.npcs.packets.server.SPacketTileEntitySave;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiButtonYesNo;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.listeners.IGuiData;

public class GuiSpawnSettings extends GuiBasePokemon implements IGuiData {
    private final PokemonSpawnerTile tile;

    public GuiSpawnSettings(BlockPos pos) {
        this.tile = (PokemonSpawnerTile)this.player.level().getBlockEntity(pos);
        Packets.sendServer(new SPacketTileEntityGet(pos));
    }

    public void init() {
        super.init();

        guiTop+=10;
        this.addLabel(new GuiLabel(2, "Spawn Radius", this.guiLeft + 20, this.guiTop + 20, 0x854a13));
        this.addLabel(new GuiLabel(3, "Max Spawn per Round", this.guiLeft + 20, this.guiTop + 42, 0x854a13));
        this.addLabel(new GuiLabel(4, "Max Pokemons Around", this.guiLeft + 20, this.guiTop + 64, 0x854a13));
        this.addLabel(new GuiLabel(5, "Tick Range", this.guiLeft + 20, this.guiTop + 86, 0x854a13));
        this.addLabel(new GuiLabel(6, "Player Detection Range", this.guiLeft + 20, this.guiTop + 108, 0x854a13));
        this.addLabel(new GuiLabel(7, "Redstone Control", this.guiLeft + 20, this.guiTop + 130, 0x854a13));
        this.addLabel(new GuiLabel(8, "Safe Spawn", this.guiLeft + 20, this.guiTop + 152, 0x854a13));
        this.addLabel(new GuiLabel(9, "Allow Water", this.guiLeft + 20, this.guiTop + 174, 0x854a13));
        this.addLabel(new GuiLabel(10, "Can Spawn Under", this.guiLeft + 20, this.guiTop + 196, 0x854a13));

        addTextField(new GuiFixedTextFieldNop(2, this, this.guiLeft + 150, this.guiTop + 15, 50, 20, ""+tile.getData().getSpawnRadius()));
        getTextField(2).setFGColor(0x854a13);
        getTextField(2).setNumbersOnly();
        getTextField(2).listener = (f)->tile.getData().setSpawnRadius(f.getInteger());
        addTextField(new GuiFixedTextFieldNop(3, this, this.guiLeft + 150, this.guiTop + 37, 50, 20, ""+tile.getData().getMaxSpawnsPerRound()));
        getTextField(3).setFGColor(0x854a13);
        getTextField(3).setNumbersOnly();
        getTextField(3).listener = (f)->tile.getData().setMaxSpawnsPerRound(f.getInteger());
        addTextField(new GuiFixedTextFieldNop(4, this, this.guiLeft + 150, this.guiTop + 59, 50, 20, ""+tile.getData().getMaxSpawnsPerSpawner()));
        getTextField(4).setFGColor(0x854a13);
        getTextField(4).setNumbersOnly();
        getTextField(4).listener = (f)->tile.getData().setMaxSpawnsPerSpawner(f.getInteger());
        addTextField(new GuiFixedTextFieldNop(5, this, this.guiLeft + 150, this.guiTop + 81, 50, 20, ""+tile.getData().getTickRangeMin()));
        getTextField(5).setFGColor(0x854a13);
        getTextField(5).setNumbersOnly();
        getTextField(5).listener = (f)->tile.getData().setTickRangeMin(f.getInteger());
        addTextField(new GuiFixedTextFieldNop(6, this, this.guiLeft + 210, this.guiTop + 81, 50, 20, ""+tile.getData().getTickRangeMax()));
        getTextField(6).setFGColor(0x854a13);
        getTextField(6).setNumbersOnly();
        getTextField(6).listener = (f)->tile.getData().setTickRangeMax(f.getInteger());
        addTextField(new GuiFixedTextFieldNop(7, this, this.guiLeft + 150, this.guiTop + 103, 50, 20, ""+tile.getData().getPlayerDetectionRange()));
        getTextField(7).setFGColor(0x854a13);
        getTextField(7).setNumbersOnly();
        getTextField(7).listener = (f)->tile.getData().setPlayerDetectionRange(f.getInteger());

        this.addButton(new GuiButtonYesNo(this, 2, this.guiLeft + 150, this.guiTop + 125, 40, 20,
                tile.getData().isRedstoneControl(), (b)-> tile.getData().setRedstoneControl(((GuiButtonYesNo)b).getBoolean())));
        this.getButton(2).setFGColor(0x854a13);

        this.addButton(new GuiButtonYesNo(this, 3, this.guiLeft + 150, this.guiTop + 147, 40, 20,
                tile.getData().isSafeSpawn(), (b)-> tile.getData().setSafeSpawn(((GuiButtonYesNo)b).getBoolean())));
        this.getButton(3).setFGColor(0x854a13);

        this.addButton(new GuiButtonYesNo(this, 4, this.guiLeft + 150, this.guiTop + 169, 40, 20,
                tile.getData().isAllowWater(), (b)-> tile.getData().setAllowWater(((GuiButtonYesNo)b).getBoolean())));
        this.getButton(4).setFGColor(0x854a13);

        this.addButton(new GuiButtonYesNo(this, 5, this.guiLeft + 150, this.guiTop + 191, 40, 20,
                tile.getData().isCanSpawnUnder(), (b)-> tile.getData().setCanSpawnUnder(((GuiButtonYesNo)b).getBoolean())));
        this.getButton(5).setFGColor(0x854a13);
        guiTop-=10;

        GuiButtonNop btnBack = new GuiButtonNop(this, 6, this.guiLeft + 330, this.guiTop + 20, 60, 20, "Back", (b)->{
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