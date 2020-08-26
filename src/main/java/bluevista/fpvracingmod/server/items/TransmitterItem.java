package bluevista.fpvracingmod.server.items;

import bluevista.fpvracingmod.config.Config;
import bluevista.fpvracingmod.server.entities.DroneEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TransmitterItem extends Item {

    public TransmitterItem(Settings settings) {
        super(settings);
    }

    public static DroneEntity droneFromTransmitter(ItemStack stack, PlayerEntity player) {
        DroneEntity drone = null;

        if (stack.getItem() instanceof TransmitterItem)
            if (stack.getSubTag(Config.BIND) != null)
                drone = DroneEntity.getByUuid(player, stack.getSubTag(Config.BIND).getUuid(Config.BIND));
        return drone;
    }

    public static boolean isBoundTransmitter(ItemStack item, DroneEntity drone) {
        try {
            if (item.getSubTag(Config.BIND) != null)
                return (drone.getUuid().equals(item.getSubTag(Config.BIND).getUuid(Config.BIND)));
            else return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isHoldingTransmitter(PlayerEntity player) {
        return player.inventory.getMainHandStack().getItem() instanceof TransmitterItem;
    }
}

