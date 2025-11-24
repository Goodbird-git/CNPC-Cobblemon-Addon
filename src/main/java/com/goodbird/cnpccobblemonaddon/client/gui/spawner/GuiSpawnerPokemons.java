package com.goodbird.cnpccobblemonaddon.client.gui.spawner;

import com.goodbird.cnpccobblemonaddon.block.tile.PokemonSpawnerTile;
import com.goodbird.cnpccobblemonaddon.client.gui.components.GuiListNop;
import com.goodbird.cnpccobblemonaddon.constants.CMEnumGuiType;
import com.goodbird.cnpccobblemonaddon.network.SPacketGetPokemonList;
import com.goodbird.cnpccobblemonaddon.network.SPacketRemotePokemonTp;
import com.goodbird.cnpccobblemonaddon.network.SPacketRemotePokemonsDespawn;
import com.goodbird.cnpccobblemonaddon.network.SPacketRowEditGuiOpen;
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

import java.util.ArrayList;

public class GuiSpawnerPokemons extends GuiBasePokemon implements IGuiData {
    private final PokemonSpawnerTile tile;
    private GuiListNop guiListNop;
    private CompoundTag pokemonList;

    public GuiSpawnerPokemons(BlockPos pos) {
        this.tile = (PokemonSpawnerTile)this.player.level().getBlockEntity(pos);
        Packets.sendServer(new SPacketGetPokemonList(pos));
    }

    public void init() {
        super.init();

        this.addLabel(new GuiLabel(2, "Name", this.guiLeft + 40, this.guiTop + 76, 0x854a13));
        this.addLabel(new GuiLabel(3, "Level", this.guiLeft + 110, this.guiTop + 76, 0x854a13));
        this.addLabel(new GuiLabel(4, "Shiny", this.guiLeft + 155, this.guiTop + 76, 0x854a13));
        this.addLabel(new GuiLabel(5, "Position", this.guiLeft + 200, this.guiTop + 76, 0x854a13));

        guiListNop = new GuiListNop(Minecraft.getInstance(), width, 100, guiTop+90, guiTop+215, 25);
        guiListNop.setRenderBackground(false);
        guiListNop.setRenderTopAndBottom(false);
        fillList();
        this.addRenderableWidget(guiListNop);
    }

    public void fillList(){
        if(pokemonList==null || !pokemonList.contains("data")) return;
        guiListNop.clearEntries();
        ListTag list = pokemonList.getList("data", 10);

        GuiButtonNop btnDespawn = new GuiButtonNop(this, 0, this.guiLeft + 20, this.guiTop + 20, 60, 20, "Despawn", (b)->{
            save();
            Packets.sendServer(new SPacketRemotePokemonsDespawn(tile.getBlockPos()));
            guiListNop.clearEntries();
        });
        btnDespawn.setFGColor(0x854a13);
        addButton(btnDespawn);

        GuiButtonNop btnBack = new GuiButtonNop(this, 1, this.guiLeft + 330, this.guiTop + 20, 60, 20, "Back", (b)->{
            save();
            Packets.sendServer(new SPacketGuiOpen(CMEnumGuiType.CobbleSpawnerMainSettings, tile.getBlockPos()));
        });
        btnBack.setFGColor(0x854a13);
        addButton(btnBack);

        for(int i=0;i<list.size();i++){
            ArrayList<AbstractWidget> widgets = new ArrayList<>();
            CompoundTag pokemon = list.getCompound(i);
            widgets.add(new GuiLabel(8, pokemon.getString("Name"), this.guiLeft + 25, 0, 0x854a13));
            widgets.add(new GuiLabel(9, ""+pokemon.getInt("Level"), this.guiLeft + 105, 0, 0x854a13));
            widgets.add(new GuiLabel(10, pokemon.getBoolean("Shiny")?"Yes":"No", this.guiLeft + 150, 0, 0x854a13));
            BlockPos pos = BlockPos.of(pokemon.getLong("Pos"));
            String posStr = "("+pos.getX()+", "+pos.getY()+", "+pos.getZ()+")";
            widgets.add(new GuiLabel(11, posStr, this.guiLeft + 195, 0, 0x854a13));
            GuiButtonNop btnTp = new GuiButtonNop(this, 5, this.guiLeft + 295, 0, 60, 20, "Teleport", (b)->{
                save();
                Packets.sendServer(new SPacketRemotePokemonTp(pokemon.getInt("Id")));
                close();
            });
            btnTp.setFGColor(0x854a13);
            widgets.add(btnTp);
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
        this.pokemonList = compound;
        this.init();
    }
}