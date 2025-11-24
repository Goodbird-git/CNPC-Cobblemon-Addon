package com.goodbird.cnpccobblemonaddon.client.gui.components;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;

public class GuiFixedTextFieldNop extends GuiTextFieldNop {
    public GuiFixedTextFieldNop(int id, Screen parent, int i, int j, int k, int l, String s) {
        super(id, parent, i, j, k, l, s);
    }

    public GuiFixedTextFieldNop(int id, Screen parent, int i, int j, int k, int l, Component s) {
        super(id, parent, i, j, k, l, s);
    }

    public int getTextColor() {
        if (this.numbersOnly || this.floatsOnly) {
            if (this.numbersOnly && (!this.isInteger() || this.getInteger() < this.min || this.getInteger() > this.max)) {
                return 16515909;
            }

            if (this.floatsOnly && (!this.isFloat() || this.getFloat() < this.minF || this.getFloat() > this.maxF)) {
                return 16515909;
            }
        }

        return packedFGColor;
    }
}
