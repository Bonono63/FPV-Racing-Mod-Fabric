package bluevista.fpvracingmod.mixin;

import bluevista.fpvracingmod.client.ClientTick;
import bluevista.fpvracingmod.client.renderers.StaticRenderer;
import bluevista.fpvracingmod.server.entities.DroneEntity;
import bluevista.fpvracingmod.server.items.GogglesItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin class modifies the behavior of the on-screen HUD. For instance,
 * the {@link InGameHudMixin#renderCrosshair(MatrixStack, CallbackInfo)} injection
 * prevents the crosshair from rendering while the player is flying a drone.
 * @author Patrick Hofmann
 */
@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow @Final MinecraftClient client;

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/entity/player/PlayerInventory;getArmorStack(I)Lnet/minecraft/item/ItemStack;"
            )
    )
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo info) {
//        if (client.options.getPerspective().isFirstPerson() && client.player.inventory.getArmorStack(3).getItem() instanceof GogglesItem) {
//            if (GogglesItem.isOn(client.player) && !(client.getCameraEntity() instanceof DroneEntity)) {
//                StaticRenderer.render(10, 20, 10, 20, tickDelta);
//            }
//        }
    }

    /**
     * This mixin method removes the crosshair from the player's screen whenever
     * they are flying a {@link bluevista.fpvracingmod.server.entities.DroneEntity}.
     * @param matrices the matrix stack
     * @param info required by every mixin injection
     */
    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void renderCrosshair(MatrixStack matrices, CallbackInfo info) {
        if (ClientTick.isInGoggles()) {
            info.cancel();
        }
    }
}
