package bluevista.fpvracingmod.client.input.keybinds;

import bluevista.fpvracingmod.network.keybinds.EMPC2S;
import bluevista.fpvracingmod.server.ServerInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class EMPKeybind {
    private static KeyBinding key;

    public static void callback(MinecraftClient client) {
        if (key.wasPressed()) EMPC2S.send(500);
    }

    public static void register() {
        key = new KeyBinding(
                "key." + ServerInitializer.MODID + ".emp",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                "category." + ServerInitializer.MODID + ".keys"
        );

        KeyBindingHelper.registerKeyBinding(key);
        ClientTickCallback.EVENT.register(EMPKeybind::callback);
    }
}
