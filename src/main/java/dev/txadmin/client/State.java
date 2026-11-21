package dev.txadmin.client;

/** Status terakhir dari server (mode pemain dan Player IDs). */
public final class State {
    public static String mode = "normal";
    public static boolean ids = false;
    public static int version = 0;

    private State() { }

    /** Format: mode=noclip;ids=1 */
    public static void parse(String s) {
        for (String part : s.split(";")) {
            String[] kv = part.split("=", 2);
            if (kv.length < 2) continue;
            if (kv[0].equals("mode")) mode = kv[1];
            if (kv[0].equals("ids")) ids = kv[1].equals("1");
        }
        version++;
    }
}
