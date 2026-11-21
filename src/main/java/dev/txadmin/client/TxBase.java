package dev.txadmin.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;

/** Dasar layar TxAdmin: area klik tak terlihat + gambar sendiri. */
public abstract class TxBase extends Screen implements TxScreen {
    protected static final int BG = 0xF0141821;
    protected static final int PANEL = 0xFF1B2030;
    protected static final int ROW = 0xFF232A3B;
    protected static final int ROW_HOVER = 0xFF2E3750;
    protected static final int ACCENT = 0xFF3B82F6;
    protected static final int MUTED = 0xFF8A93A8;

    protected TxBase(String title) { super(Component.literal(title)); }

    /** Area klik tak terlihat (tombol vanilla dengan alpha 0). */
    protected void hit(int x, int y, int w, int h, Runnable r) {
        Button b = Button.builder(Component.empty(), btn -> r.run()).bounds(x, y, w, h).build();
        b.setAlpha(0f);
        this.addRenderableWidget(b);
    }

    protected static boolean inside(int mx, int my, int x, int y, int w, int h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }

    protected void drawToast(GuiGraphicsExtractor g) { Toasts.draw(g, this.font, this.width); }
}
