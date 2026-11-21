package dev.txadmin.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

/** Kotak di tengah dengan kolom ketik (Teleport, Heal player, Announcement). */
public final class PromptScreen extends TxBase {
    private final Screen parent;
    private final String title;
    private final String label;
    private final int max;
    private final Consumer<String> onSubmit;
    private EditBox box;
    private int bx, by;
    private static final int W = 280, H = 104;

    public PromptScreen(Screen parent, String title, String label, int max, Consumer<String> onSubmit) {
        super(title);
        this.parent = parent;
        this.title = title;
        this.label = label;
        this.max = max;
        this.onSubmit = onSubmit;
    }

    @Override
    protected void init() {
        bx = (this.width - W) / 2;
        by = (this.height - H) / 2;
        box = new EditBox(this.font, bx + 12, by + 40, W - 24, 20, Component.literal(label));
        box.setMaxLength(max);
        box.setHint(Component.literal(label));
        this.addRenderableWidget(box);
        this.setInitialFocus(box);
        hit(bx + 12, by + 70, 124, 22, this::submit);
        hit(bx + W - 136, by + 70, 124, 22, () -> this.minecraft.setScreenAndShow(parent));
    }

    private void submit() {
        String v = box.getValue().trim();
        if (v.isEmpty()) { Toasts.show("error", "Type something first"); return; }
        onSubmit.accept(v);
        this.minecraft.setScreenAndShow(parent);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float delta) {
        g.fill(bx, by, bx + W, by + H, PANEL);
        g.fill(bx, by, bx + W, by + 2, ACCENT);
        super.extractRenderState(g, mx, my, delta);
        g.text(this.font, title, bx + 12, by + 12, 0xFFFFFFFF, true);
        g.text(this.font, label, bx + 12, by + 26, MUTED, true);
        drawButton(g, bx + 12, by + 70, 124, 22, "Send", 0xFF16A34A, mx, my);
        drawButton(g, bx + W - 136, by + 70, 124, 22, "Back", 0xFF475569, mx, my);
        drawToast(g);
    }

    private void drawButton(GuiGraphicsExtractor g, int x, int y, int w, int h, String t, int color, int mx, int my) {
        g.fill(x, y, x + w, y + h, inside(mx, my, x, y, w, h) ? (color | 0xFF000000) : (color & 0xFFCCCCCC | 0xFF000000));
        g.text(this.font, t, x + (w - this.font.width(t)) / 2, y + 7, 0xFFFFFFFF, true);
    }
}
