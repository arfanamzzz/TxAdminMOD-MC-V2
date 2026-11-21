package dev.txadmin.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Kotak panjang di atas layar: biru = info, hijau = berhasil, merah = gagal. */
public final class Toasts {
    private static String text = "";
    private static String kind = "info";
    private static long until = 0;

    private Toasts() { }

    public static void show(String k, String t) {
        kind = k;
        text = t;
        until = System.currentTimeMillis() + 3000;
    }

    public static void draw(GuiGraphicsExtractor g, Font font, int screenWidth) {
        if (System.currentTimeMillis() > until || text.isEmpty()) return;
        int bg = switch (kind) { case "success" -> 0xE016A34A; case "error" -> 0xE0DC2626; default -> 0xE02563EB; };
        String icon = switch (kind) { case "success" -> "OK"; case "error" -> "X"; default -> "!"; };
        int w = font.width(text) + 46, h = 22, x = (screenWidth - w) / 2, y = 8;
        g.fill(x, y, x + w, y + h, bg);
        g.fill(x + 6, y + 5, x + 20, y + 17, 0x55FFFFFF);
        g.text(font, icon, x + 13 - font.width(icon) / 2, y + 7, 0xFFFFFFFF, true);
        g.text(font, text, x + 28, y + 7, 0xFFFFFFFF, true);
    }
}
