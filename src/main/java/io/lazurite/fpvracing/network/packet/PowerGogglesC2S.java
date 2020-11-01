package io.lazurite.fpvracing.network.packet;

import io.lazurite.fpvracing.server.ServerInitializer;
import io.lazurite.fpvracing.server.ServerTick;
import io.lazurite.fpvracing.server.entity.FlyableEntity;
import io.lazurite.fpvracing.server.entity.flyable.QuadcopterEntity;
import io.lazurite.fpvracing.server.item.GogglesItem;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;

public class PowerGogglesC2S {
    public static final Identifier PACKET_ID = new Identifier(ServerInitializer.MODID, "power_goggles_c2s");

    public static void accept(PacketContext context, PacketByteBuf buf) {
        PlayerEntity player = context.getPlayer();
        ItemStack hat = player.inventory.armor.get(3);
        boolean on = buf.readBoolean();
        String[] keys = new String[] {
            buf.readString(32767),
            buf.readString(32767)
        };

        context.getTaskQueue().execute(() -> {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

            ServerInitializer.SERVER_PLAYER_KEYS.remove(player.getUuid());
            ServerInitializer.SERVER_PLAYER_KEYS.put(player.getUuid(), keys);

            if (hat.getItem() instanceof GogglesItem) {
                if (on && !GogglesItem.isInGoggles(serverPlayer)) {
                    List<FlyableEntity> flyables = FlyableEntity.getList(serverPlayer, 500);

                    for (FlyableEntity entity : flyables) {
                        if (serverPlayer.distanceTo(entity) > QuadcopterEntity.TRACKING_RANGE) {
                            continue;
                        } else if (GogglesItem.isOnSameChannel(entity, serverPlayer)) {
                            GogglesItem.setOn(hat, true);
                            ServerTick.setView(serverPlayer, entity);
                        }
                    }
                } else {
                    GogglesItem.setOn(hat, false);
                    ServerTick.resetView(serverPlayer);
                }
            }
        });
    }

    public static void send(boolean on, String[] keys) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(on);

        for (String key : keys) {
            buf.writeString(key);
        }

        ClientSidePacketRegistry.INSTANCE.sendToServer(PACKET_ID, buf);
    }

    public static void register() {
        ServerSidePacketRegistry.INSTANCE.register(PACKET_ID, PowerGogglesC2S::accept);
    }
}
