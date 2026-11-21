package dev.txadmin.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class TxAdminClient implements ClientModInitializer {
    private static KeyMapping key(String name, KeyMapping.Category cat) {
        // UNKNOWN = belum dipasang; atur sendiri di Options > Controls > Key Binds > txAdmin
        return KeyMappingHelper.registerKeyMapping(new KeyMapping(name, InputConstants.UNKNOWN.getValue(), cat));
    }

    @Override
    public void onInitializeClient() {
        PayloadTypeRegistry.serverboundPlay().register(TxPayload.TYPE, TxPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(TxPayload.TYPE, TxPayload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(TxPayload.TYPE,
                (payload, context) -> context.client().execute(() -> Net.handle(payload.data())));

        KeyMapping.Category cat = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("txadmin", "main"));
        KeyMapping menu = key("key.txadmin.menu", cat);
        KeyMapping noclip = key("key.txadmin.noclip", cat);
        KeyMapping god = key("key.txadmin.god", cat);
        KeyMapping heal = key("key.txadmin.heal", cat);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menu.consumeClick()) client.setScreenAndShow(new MainScreen());
            while (noclip.consumeClick()) Net.send("mode|" + ("noclip".equals(State.mode) ? "normal" : "noclip"));
            while (god.consumeClick()) Net.send("mode|" + ("god".equals(State.mode) ? "normal" : "god"));
            while (heal.consumeClick()) Net.send("heal|myself");
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> Net.send("hello"));
    }
}
