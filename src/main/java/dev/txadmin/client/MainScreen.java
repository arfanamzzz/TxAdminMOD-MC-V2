package dev.txadmin.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Menu utama txAdmin: Player Mode, Teleport, Heal, Send Announcement, Toggle Player IDs. */
public final class MainScreen extends TxBase {
    private static final String[] MODES = {"normal", "noclip", "god", "jump"};
    private static final String[] MODE_LABEL = {"Normal", "Noclip", "God Mode", "Super Jump"};
    private static final String[] HEAL = {"Myself", "Player", "Everyone"};
    private static final String[] ICON = {"N", "T", "+", "A", "#"};
    private static final int[] ICON_COLOR = {0xFF3B82F6, 0xFF8B5CF6, 0xFFEF4444, 0xFFF59E0B, 0xFF10B981};
    private static final int W = 320, ROW_H = 26, GAP = 4;

    private int modeSel = 0, healSel = 0, seenVersion = -1;
    private int px, py;

    public MainScreen() {
        super("txAdmin");
        syncState();
    }

    private void syncState() {
        for (int i = 0; i < MODES.length; i++) if (MODES[i].equals(State.mode)) modeSel = i;
        seenVersion = State.version;
    }

    private int rowY(int i) { return py + 62 + i * (ROW_H + GAP); }

    @Override
    protected void init() {
        px = (this.width - W) / 2;
        py = (this.height - 236) / 2;
        // tab
        hit(px + 12 + 60, py + 34, 64, 16, () -> Toasts.show("info", "PLAYERS tab: coming in the next update"));
        hit(px + 12 + 130, py + 34, 64, 16, () -> Toasts.show("info", "TXADMIN tab: coming in the next update"));

        int main = W - 16 - 56;
        // 0: Player Mode (<> + enter)
        hit(px + 8, rowY(0), main, ROW_H, () -> Net.send("mode|" + MODES[modeSel]));
        hit(px + W - 8 - 52, rowY(0), 26, ROW_H, () -> modeSel = (modeSel + MODES.length - 1) % MODES.length);
        hit(px + W - 8 - 26, rowY(0), 26, ROW_H, () -> modeSel = (modeSel + 1) % MODES.length);
        // 1: Teleport
        hit(px + 8, rowY(1), W - 16, ROW_H, () -> this.minecraft.setScreenAndShow(
                new PromptScreen(this, "Teleport", "Player ID", 32, id -> Net.send("tp|" + id))));
        // 2: Heal (<> + enter)
        hit(px + 8, rowY(2), main, ROW_H, () -> {
            if (healSel == 0) Net.send("heal|myself");
            else if (healSel == 2) Net.send("heal|everyone");
            else this.minecraft.setScreenAndShow(new PromptScreen(this, "Heal", "Player ID", 32, id -> Net.send("heal|player|" + id)));
        });
        hit(px + W - 8 - 52, rowY(2), 26, ROW_H, () -> healSel = (healSel + HEAL.length - 1) % HEAL.length);
        hit(px + W - 8 - 26, rowY(2), 26, ROW_H, () -> healSel = (healSel + 1) % HEAL.length);
        // 3: Announcement
        hit(px + 8, rowY(3), W - 16, ROW_H, () -> this.minecraft.setScreenAndShow(
                new PromptScreen(this, "Send Announcement", "Message", 256, m -> Net.send("announce|" + m))));
        // 4: Toggle Player IDs
        hit(px + 8, rowY(4), W - 16, ROW_H, () -> Net.send("ids|toggle"));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float delta) {
        super.extractRenderState(g, mx, my, delta);
        if (seenVersion != State.version) syncState();

        g.fill(px, py, px + W, py + 236, PANEL);
        g.fill(px, py, px + W, py + 2, ACCENT);
        g.text(this.font, "txAdmin", px + 12, py + 12, 0xFFFFFFFF, true);
        g.text(this.font, "v1.0.0", px + 12 + this.font.width("txAdmin") + 4, py + 12, 0xFF5B6478, true);

        g.text(this.font, "MAIN", px + 12, py + 38, 0xFFFFFFFF, true);
        g.fill(px + 12, py + 50, px + 12 + this.font.width("MAIN"), py + 52, ACCENT);
        g.text(this.font, "PLAYERS", px + 12 + 60, py + 38, MUTED, true);
        g.text(this.font, "TXADMIN", px + 12 + 130, py + 38, MUTED, true);
        g.fill(px, py + 54, px + W, py + 55, 0xFF2A3247);

        String[] label = {
                "Player Mode: " + MODE_LABEL[modeSel],
                "Teleport: Type player ID",
                "Heal: " + HEAL[healSel],
                "Send Announcement",
                "Toggle Player IDs: " + (State.ids ? "On" : "Off")
        };
        for (int i = 0; i < 5; i++) {
            int y = rowY(i);
            boolean hover = inside(mx, my, px + 8, y, W - 16, ROW_H);
            g.fill(px + 8, y, px + W - 8, y + ROW_H, hover ? ROW_HOVER : ROW);
            g.fill(px + 14, y + 4, px + 32, y + 22, ICON_COLOR[i]);
            g.text(this.font, ICON[i], px + 23 - this.font.width(ICON[i]) / 2, y + 9, 0xFFFFFFFF, true);
            g.text(this.font, label[i], px + 40, y + 9, 0xFFFFFFFF, true);
            if (i == 0 || i == 2) {
                g.text(this.font, "<", px + W - 8 - 52 + 9, y + 9, MUTED, true);
                g.text(this.font, ">", px + W - 8 - 26 + 9, y + 9, MUTED, true);
            }
        }
        drawToast(g);
    }
}
