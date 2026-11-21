package dev.txadmin.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;

public final class Net {
    private Net() { }

    public static void send(String msg) {
        if (Minecraft.getInstance().getConnection() == null) return;
        ClientPlayNetworking.send(new TxPayload(msg));
    }

    /** Pesan dari server: toast|jenis|teks, state|..., open|main */
    public static void handle(String msg) {
        int i = msg.indexOf('|');
        String cmd = i < 0 ? msg : msg.substring(0, i);
        String rest = i < 0 ? "" : msg.substring(i + 1);
        Minecraft mc = Minecraft.getInstance();
        switch (cmd) {
            case "toast" -> {
                int j = rest.indexOf('|');
                if (j > 0) Toasts.show(rest.substring(0, j), rest.substring(j + 1));
            }
            case "state" -> State.parse(rest);
            case "open" -> mc.setScreenAndShow(new MainScreen());
            default -> { }
        }
    }
}
