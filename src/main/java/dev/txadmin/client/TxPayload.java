package dev.txadmin.client;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** Satu string teks "perintah|argumen" dua arah antara mod dan plugin (channel txadmin:main). */
public record TxPayload(String data) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<TxPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("txadmin", "main"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TxPayload> CODEC =
            StreamCodec.composite(ByteBufCodecs.STRING_UTF8, TxPayload::data, TxPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() { return TYPE; }
}
