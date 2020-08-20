package bluevista.fpvracingmod.network.config;

import bluevista.fpvracingmod.config.Config;
import bluevista.fpvracingmod.server.ServerInitializer;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class ConfigC2S {
    public static final Identifier PACKET_ID = new Identifier(ServerInitializer.MODID, "client_config_server_packet");

    // sends the entire config from the client to the server
    public static void accept(PacketContext context, PacketByteBuf buf) {

        UUID uuid = context.getPlayer().getUuid();
        Config config = new Config(buf);

        context.getTaskQueue().execute(() -> {
            ServerInitializer.serverPlayerConfigs.remove(uuid);
            ServerInitializer.serverPlayerConfigs.put(uuid, config);
        });
    }

    public static void send(Config config) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeString(Config.ALL);
        config.serialize(buf);

        ClientSidePacketRegistry.INSTANCE.sendToServer(PACKET_ID, buf);
    }

    public static void register() {
        ServerSidePacketRegistry.INSTANCE.register(PACKET_ID, ConfigC2S::accept);
    }
}