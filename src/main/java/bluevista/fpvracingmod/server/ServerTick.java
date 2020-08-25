package bluevista.fpvracingmod.server;

import bluevista.fpvracingmod.server.entities.DroneEntity;
import bluevista.fpvracingmod.server.items.GogglesItem;
import bluevista.fpvracingmod.server.items.TransmitterItem;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class ServerTick {

    public static void tick(MinecraftServer server) {
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
        for (ServerPlayerEntity player : players) {

            if (GogglesItem.isWearingGoggles(player) && !isInGoggles(player)) {
                List<DroneEntity> drones = DroneEntity.getNearbyDrones(player, 320);
                for (DroneEntity drone : drones) {
                    if (GogglesItem.isOnRightChannel(drone, player)) {
                        setView(player, drone);
                    }

                    if(TransmitterItem.isBoundTransmitter(player.getMainHandStack(), drone)) {
                        drone.acceptInputFrom(player.getUuid());
                    }
                }
            }

            if (!GogglesItem.isWearingGoggles(player) && isInGoggles(player) || !GogglesItem.isOn(player) || player.getCameraEntity() != null && player.getCameraEntity().removed)
                resetView(player);
        }
    }

    public static void setView(ServerPlayerEntity player, DroneEntity drone) {
        if(!(player.getCameraEntity() instanceof DroneEntity)) {
            drone.setInfiniteTracking(true);
            player.setCameraEntity(drone);
        }
    }

    public static void resetView(ServerPlayerEntity player) {
        if(player.getCameraEntity() instanceof DroneEntity) {
            DroneEntity drone = (DroneEntity) player.getCameraEntity();
            drone.setInfiniteTracking(false);
            player.setCameraEntity(player);
        }
    }

    public static boolean isInGoggles(ServerPlayerEntity player) {
        return player.getCameraEntity() instanceof DroneEntity;
    }

    public static void register() {
        ServerTickCallback.EVENT.register(ServerTick::tick);
    }
}
